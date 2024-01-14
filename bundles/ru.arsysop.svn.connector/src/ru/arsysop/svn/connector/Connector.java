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

package ru.arsysop.svn.connector;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.team.svn.core.connector.ISVNAnnotationCallback;
import org.eclipse.team.svn.core.connector.ISVNCallListener;
import org.eclipse.team.svn.core.connector.ISVNChangeListCallback;
import org.eclipse.team.svn.core.connector.ISVNConflictResolutionCallback;
import org.eclipse.team.svn.core.connector.ISVNConnector;
import org.eclipse.team.svn.core.connector.ISVNCredentialsPrompt;
import org.eclipse.team.svn.core.connector.ISVNDiffStatusCallback;
import org.eclipse.team.svn.core.connector.ISVNEntryCallback;
import org.eclipse.team.svn.core.connector.ISVNEntryInfoCallback;
import org.eclipse.team.svn.core.connector.ISVNEntryStatusCallback;
import org.eclipse.team.svn.core.connector.ISVNImportFilterCallback;
import org.eclipse.team.svn.core.connector.ISVNLogEntryCallback;
import org.eclipse.team.svn.core.connector.ISVNNotificationCallback;
import org.eclipse.team.svn.core.connector.ISVNPatchCallback;
import org.eclipse.team.svn.core.connector.ISVNProgressMonitor;
import org.eclipse.team.svn.core.connector.ISVNPropertyCallback;
import org.eclipse.team.svn.core.connector.SVNConflictResolution.Choice;
import org.eclipse.team.svn.core.connector.SVNConnectorException;
import org.eclipse.team.svn.core.connector.SVNDepth;
import org.eclipse.team.svn.core.connector.SVNEntryReference;
import org.eclipse.team.svn.core.connector.SVNEntryRevisionReference;
import org.eclipse.team.svn.core.connector.SVNExternalReference;
import org.eclipse.team.svn.core.connector.SVNMergeInfo;
import org.eclipse.team.svn.core.connector.SVNMergeInfo.LogKind;
import org.eclipse.team.svn.core.connector.SVNProperty;
import org.eclipse.team.svn.core.connector.SVNRevision;
import org.eclipse.team.svn.core.connector.SVNRevisionRange;
import org.eclipse.team.svn.core.connector.configuration.ISVNConfigurationEventHandler;

//TODO
final class Connector implements ISVNConnector {

	@Override
	public void addCallListener(ISVNCallListener listener) {
		//TODO
	}

	@Override
	public void removeCallListener(ISVNCallListener listener) {
		//TODO
	}

	@Override
	public String getConfigDirectory() throws SVNConnectorException {
		//TODO
		return null;
	}

	@Override
	public void setConfigDirectory(String configDir) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void setConfigurationEventHandler(ISVNConfigurationEventHandler configHandler) throws SVNConnectorException {
		//TODO
	}

	@Override
	public ISVNConfigurationEventHandler getConfigurationEventHandler() throws SVNConnectorException {
		//TODO
		return null;
	}

	@Override
	public void setUsername(String username) {
		//TODO
	}

	@Override
	public void setPassword(String password) {
		//TODO
	}

	@Override
	public void setPrompt(ISVNCredentialsPrompt prompt) {
		//TODO
	}

	@Override
	public ISVNCredentialsPrompt getPrompt() {
		//TODO
		return null;
	}

	@Override
	public void setNotificationCallback(ISVNNotificationCallback notify) {
		//TODO
	}

	@Override
	public ISVNNotificationCallback getNotificationCallback() {
		//TODO
		return null;
	}

	@Override
	public void setConflictResolver(ISVNConflictResolutionCallback listener) {
		//TODO
	}

	@Override
	public ISVNConflictResolutionCallback getConflictResolver() {
		//TODO
		return null;
	}

	@Override
	public long checkout(SVNEntryRevisionReference fromReference, String destPath, SVNDepth depth, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
		return -1;
	}

