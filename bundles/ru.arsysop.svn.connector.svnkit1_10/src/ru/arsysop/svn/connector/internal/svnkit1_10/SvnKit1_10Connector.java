/*
 * Copyright (c) 2023, 2025 ArSysOp
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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.subversion.javahl.ClientException;
import org.apache.subversion.javahl.CommitInfo;
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
import org.eclipse.team.svn.core.connector.SVNEntry;
import org.eclipse.team.svn.core.connector.SVNEntryReference;
import org.eclipse.team.svn.core.connector.SVNEntryRevisionReference;
import org.eclipse.team.svn.core.connector.SVNExternalReference;
import org.eclipse.team.svn.core.connector.SVNMergeInfo;
import org.eclipse.team.svn.core.connector.SVNMergeInfo.LogKind;
import org.eclipse.team.svn.core.connector.SVNProperty;
import org.eclipse.team.svn.core.connector.SVNRevision;
import org.eclipse.team.svn.core.connector.SVNRevisionRange;
import org.eclipse.team.svn.core.connector.configuration.ISVNConfigurationEventHandler;
import org.tmatesoft.svn.core.internal.wc.SVNFileUtil;
import org.tmatesoft.svn.core.javahl17.SVNClientImpl;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableArray;
import ru.arsysop.svn.connector.internal.adapt.jhlsv.LockNullableAdapter;
import ru.arsysop.svn.connector.internal.adapt.jhlsv.MergeInfoAdapter;
import ru.arsysop.svn.connector.internal.adapt.jhlsv.NodeKindAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.AdaptClientNotifyCallback;
import ru.arsysop.svn.connector.internal.adapt.svjhl.ChoiceAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.DepthAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.InfoCallbackAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.InheritedCallbackAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.LogKindAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.LogMessageCallbackAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.PropertyCallbackAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.RevisionAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.RevisionRangeAdapter;
import ru.arsysop.svn.connector.internal.adapt.svjhl.StatusCallbackAdapter;

//TODO
final class SvnKit1_10Connector implements ISVNConnector {

	private final CallWatch watch;
	private final SVNClientImpl client;
	private final List<ISVNCredentialsPrompt> prompts = new ArrayList<>();
//FIXME: AF: not sure why do we need this
	private final List<ISVNConfigurationEventHandler> handlers = new ArrayList<>();

	SvnKit1_10Connector(String name) {
		SVNFileUtil.setSleepForTimestamp(false);// not time to relax
		//FIXME: AF: check if we can remove "trilead" from target
		System.setProperty("svnkit.ssh.client", "apache"); //$NON-NLS-1$ //$NON-NLS-2$
		watch = new CallWatch(name);
		client = SVNClientImpl.newInstance();
		client.notification2(new AdaptClientNotifyCallback(watch.notifications));
	}

	@Override
	public void addCallListener(ISVNCallListener listener) {
		watch.addListener(listener);
	}

	@Override
	public void removeCallListener(ISVNCallListener listener) {
		watch.removeListener(listener);
	}

	@Override
	public String getConfigDirectory() throws SVNConnectorException {
		return watch.queryFast(ISVNCallListener.GET_CONFIG_DIRECTORY, //
				Collections.emptyMap(), //
				p -> client.getConfigDirectory());
	}

	@Override
	public void setConfigDirectory(String directory) throws SVNConnectorException {
		watch.commandFast(ISVNCallListener.SET_CONFIG_DIRECTORY, //
				Map.of("configDir", directory), // //$NON-NLS-1$
				p -> client.setConfigDirectory(directory));
	}

	@Override
	public void setConfigurationEventHandler(ISVNConfigurationEventHandler handler) throws SVNConnectorException {
		handlers.clear();
		watch.commandSafe(ISVNCallListener.SET_CONFIGURATION_EVENT_HANDLER, //
				Map.of("configHandler", handler), //$NON-NLS-1$
				p -> handlers.add(handler));
	}

	@Override
	public ISVNConfigurationEventHandler getConfigurationEventHandler() throws SVNConnectorException {
		return watch.querySafe(ISVNCallListener.GET_CONFIGURATION_EVENT_HANDLER, //
				Collections.emptyMap(), p -> handlers.stream().findAny().orElse(null));
	}

	@Override
	public void setUsername(String username) {
		Map<String, Object> parameters = Map.of("username", username); //$NON-NLS-1$
		watch.commandSafe(ISVNCallListener.SET_USERNAME, parameters, p -> client.username(username));
	}

	@Override
	public void setPassword(String password) {
		Map<String, Object> parameters = Map.of("password", password); //$NON-NLS-1$
		watch.commandSafe(ISVNCallListener.SET_PASSWORD, parameters, p -> client.username(password));
	}

	@Override
	public void setPrompt(ISVNCredentialsPrompt prompt) {
		Map<String, Object> parameters = Map.of("prompt", prompt); //$NON-NLS-1$
		prompts.clear();
		prompts.add(prompt);
		watch.commandSafe(ISVNCallListener.SET_PROMPT, parameters,
				p -> client.setPrompt(new CredentialsCallback(prompt)));
	}

	@Override
	public ISVNCredentialsPrompt getPrompt() {
		//FIXME: AF: check if we need this method at all
		return watch.querySafe(ISVNCallListener.GET_PROMPT, //
				Collections.emptyMap(), //
				p -> prompts.stream().findAny().orElse(null));
	}

	@Override
	public void setNotificationCallback(ISVNNotificationCallback notify) {
		System.out.println("SvnKit1_10Connector.setNotificationCallback()");
		//TODO
	}

	@Override
	public ISVNNotificationCallback getNotificationCallback() {
		System.out.println("SvnKit1_10Connector.getNotificationCallback()");
		//TODO
		return null;
	}

	@Override
	public void setConflictResolver(ISVNConflictResolutionCallback listener) {
		System.out.println("SvnKit1_10Connector.setConflictResolver()");
		//TODO
	}

	@Override
	public ISVNConflictResolutionCallback getConflictResolver() {
		System.out.println("SvnKit1_10Connector.getConflictResolver()");
		//TODO
		return null;
	}

	@Override
	public long checkout(SVNEntryRevisionReference fromReference, String destPath, SVNDepth depth, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("fromReference", fromReference);
		parameters.put("destPath", destPath);
		parameters.put("depth", depth);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		return watch.queryLong(ISVNCallListener.CHECKOUT, //
				parameters, //
				callback(monitor), p -> client.checkout(//
						fromReference.path, destPath, //
						new RevisionAdapter(fromReference.revision).adapt(), //
						new RevisionAdapter(fromReference.pegRevision).adapt(), //
						new DepthAdapter(depth).adapt(), //
						(options & Options.IGNORE_EXTERNALS) != 0, //
						(options & Options.ALLOW_UNVERSIONED_OBSTRUCTIONS) != 0));
	}

	@Override
	public void lock(String[] path, String comment, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path);
		parameters.put("comment", comment);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.LOCK, //
				parameters, //
				callback(monitor), //
				p -> client.lock(//
						new HashSet<>(Arrays.asList(path)), //
						comment, //
						(options & Options.FORCE) != 0));
	}

	@Override
	public void unlock(String[] path, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.UNLOCK, //
				parameters, //
				callback(monitor), //
				p -> client.unlock(//
						new HashSet<>(Arrays.asList(path)), //
						(options & Options.FORCE) != 0));
	}

	@Override
	public void add(String path, SVNDepth depth, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path);
		parameters.put("depth", depth);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.ADD, //
				parameters, //
				callback(monitor), //
				p -> client.add(path, //
						new DepthAdapter(depth).adapt(), //
						(options & Options.FORCE) != 0, //
						(options & Options.INCLUDE_IGNORED) != 0, //
						(options & Options.IGNORE_AUTOPROPS) != 0, //
						(options & Options.INCLUDE_PARENTS) != 0));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void commit(String[] path, String message, String[] changeLists, SVNDepth depth, long options, Map revProps,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path);
		parameters.put("message", message);
		parameters.put("changeLists", changeLists);
		parameters.put("depth", depth);
		parameters.put("options", Long.valueOf(options));
		parameters.put("revProps", revProps);
		parameters.put("monitor", monitor);
		CommitStatusCallback status = new CommitStatusCallback(monitor);
		watch.commandCallback(ISVNCallListener.COMMIT, //
				parameters, //
				callback(monitor), //
				p -> client.commit(//
						new HashSet<>(Arrays.asList(path)), //
						new DepthAdapter(depth).adapt(), //
						(options & Options.KEEP_LOCKS) != 0, //
						(options & Options.KEEP_CHANGE_LIST) != 0, //
						Optional.ofNullable(changeLists).map(Arrays::asList).orElse(null), //
						new RevProps((Map<String, Object>) revProps).adapt(), //
						new CommitMessage(message), status),
				p -> Optional.ofNullable(status.info)//
				.map(CommitInfo::getPostCommitError)//
				.ifPresent(e -> p.put("lastPostCommitError", e)));
	}

	@Override
	public long[] update(String[] path, SVNRevision revision, SVNDepth depth, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path);
		parameters.put("revision", revision);
		parameters.put("depth", depth);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		return watch.queryLong(ISVNCallListener.UPDATE, //
				parameters, //
				callback(monitor), p -> client.update(//
						new HashSet<>(Arrays.asList(path)), //
						new RevisionAdapter(revision).adapt(), //
						new DepthAdapter(depth).adapt(), //
						(options & Options.DEPTH_IS_STICKY) != 0, //
						(options & Options.INCLUDE_PARENTS) != 0, //
						(options & Options.IGNORE_EXTERNALS) != 0, //
						(options & Options.ALLOW_UNVERSIONED_OBSTRUCTIONS) != 0));
	}

	@Override
	public long switchTo(String path, SVNEntryRevisionReference toReference, SVNDepth depth, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path);
		parameters.put("toReference", toReference);
		parameters.put("depth", depth);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		return watch.queryLong(ISVNCallListener.SWITCH, //
				parameters, //
				callback(monitor), p -> client.doSwitch(//
						path, //
						toReference.path, //
						new RevisionAdapter(toReference.revision).adapt(), //
						new RevisionAdapter(toReference.pegRevision).adapt(), //
						new DepthAdapter(depth).adapt(), //
						(options & Options.DEPTH_IS_STICKY) != 0, //
						(options & Options.IGNORE_EXTERNALS) != 0, //
						(options & Options.ALLOW_UNVERSIONED_OBSTRUCTIONS) != 0, //
						(options & Options.IGNORE_ANCESTRY) != 0));
	}

	@Override
	public void revert(String[] paths, SVNDepth depth, String[] changeLists, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("paths", paths);
		parameters.put("depth", depth);
		parameters.put("changeLists", changeLists);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.REVERT, //
				parameters, //
				callback(monitor), //
				p -> revert(paths, //
						depth, //
						changeLists));
	}

	private void revert(String[] paths, SVNDepth depth, String[] changeLists) throws ClientException {
		for (String path : paths) {
			client.revert(path, //
					new DepthAdapter(depth).adapt(), //
					Optional.ofNullable(changeLists).map(Arrays::asList).orElse(null));
		}
	}

	@Override
	public void status(String path, SVNDepth depth, long options, String[] changeLists,
			ISVNEntryStatusCallback callback, ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path);
		parameters.put("depth", depth);
		parameters.put("options", Long.valueOf(options));
		parameters.put("changeLists", changeLists);
		parameters.put("callback", callback);
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.STATUS, //
				parameters, //
				callback(monitor), //
				p -> client.status(path, //
						new DepthAdapter(depth).adapt(), //
						(options & Options.SERVER_SIDE) != 0, //
						(options & Options.INCLUDE_UNCHANGED) != 0, //
						(options & Options.INCLUDE_IGNORED) != 0, //
						(options & Options.IGNORE_EXTERNALS) != 0, //
						Optional.ofNullable(changeLists).map(Arrays::asList).orElse(null), //
						new StatusCallbackAdapter(callback).adapt()));
	}

	@Override
	public void relocate(String from, String to, String path, SVNDepth depth, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("from", from);
		parameters.put("to", to);
		parameters.put("path", path);
		parameters.put("depth", depth);
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.RELOCATE, //
				parameters, //
				callback(monitor), //
				p -> client.relocate(from, //
						to, //
						path, //
						depth == SVNDepth.INFINITY));
	}

	@Override
	public void cleanup(String path, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.CLEANUP, //
				parameters, //
				callback(monitor), //
				p -> client.cleanup(path));
	}

	@Override
	public void mergeTwo(SVNEntryRevisionReference reference1, SVNEntryRevisionReference reference2, String localPath,
			SVNDepth depth, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reference1", reference1);
		parameters.put("reference2", reference2);
		parameters.put("localPath", localPath);
		parameters.put("depth", depth);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.MERGE_TWO, //
				parameters, //
				callback(monitor), //
				p -> client.merge(//
						reference1.path, //
						new RevisionAdapter(reference1.revision).adapt(), //
						reference2.path, //
						new RevisionAdapter(reference2.revision).adapt(), //
						localPath, //
						(options & Options.FORCE) != 0, //
						new DepthAdapter(depth).adapt(), //
						(options & Options.IGNORE_ANCESTRY) != 0, //
						(options & Options.SIMULATE) != 0, //
						(options & Options.RECORD_ONLY) != 0));
	}

	@Override
	public void merge(SVNEntryReference reference, SVNRevisionRange[] revisions, String localPath, SVNDepth depth,
			long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reference", reference);
		parameters.put("revisions", revisions);
		parameters.put("localPath", localPath);
		parameters.put("depth", depth);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.MERGE, //
				parameters, //
				callback(monitor), //
				p -> client.merge(//
						reference.path, //
						new RevisionAdapter(reference.pegRevision).adapt(), //
						Arrays.asList(new SvnNullableArray<>(revisions,
								org.apache.subversion.javahl.types.RevisionRange[]::new,
								r -> new RevisionRangeAdapter(r).adapt()).adapt()), //
						localPath, //
						(options & Options.FORCE) != 0, //
						new DepthAdapter(depth).adapt(), //
						(options & Options.IGNORE_MERGE_HISTORY) != 0, //
						(options & Options.IGNORE_ANCESTRY) != 0, //
						(options & Options.SIMULATE) != 0, //
						(options & Options.RECORD_ONLY) != 0));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void mergeReintegrate(SVNEntryReference reference, String localPath, long options,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reference", reference);
		parameters.put("localPath", localPath);
		parameters.put("options", Long.valueOf(options));
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.MERGE_REINTEGRATE, //
				parameters, //
				callback(monitor), //
				p -> client.mergeReintegrate(//
						reference.path, //
						new RevisionAdapter(reference.pegRevision).adapt(), //
						localPath, //
						(options & Options.SIMULATE) != 0));
	}

	@Override
	public SVNMergeInfo getMergeInfo(SVNEntryReference reference, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reference", reference);
		parameters.put("monitor", monitor);
		return watch.queryAdapt(ISVNCallListener.GET_MERGE_INFO, //
				parameters, //
				callback(monitor), //
				p -> client.getMergeinfo(//
						reference.path, //
						new RevisionAdapter(reference.pegRevision).adapt()),
				v -> new MergeInfoAdapter(v).adapt());
	}

	@Override
	public void listMergeInfoLog(LogKind logKind, SVNEntryReference reference, SVNEntryReference mergeSourceReference,
			SVNRevisionRange mergeSourceRange, String[] revProps, SVNDepth depth, long options, ISVNLogEntryCallback cb,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("logKind", logKind);
		parameters.put("reference", reference);
		parameters.put("mergeSourceReference", mergeSourceReference);
		parameters.put("mergeSourceRange", mergeSourceRange);
		parameters.put("revProps", revProps);
		parameters.put("options", Long.valueOf(options));
		parameters.put("cb", cb);
		parameters.put("monitor", monitor);
		final ISVNLogEntryCallback callback1 = cb;
		watch.commandLong(ISVNCallListener.LIST_MERGE_INFO_LOG, //
				parameters, //
				callback(monitor), //
				p -> client.getMergeinfoLog(//
						new LogKindAdapter(logKind).adapt(), //
						reference.path, //
						new RevisionAdapter(reference.pegRevision).adapt(), //
						mergeSourceReference.path, //
						new RevisionAdapter(mergeSourceReference.pegRevision).adapt(), //
						new RevisionAdapter(mergeSourceRange.from).adapt(), //
						new RevisionAdapter(mergeSourceRange.to).adapt(), //
						(options & Options.DISCOVER_PATHS) != 0, //
						new DepthAdapter(depth).adapt(), //
						new HashSet<>(Arrays.asList(revProps)), //
						new LogMessageCallbackAdapter(callback1).adapt()));
	}

	@Override
	public String[] suggestMergeSources(SVNEntryReference reference, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reference", reference);
		parameters.put("monitor", monitor);
		return watch.queryAdapt(ISVNCallListener.SUGGEST_MERGE_SOURCES, //
				parameters, //
				callback(monitor), //
				p -> client.suggestMergeSources(//
						reference.path, //
						new RevisionAdapter(reference.pegRevision).adapt()),
				v -> Optional.ofNullable(v).map(s -> s.toArray(String[]::new)).orElse(null));
	}

	@Override
	public void resolve(String path, Choice conflictResult, SVNDepth depth, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("path", path);
		parameters.put("conflictResult", conflictResult);
		parameters.put("depth", depth);
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.RESOLVE, //
				parameters, //
				callback(monitor), //
				p -> client.resolve(//
						path, //
						new DepthAdapter(depth).adapt(), //
						new ChoiceAdapter(conflictResult).adapt()));
	}

	@Override
	public void addToChangeList(String[] paths, String changelist, SVNDepth depth, String[] changeLists,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("paths", paths);
		parameters.put("changelist", changelist);
		parameters.put("depth", depth);
		parameters.put("changeLists", changeLists);
		parameters.put("monitor", monitor);
		watch.commandLong(ISVNCallListener.ADD_TO_CHANGE_LIST, //
				parameters, //
				callback(monitor), //
				p -> client.addToChangelist(//
						new HashSet<>(Arrays.asList(paths)), //
						changelist, //
						new DepthAdapter(depth).adapt(),
						Optional.ofNullable(changeLists).map(Arrays::asList).orElse(null)));
	}

	@Override
	public void removeFromChangeLists(String[] paths, SVNDepth depth, String[] changeLists, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.removeFromChangeLists()");
		//TODO
	}

	@Override
	public void dumpChangeLists(String[] changeLists, String rootPath, SVNDepth depth, ISVNChangeListCallback cb,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.dumpChangeLists()");
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void importTo(String path, String url, String message, SVNDepth depth, long options, Map revProps,
			ISVNImportFilterCallback filter, ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.importTo()");
		//TODO
	}

	@Override
	public long exportTo(SVNEntryRevisionReference fromReference, String destPath, String nativeEOL, SVNDepth depth,
			long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.exportTo()");
		//TODO
		return 0;
	}

	@Override
	public void diffTwo(SVNEntryRevisionReference refPrev, SVNEntryRevisionReference refNext, String relativeToDir,
			String fileName, SVNDepth depth, long options, String[] changeLists, long outputOptions,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.diffTwo()");
		//TODO
	}

	@Override
	public void diff(SVNEntryReference reference, SVNRevisionRange range, String relativeToDir, String fileName,
			SVNDepth depth, long options, String[] changeLists, long outputOptions, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.diff()");
		//TODO
	}

	@Override
	public void diffTwo(SVNEntryRevisionReference refPrev, SVNEntryRevisionReference refNext, String relativeToDir,
			OutputStream stream, SVNDepth depth, long options, String[] changeLists, long outputOptions,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.diffTwo()");
		//TODO
	}

	@Override
	public void diff(SVNEntryReference reference, SVNRevisionRange range, String relativeToDir, OutputStream stream,
			SVNDepth depth, long options, String[] changeLists, long outputOptions, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.diff()");
		//TODO
	}

	@Override
	public void diffStatusTwo(SVNEntryRevisionReference refPrev, SVNEntryRevisionReference refNext, SVNDepth depth,
			long options, String[] changeLists, ISVNDiffStatusCallback cb, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.diffStatusTwo()");
		//TODO
	}

	@Override
	public void diffStatus(SVNEntryReference reference, SVNRevisionRange range, SVNDepth depth, long options,
			String[] changeLists, ISVNDiffStatusCallback cb, ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.diffStatus()");
		//TODO
	}

	@Override
	public void getInfo(SVNEntryRevisionReference reference, SVNDepth depth, long options, String[] changeLists,
			ISVNEntryInfoCallback cb, ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reference", reference); //$NON-NLS-1$
		parameters.put("depth", depth); //$NON-NLS-1$
		parameters.put("options", Long.valueOf(options)); //$NON-NLS-1$
		parameters.put("changeLists", changeLists); //$NON-NLS-1$
		parameters.put("cb", cb); //$NON-NLS-1$
		parameters.put("monitor", monitor); //$NON-NLS-1$
		watch.commandLong(ISVNCallListener.GET_INFO, parameters, callback(monitor), p -> client.info2(//
				reference.path, //
				new RevisionAdapter(reference.revision).adapt(), //
				new RevisionAdapter(reference.pegRevision).adapt(), //
				new DepthAdapter(depth).adapt(), //
				Optional.ofNullable(changeLists).map(Arrays::asList).orElse(null), //FIXME: AF: investigate if we can provide empty list here
				new InfoCallbackAdapter(cb).adapt()));
	}

	@Override
	public SVNProperty[] streamFileContent(SVNEntryRevisionReference reference, long options, OutputStream stream,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.streamFileContent()");
		//TODO
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void mkdir(String[] path, String message, long options, Map revProps, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.mkdir()");
		//TODO
	}

	@Override
	public void moveLocal(String[] srcPaths, String dstPath, long options, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.moveLocal()");
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void moveRemote(String[] srcPaths, String dstPath, String message, long options, Map revProps,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.moveRemote()");
		//TODO
	}

	@Override
	public void copyLocal(SVNEntryRevisionReference[] srcPaths, String destPath, long options,
			Map<String, List<SVNExternalReference>> externalsToPin, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.copyLocal()");
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void copyRemote(SVNEntryRevisionReference[] srcPaths, String destPath, String message, long options,
			Map revProps, Map<String, List<SVNExternalReference>> externalsToPin, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.copyRemote()");
		//TODO
	}

	@Override
	public void removeLocal(String[] path, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.removeLocal()");
		//TODO
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void removeRemote(String[] path, String message, long options, Map revProps, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.removeRemote()");
		//TODO
	}

	@Override
	public void listHistoryLog(SVNEntryReference reference, SVNRevisionRange[] revisionRanges, String[] revProps,
			long limit, long options, ISVNLogEntryCallback cb, ISVNProgressMonitor monitor)
					throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reference", reference); //$NON-NLS-1$
		parameters.put("revisionRanges", revisionRanges); //$NON-NLS-1$
		parameters.put("revProps", revProps); //$NON-NLS-1$
		parameters.put("limit", Long.valueOf(limit)); //$NON-NLS-1$
		parameters.put("options", Long.valueOf(options)); //$NON-NLS-1$
		parameters.put("cb", cb); //$NON-NLS-1$
		parameters.put("monitor", monitor); //$NON-NLS-1$
		watch.commandLong(ISVNCallListener.LIST_HISTORY_LOG, parameters, callback(monitor), //
				p -> client.logMessages(//
						reference.path, //
						new RevisionAdapter(reference.pegRevision).adapt(), //
						Arrays.asList(new SvnNullableArray<>(revisionRanges,
								org.apache.subversion.javahl.types.RevisionRange[]::new,
								r -> new RevisionRangeAdapter(r).adapt()).adapt()), //
						(options & Options.STOP_ON_COPY) != 0, //
						(options & Options.DISCOVER_PATHS) != 0, //
						(options & Options.INCLUDE_MERGED_REVISIONS) != 0, //
						new HashSet<>(Arrays.asList(revProps)), //
						limit, //
						new LogMessageCallbackAdapter(cb).adapt()));
	}

	@Override
	public void annotate(SVNEntryReference reference, SVNRevisionRange revisionRange, long options, long diffOptions,
			ISVNAnnotationCallback callback, ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.annotate()");
		//TODO
	}

	@Override
	public void listEntries(SVNEntryRevisionReference reference, SVNDepth depth, int fields, long options,
			ISVNEntryCallback cb, ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reference", reference); //$NON-NLS-1$
		parameters.put("depth", depth); //$NON-NLS-1$
		parameters.put("direntFields", Integer.valueOf(fields)); //$NON-NLS-1$
		parameters.put("options", Long.valueOf(options)); //$NON-NLS-1$
		parameters.put("cb", cb); //$NON-NLS-1$
		parameters.put("monitor", monitor); //$NON-NLS-1$
		watch.commandLong(ISVNCallListener.LIST, parameters, callback(monitor),
				p -> listEntries(reference, depth, fields, options, cb));
	}

	private void listEntries(SVNEntryRevisionReference reference, SVNDepth depth, int fields, long options,
			ISVNEntryCallback cb) throws ClientException {
		client.list(//
				reference.path, //
				new RevisionAdapter(reference.revision).adapt(), //
				new RevisionAdapter(reference.pegRevision).adapt(), //
				new DepthAdapter(depth).adapt(), //
				fields, //
				(options & Options.FETCH_LOCKS) != 0, //
				new org.apache.subversion.javahl.callback.ListCallback() {

					public void doEntry(org.apache.subversion.javahl.types.DirEntry entry,
							org.apache.subversion.javahl.types.Lock lock) {
						String path = entry.getPath();
						if ((path == null || path.length() == 0)
								&& entry.getNodeKind() != org.apache.subversion.javahl.types.NodeKind.file) {
							return;
						}
						cb.next(new SVNEntry(path, //
								entry.getLastChangedRevisionNumber(), //
								Optional.ofNullable(entry.getLastChanged()).map(Date::getTime).orElse(0L), //
								entry.getLastAuthor(), //
								entry.getHasProps(), //
								new NodeKindAdapter(entry.getNodeKind()).adapt(), //
								entry.getSize(), //
								new LockNullableAdapter(lock).adapt()));
					}

				}
				);
	}

	@Override
	public void listProperties(SVNEntryRevisionReference reference, SVNDepth depth, String[] changeLists, long options,
			ISVNPropertyCallback callback, ISVNProgressMonitor monitor) throws SVNConnectorException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reference", reference); //$NON-NLS-1$
		parameters.put("depth", depth); //$NON-NLS-1$
		parameters.put("changeLists", changeLists); //$NON-NLS-1$
		parameters.put("callback", callback); //$NON-NLS-1$
		parameters.put("monitor", monitor); //$NON-NLS-1$
		watch.commandLong(ISVNCallListener.GET_PROPERTIES, parameters, callback(monitor),
				p -> listProperties(reference, depth, changeLists, options, callback));
	}

	private void listProperties(SVNEntryRevisionReference reference, SVNDepth depth, String[] changeLists, long options,
			ISVNPropertyCallback callback) throws ClientException {

		if ((options & Options.INHERIT_PROPERTIES) != 0) {
			final ISVNPropertyCallback callback1 = callback;
			client.properties(reference.path, //
					new RevisionAdapter(reference.revision).adapt(), new RevisionAdapter(reference.pegRevision).adapt(), //
					new DepthAdapter(depth).adapt(), //
					Optional.ofNullable(changeLists).map(Arrays::asList).orElse(null), //FIXME: AF: investigate if we can provide empty list here
					new InheritedCallbackAdapter(callback1).adapt());
		} else {
			final ISVNPropertyCallback callback1 = callback;
			client.properties(reference.path, //
					new RevisionAdapter(reference.revision).adapt(), new RevisionAdapter(reference.pegRevision).adapt(), //
					new DepthAdapter(depth).adapt(), //
					Optional.ofNullable(changeLists).map(Arrays::asList).orElse(null), //FIXME: AF: investigate if we can provide empty list here
					new PropertyCallbackAdapter(callback1).adapt());
		}
	}

	@Override
	public SVNProperty getProperty(SVNEntryRevisionReference reference, String name, String[] changeLists,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
		System.out.println("SvnKit1_10Connector.getProperty()");
		return null;
	}

	@Override
	public void setPropertyLocal(String[] path, SVNProperty property, SVNDepth depth, long options,
			String[] changeLists, ISVNProgressMonitor monitor) throws SVNConnectorException {
		//TODO
		System.out.println("SvnKit1_10Connector.setPropertyLocal()");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setPropertyRemote(SVNEntryReference reference, SVNProperty property, String message, long options,
			Map revProps, ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.setPropertyRemote()");
		//TODO
	}

	@Override
	public SVNProperty[] listRevisionProperties(SVNEntryReference reference, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.listRevisionProperties()");
		//TODO
		return null;
	}

	@Override
	public SVNProperty getRevisionProperty(SVNEntryReference reference, String name, ISVNProgressMonitor monitor)
			throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.getRevisionProperty()");
		//TODO
		return null;
	}

	@Override
	public void setRevisionProperty(SVNEntryReference reference, SVNProperty property, String originalValue,
			long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.setRevisionProperty()");
		//TODO
	}

	@Override
	public void upgrade(String path, ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.upgrade()");
		//TODO
	}

	@Override
	public void patch(String patchPath, String targetPath, int stripCount, long options, ISVNPatchCallback callback,
			ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.patch()");
		//TODO
	}

	@Override
	public void vacuum(String path, long options, ISVNProgressMonitor monitor) throws SVNConnectorException {
		System.out.println("SvnKit1_10Connector.vacuum()");
		//TODO
	}

	private ProgressCallback callback(ISVNProgressMonitor monitor) {
		return new ProgressCallback(monitor, client::cancelOperation);
	}

	@Override
	public void dispose() {
		client.dispose();
		watch.dispose();
	}

}
