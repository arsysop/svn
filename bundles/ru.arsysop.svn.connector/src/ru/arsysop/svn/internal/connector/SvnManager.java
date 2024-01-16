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

package ru.arsysop.svn.internal.connector;

import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.team.svn.core.connector.ISVNManager;
import org.eclipse.team.svn.core.connector.ISVNProgressMonitor;
import org.eclipse.team.svn.core.connector.ISVNRepositoryFreezeAction;
import org.eclipse.team.svn.core.connector.ISVNRepositoryMessageCallback;
import org.eclipse.team.svn.core.connector.ISVNRepositoryNotificationCallback;
import org.eclipse.team.svn.core.connector.SVNConnectorException;
import org.eclipse.team.svn.core.connector.SVNDepth;
import org.eclipse.team.svn.core.connector.SVNEntryReference;
import org.eclipse.team.svn.core.connector.SVNLock;
import org.eclipse.team.svn.core.connector.SVNProperty;
import org.eclipse.team.svn.core.connector.SVNRevisionRange;

//TODO
final class SvnManager implements ISVNManager {

	@Override
	public void create(String repositoryPath, RepositoryKind repositoryType, String configPath, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void deltify(String path, SVNRevisionRange range, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void hotCopy(String path, String targetPath, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void dump(String path, OutputStream dataOut, SVNRevisionRange range,
			ISVNRepositoryNotificationCallback callback, long options, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		//TODO
	}

	@Override
	public void listDBLogs(String path, ISVNRepositoryMessageCallback receiver, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void load(String path, InputStream dataInput, SVNRevisionRange range, String relativePath,
			ISVNRepositoryNotificationCallback callback, long options, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		//TODO
	}

	@Override
	public void listTransactions(String path, ISVNRepositoryMessageCallback receiver, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public long recover(String path, ISVNRepositoryNotificationCallback callback, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
		return 0;
	}

	@Override
	public void freeze(ISVNRepositoryFreezeAction action, String[] paths, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void removeTransaction(String path, String[] transactions, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void setRevisionProperty(SVNEntryReference reference, SVNProperty property, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void verify(String path, SVNRevisionRange range, ISVNRepositoryNotificationCallback callback,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public SVNLock[] listLocks(String path, SVNDepth depth, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
		return null;
	}

	@Override
	public void removeLocks(String path, String[] locks, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void upgrade(String path, ISVNRepositoryNotificationCallback callback, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void pack(String path, ISVNRepositoryNotificationCallback callback, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void dispose() {
		//TODO
	}

}