	@Override
	public void lock(String[] path, String comment, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void unlock(String[] path, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void add(String path, SVNDepth depth, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void commit(String[] path, String message, String[] changeLists, SVNDepth depth, long options, Map revProps,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public long[] update(String[] path, SVNRevision revision, SVNDepth depth, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
		return null;
	}

	@Override
	public long switchTo(String path, SVNEntryRevisionReference toReference, SVNDepth depth, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
		return 0;
	}

	@Override
	public void revert(String[] paths, SVNDepth depth, String[] changeLists, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void status(String path, SVNDepth depth, long options, String[] changeLists,
			ISVNEntryStatusCallback callback, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void relocate(String from, String to, String path, SVNDepth depth, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void cleanup(String path, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void mergeTwo(SVNEntryRevisionReference reference1, SVNEntryRevisionReference reference2, String localPath,
			SVNDepth depth, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void merge(SVNEntryReference reference, SVNRevisionRange[] revisions, String localPath, SVNDepth depth,
			long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mergeReintegrate(SVNEntryReference reference, String localPath, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public SVNMergeInfo getMergeInfo(SVNEntryReference reference, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
		return null;
	}

	@Override
	public void listMergeInfoLog(LogKind logKind, SVNEntryReference reference, SVNEntryReference mergeSourceReference,
			SVNRevisionRange mergeSourceRange, String[] revProps, SVNDepth depth, long options, ISVNLogEntryCallback cb,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public String[] suggestMergeSources(SVNEntryReference reference, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
		return null;
	}

	@Override
	public void resolve(String path, Choice conflictResult, SVNDepth depth, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void addToChangeList(String[] paths, String targetChangeList, SVNDepth depth, String[] filterByChangeLists,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void removeFromChangeLists(String[] paths, SVNDepth depth, String[] changeLists, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void dumpChangeLists(String[] changeLists, String rootPath, SVNDepth depth, ISVNChangeListCallback cb,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void importTo(String path, String url, String message, SVNDepth depth, long options, Map revProps,
			ISVNImportFilterCallback filter, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public long exportTo(SVNEntryRevisionReference fromReference, String destPath, String nativeEOL, SVNDepth depth,
			long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
		return 0;
	}

	@Override
	public void diffTwo(SVNEntryRevisionReference refPrev, SVNEntryRevisionReference refNext, String relativeToDir,
			String fileName, SVNDepth depth, long options, String[] changeLists, long outputOptions,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void diff(SVNEntryReference reference, SVNRevisionRange range, String relativeToDir, String fileName,
			SVNDepth depth, long options, String[] changeLists, long outputOptions, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		//TODO
	}

	@Override
	public void diffTwo(SVNEntryRevisionReference refPrev, SVNEntryRevisionReference refNext, String relativeToDir,
			OutputStream stream, SVNDepth depth, long options, String[] changeLists, long outputOptions,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void diff(SVNEntryReference reference, SVNRevisionRange range, String relativeToDir, OutputStream stream,
			SVNDepth depth, long options, String[] changeLists, long outputOptions, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		//TODO
	}

	@Override
	public void diffStatusTwo(SVNEntryRevisionReference refPrev, SVNEntryRevisionReference refNext, SVNDepth depth,
			long options, String[] changeLists, ISVNDiffStatusCallback cb, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		//TODO
	}

	@Override
	public void diffStatus(SVNEntryReference reference, SVNRevisionRange range, SVNDepth depth, long options,
			String[] changeLists, ISVNDiffStatusCallback cb, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void getInfo(SVNEntryRevisionReference reference, SVNDepth depth, long options, String[] changeLists,
			ISVNEntryInfoCallback cb, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public SVNProperty[] streamFileContent(SVNEntryRevisionReference reference, long options, OutputStream stream,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void mkdir(String[] path, String message, long options, Map revProps, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void moveLocal(String[] srcPaths, String dstPath, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void moveRemote(String[] srcPaths, String dstPath, String message, long options, Map revProps,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void copyLocal(SVNEntryRevisionReference[] srcPaths, String destPath, long options,
			Map<String, List<SVNExternalReference>> externalsToPin, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void copyRemote(SVNEntryRevisionReference[] srcPaths, String destPath, String message, long options,
			Map revProps, Map<String, List<SVNExternalReference>> externalsToPin, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		//TODO
	}

	@Override
	public void removeLocal(String[] path, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void removeRemote(String[] path, String message, long options, Map revProps, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
	}

	@Override
	public void listHistoryLog(SVNEntryReference reference, SVNRevisionRange[] revisionRanges, String[] revProps,
			long limit, long options, ISVNLogEntryCallback cb, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		//TODO
	}

	@Override
	public void annotate(SVNEntryReference reference, SVNRevisionRange revisionRange, long options, long diffOptions,
			ISVNAnnotationCallback callback, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void listEntries(SVNEntryRevisionReference reference, SVNDepth depth, int direntFields, long options,
			ISVNEntryCallback cb, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void listProperties(SVNEntryRevisionReference reference, SVNDepth depth, String[] changeLists, long options,
			ISVNPropertyCallback callback, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public SVNProperty getProperty(SVNEntryRevisionReference reference, String name, String[] changeLists,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
		return null;
	}

	@Override
	public void setPropertyLocal(String[] path, SVNProperty property, SVNDepth depth, long options,
			String[] changeLists, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setPropertyRemote(SVNEntryReference reference, SVNProperty property, String message, long options,
			Map revProps, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public SVNProperty[] listRevisionProperties(SVNEntryReference reference, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
		return null;
	}

	@Override
	public SVNProperty getRevisionProperty(SVNEntryReference reference, String name, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		//TODO
		return null;
	}

	@Override
	public void setRevisionProperty(SVNEntryReference reference, SVNProperty property, String originalValue,
			long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void upgrade(String path, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void patch(String patchPath, String targetPath, int stripCount, long options, ISVNPatchCallback callback,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void vacuum(String path, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
	}

	@Override
	public void dispose() {
		//TODO
	}

}
