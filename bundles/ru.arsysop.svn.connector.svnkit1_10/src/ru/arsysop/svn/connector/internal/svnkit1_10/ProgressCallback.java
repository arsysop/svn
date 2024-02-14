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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.team.svn.core.connector.ISVNNotificationCallback;
import org.eclipse.team.svn.core.connector.ISVNProgressMonitor;
import org.eclipse.team.svn.core.connector.ISVNRepositoryNotificationCallback;
import org.eclipse.team.svn.core.connector.SVNEntry;
import org.eclipse.team.svn.core.connector.SVNEntryStatus;
import org.eclipse.team.svn.core.connector.SVNNotification;
import org.eclipse.team.svn.core.connector.SVNRepositoryNotification;

final class ProgressCallback implements ISVNNotificationCallback, ISVNRepositoryNotificationCallback {

	final ISVNProgressMonitor monitor;
	private final Cancel cancel;
	private final List<Thread> threads = new ArrayList<>();
	private Optional<Long> cancellation;
	private int current = 0;

	ProgressCallback(ISVNProgressMonitor monitor, Cancel cancel) {
		this.monitor = Objects.requireNonNull(monitor);
		this.cancel = Objects.requireNonNull(cancel);
		cancellation = Optional.empty();
	}

	void cancel() {
		try {
			cancellation = Optional.of(System.currentTimeMillis());
			cancel.cancel();
		} catch (Exception e) {
		}
	}

	public void notify(SVNNotification notification) {
		monitor.progress(current++, ISVNProgressMonitor.TOTAL_UNKNOWN,
				new ISVNProgressMonitor.ItemState(//
						notification.path, //
						notification.action.id, //
						notification.kind, //
						notification.mimeType, //
						notification.contentState.id, //
						notification.propState.id, //
						notification.revision, //
						notification.errMsg));
	}

	public void notify(SVNRepositoryNotification notification) {
		monitor.progress(current++, ISVNProgressMonitor.TOTAL_UNKNOWN,
				new ISVNProgressMonitor.ItemState(//
						notification.path, //
						notification.action.id, //
						SVNEntry.Kind.UNKNOWN, //
						null, //
						SVNEntryStatus.Kind.NONE.id, //
						SVNEntryStatus.Kind.NONE.id, //
						notification.revision, //
						notification.warning));
	}

	void start() {
		threads.add(Thread.currentThread());
	}

	void finish() {
		synchronized (this) {
			threads.clear();
		}
	}

	synchronized void interrupt() {
		threads.stream().findAny().ifPresent(Thread::interrupt);
	}

	Optional<Long> cancellation() {
		return cancellation;
	}

}
