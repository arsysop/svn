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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.subversion.javahl.ISVNRepos;
import org.apache.subversion.javahl.SVNRepos;
import org.eclipse.team.svn.core.connector.ISVNCallListener;
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
import org.eclipse.team.svn.core.utility.SVNRepositoryNotificationComposite;

import ru.arsysop.svn.connector.internal.adapt.svjhl.AdaptReposNotifyCallback;
import ru.arsysop.svn.connector.internal.adapt.svjhl.RevisionAdapter;

final class SvnKit1_10Manager implements ISVNManager {

	private final CallWatch watch;
	private final ISVNRepos admin;
	private final SVNRepositoryNotificationComposite composite = new SVNRepositoryNotificationComposite();

	SvnKit1_10Manager(String name) {
		watch = new CallWatch(name);
		admin = new SVNRepos();
	}

	@Override
	public void create(String repositoryPath, RepositoryKind repositoryType, String configPath, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("repositoryPath", repositoryPath); //$NON-NLS-1$
		parameters.put("repositoryType", repositoryType); //$NON-NLS-1$
		parameters.put("configPath", configPath); //$NON-NLS-1$
		parameters.put("options", options); //$NON-NLS-1$
		parameters.put("monitor", monitor); //$NON-NLS-1$
		watch.operation(ISVNCallListener.CREATE, parameters, callback(monitor),
				p -> admin.create(//
						new File(repositoryPath), //
						(options & Options.DISABLE_FSYNC_COMMIT) != 0, //
						(options & Options.KEEP_LOG) != 0, //
						Optional.ofNullable(configPath).map(File::new).orElse(null),
						Optional.ofNullable(repositoryType).orElse(ISVNManager.RepositoryKind.FSFS).id));
	}

	@Override
	public void deltify(String path, SVNRevisionRange range, ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path); //$NON-NLS-1$
		parameters.put("range", range); //$NON-NLS-1$
		parameters.put("monitor", monitor); //$NON-NLS-1$
		watch.operation(ISVNCallListener.DELTIFY, parameters, callback(monitor), p -> admin.deltify(//
				new File(path), //
				new RevisionAdapter(range.from).adapt(), //
				new RevisionAdapter(range.to).adapt()));
	}

	@Override
	public void hotCopy(String path, String targetPath, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path); //$NON-NLS-1$
		parameters.put("targetPath", targetPath); //$NON-NLS-1$
		parameters.put("options", options); //$NON-NLS-1$
		parameters.put("monitor", monitor); //$NON-NLS-1$
		watch.operation(ISVNCallListener.HOT_COPY, parameters, callback(monitor),
				p -> admin.hotcopy(//
						new File(path), //
						new File(targetPath), //
						(options & Options.CLEAN_LOGS) != 0));
	}

	@Override
	public void dump(String path, OutputStream dataOut, SVNRevisionRange range,
			ISVNRepositoryNotificationCallback callback, long options, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path); //$NON-NLS-1$
		parameters.put("dataOut", dataOut); //$NON-NLS-1$
		parameters.put("range", range); //$NON-NLS-1$
		parameters.put("callback", callback); //$NON-NLS-1$
		parameters.put("options", options); //$NON-NLS-1$
		parameters.put("monitor", monitor); //$NON-NLS-1$
		watch.operation(ISVNCallListener.DUMP, parameters, callback(monitor),
				p -> admin.dump(//
						new File(path), //
						dataOut, //
						new RevisionAdapter(range.from).adapt(), //
						new RevisionAdapter(range.to).adapt(), //
						(options & Options.INCREMENTAL) != 0, //
						(options & Options.USE_DELTAS) != 0, //
						new AdaptReposNotifyCallback(composite)));
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

	private ProgressCallback callback(ISVNProgressMonitor monitor) {
		return new ProgressCallback(monitor, admin::cancelOperation);
	}

	@Override
	public void dispose() {
		admin.dispose();
		watch.dispose();
	}

}
