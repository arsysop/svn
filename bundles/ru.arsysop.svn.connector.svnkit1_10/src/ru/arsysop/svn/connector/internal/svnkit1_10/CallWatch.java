/*
 * Copyright (c) ArSysOp 2020-2024
 * 
 * ArSysOp and its affiliates make no warranty of any kind
 * with regard to this material.
 * 
 * ArSysOp expressly disclaims all warranties as to the material, express,
 * and implied, including but not limited to the implied warranties of
 * merchantability, fitness for a particular purpose and non-infringement of third
 * party rights.
 * 
 * In no event shall ArSysOp be liable to you or any other person for any damages,
 * including, without limitation, any direct, indirect, incidental or consequential
 * damages, expenses, lost profits, lost data or other damages arising out of the use,
 * misuse or inability to use the material and any derived software, even if ArSysOp,
 * its affiliate or an authorized dealer has been advised of the possibility of such damages.
 *
 */

package ru.arsysop.svn.connector.internal.svnkit1_10;

import java.util.Map;

import org.apache.subversion.javahl.ClientException;
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

	void addListener(ISVNCallListener listener) {
		listeners.add(listener);
	}

	void removeListener(ISVNCallListener listener) {
		listeners.remove(listener);
	}

	<V> V query(String method, Map<String, Object> parameters, Query<V> query) throws SVNConnectorException {
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

	<V> V querySafe(String method, Map<String, Object> parameters, QuerySafe<V> query) {
		asked(method, parameters);
		V value = query.query(parameters);
		succeeded(method, parameters, value);
		return value;
	}

	<V> void command(String method, Map<String, Object> parameters, Command command) throws SVNConnectorException {
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

	<V> void commandSafe(String method, Map<String, Object> parameters, CommandSafe command) {
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

	private boolean authenticationFailure(ClientException t) {
		return t.getAprError() == SVNErrorCodes.raNotAuthorized;
	}

	private boolean wasCancelled(ClientException t) {
		return t.getAprError() == SVNErrorCodes.cancelled;
	}

	private boolean unresolvedConflict(ClientException t) {
		return t.getAprError() == SVNErrorCodes.fsConflict || t.getAprError() == SVNErrorCodes.fsTxnOutOfDate;
	}

}
