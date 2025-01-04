/*
 * Copyright (c) 2023, 2024 ArSysOp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Contributors:
 *     ArSysOp - initial API and implementation
 */

package ru.arsysop.svn.connector.internal.svnkit1_10;

import java.util.Map;
import java.util.function.Function;

import org.apache.subversion.javahl.ClientException;
import org.apache.subversion.javahl.SubversionException;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.team.svn.core.connector.ISVNCallListener;
import org.eclipse.team.svn.core.connector.SVNConnectorAuthenticationException;
import org.eclipse.team.svn.core.connector.SVNConnectorCancelException;
import org.eclipse.team.svn.core.connector.SVNConnectorException;
import org.eclipse.team.svn.core.connector.SVNConnectorUnresolvedConflictException;
import org.eclipse.team.svn.core.connector.SVNErrorCodes;
import org.eclipse.team.svn.core.utility.SVNNotificationComposite;

final class CallWatch {

	final SVNNotificationComposite notifications = new SVNNotificationComposite();
	private final ListenerList<ISVNCallListener> listeners = new ListenerList<>();
	private final Watchdog watchdog;

	CallWatch(String name) {
		watchdog = new Watchdog(name);
		watchdog.start();
	}

	void addListener(ISVNCallListener listener) {
		listeners.add(listener);
	}

	void removeListener(ISVNCallListener listener) {
		listeners.remove(listener);
	}

	<V> V querySafe(String method, Map<String, Object> parameters, QuerySafe<V> query) {
		asked(method, parameters);
		V value = query.query(parameters);
		succeeded(method, parameters, value);
		return value;
	}

	<V> V queryFast(String method, Map<String, Object> parameters, QueryFast<V> query) throws SVNConnectorException {
		asked(method, parameters);
		try {
			V value = query.query(parameters);
			succeeded(method, parameters, value);
			return value;
		} catch (ClientException ex) {
			SVNConnectorException wrap = wrap(ex);
			failed(method, parameters, wrap);
			throw wrap;
		}
	}

	<V> V queryLong(String method, Map<String, Object> parameters, ProgressCallback progress, QueryLong<V> query)
			throws SVNConnectorException {
		return queryAdapt(method, parameters, progress, query, Function.identity());
	}

	<V, A> A queryAdapt(String method, Map<String, Object> parameters, ProgressCallback progress, QueryLong<V> query,
			Function<V, A> adapter) throws SVNConnectorException {
		asked(method, parameters);
		try {
			notifications.add(progress);
			progress.start();
			watchdog.add(progress);
			A value = adapter.apply(query.query(parameters));
			succeeded(method, parameters, null);//oh, no! we need to change this interface
			return value;
		} catch (SubversionException ex) {
			SVNConnectorException wrap = wrap(ex);
			failed(method, parameters, wrap);
			throw wrap;
		} finally {
			progress.finish();
			watchdog.remove(progress);
			notifications.remove(progress);
		}
	}

	void commandFast(String method, Map<String, Object> parameters, CommandFast command) throws SVNConnectorException {
		asked(method, parameters);
		try {
			command.command(parameters);
			succeeded(method, parameters, null);//oh, no! we need to change this interface
		} catch (ClientException ex) {
			SVNConnectorException wrap = wrap(ex);
			failed(method, parameters, wrap);
			throw wrap;
		}
	}

	void commandLong(String method, Map<String, Object> parameters, ProgressCallback progress, CommandLong command)
			throws SVNConnectorException {
		commandCallback(method, parameters, progress, command, p -> {
		});
	}

	void commandCallback(String method, Map<String, Object> parameters, ProgressCallback progress, CommandLong command,
			CommandCallback callback) throws SVNConnectorException {
		asked(method, parameters);
		try {
			notifications.add(progress);
			progress.start();
			watchdog.add(progress);
			command.command(parameters);
			callback.accept(parameters);
			succeeded(method, parameters, null);//oh, no! we need to change this interface
		} catch (ClientException ex) {
			SVNConnectorException wrap = wrap(ex);
			failed(method, parameters, wrap);
			throw wrap;
		} catch (SubversionException ex) {
			SVNConnectorException wrap = wrap(ex);
			failed(method, parameters, wrap);
			throw wrap;
		} finally {
			progress.finish();
			watchdog.remove(progress);
			notifications.remove(progress);
		}
	}

	void commandSafe(String method, Map<String, Object> parameters, CommandSafe command) {
		asked(method, parameters);
		command.command(parameters);
		succeeded(method, parameters, null);//oh, no! we need to change this interface
	}

	private void asked(String method, Map<String, Object> parameters) {
		for (ISVNCallListener listener : listeners) {
			listener.asked(method, parameters);
		}
	}

	private void succeeded(String method, Map<String, Object> parameters, Object value) {
		for (ISVNCallListener listener : listeners) {
			listener.succeeded(method, parameters, value);
		}
	}

	private void failed(String method, Map<String, Object> parameters, SVNConnectorException exception) {
		for (ISVNCallListener listener : listeners) {
			listener.failed(method, parameters, exception);
		}
	}

	private SVNConnectorException wrap(ClientException ex) {
		if (authenticationFailure(ex)) {
			return new SVNConnectorAuthenticationException(ex.getMessage(), ex);
		}
		if (wasCancelled(ex)) {
			return new SVNConnectorCancelException(ex.getMessage(), ex);
		}
		if (unresolvedConflict(ex)) {
			return new SVNConnectorUnresolvedConflictException(ex.getMessage(), ex);
		}
		return new SVNConnectorException(ex.getMessage(), ex.getAprError(), ex);
	}

	private SVNConnectorException wrap(SubversionException ex) {
		return new SVNConnectorException(ex.getMessage(), ex);
	}

	private boolean authenticationFailure(ClientException t) {
		return t.getAprError() == SVNErrorCodes.raNotAuthorized;
	}

	private boolean wasCancelled(ClientException t) {
		return t.getAprError() == SVNErrorCodes.cancelled;
	}

	private boolean unresolvedConflict(ClientException t) {
		return t.getAprError() == SVNErrorCodes.fsConflict || t.getAprError() == SVNErrorCodes.fsTxnOutOfDate;
	}

	void dispose() {
		watchdog.interrupt();
	}

}
