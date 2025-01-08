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

import org.eclipse.team.svn.core.connector.ISVNDiffStatusCallback;
import org.eclipse.team.svn.core.connector.SVNDiffStatus;
import org.eclipse.team.svn.core.utility.SVNUtility;

import ru.arsysop.svn.connector.internal.adapt.jhlsv.NodeKindAdapter;

final class DiffSummarized implements org.apache.subversion.javahl.callback.DiffSummaryCallback {

	private final String prev;
	private final String next;
	private final boolean isFile;
	private final ISVNDiffStatusCallback cb;
	private SVNDiffStatus saved;

	public DiffSummarized(String prev, String next, boolean isFile, ISVNDiffStatusCallback cb) {
		this.prev = SVNUtility.decodeURL(prev);
		this.next = SVNUtility.decodeURL(next);
		this.isFile = isFile;
		this.cb = cb;
	}

	@Override
	public void onSummary(org.apache.subversion.javahl.DiffSummary descriptor) {
		org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind changeType = changeType(descriptor);
		org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind propChangeType = descriptor.propsChanged()
				? org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.MODIFIED
						: org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.NORMAL;
		if (changeType != org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.NORMAL
				|| propChangeType != org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.NORMAL) {
			String tPath1 = descriptor.getPath();
			String tPath2 = tPath1;
			if (tPath1.length() == 0 || isFile) {
				tPath1 = prev;
				tPath2 = next;
			} else {
				tPath1 = collapsePaths(prev, tPath1);
				tPath2 = collapsePaths(next, tPath2);
			}
			SVNDiffStatus status = new SVNDiffStatus(SVNUtility.encodeURL(tPath1), SVNUtility.encodeURL(tPath2),
					new NodeKindAdapter(descriptor.getNodeKind()).adapt(), changeType, propChangeType);
			if (saved != null) {
				if (saved.pathPrev.equals(status.pathPrev) && saved.pathNext.equals(status.pathNext)
						&& saved.textStatus == org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.DELETED
						&& status.textStatus == org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.ADDED) {
					saved = new SVNDiffStatus(SVNUtility.encodeURL(tPath1), SVNUtility.encodeURL(tPath2),
							new NodeKindAdapter(descriptor.getNodeKind()).adapt(),
							org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.REPLACED,
							org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.NORMAL);
					status = null;
				}
				cb.next(saved);
			}
			saved = status;
		}
	}

	private org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind changeType(
			org.apache.subversion.javahl.DiffSummary descriptor) {
		org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind changeType = org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.NORMAL;
		if (descriptor.getDiffKind() == org.apache.subversion.javahl.DiffSummary.DiffKind.added) {
			changeType = org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.ADDED;
		} else if (descriptor.getDiffKind() == org.apache.subversion.javahl.DiffSummary.DiffKind.deleted) {
			changeType = org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.DELETED;
		} else if (descriptor.getDiffKind() == org.apache.subversion.javahl.DiffSummary.DiffKind.modified) {
			changeType = org.eclipse.team.svn.core.connector.SVNEntryStatus.Kind.MODIFIED;
		}
		return changeType;
	}

	void doLastDiff() {
		if (saved != null) {
			cb.next(saved);
		}
	}

	private String collapsePaths(String left, String right) {
		if (base(left).equals(root(right))) {
			return dir(left) + "/" + right; //$NON-NLS-1$
		} else {
			return left + "/" + right; //$NON-NLS-1$
		}
	}

	private String base(String path) {
		int last = path.lastIndexOf('/');
		if (last != -1) {
			return path.substring(last + 1);
		} else {
			return path;
		}
	}

	private String dir(String path) {
		int last = path.lastIndexOf('/');
		if (last > 0) {
			return path.substring(0, last);
		} else {
			return path;
		}
	}

	private String root(String path) {
		int first = path.indexOf('/');
		if (first > 0) {
			return path.substring(0, first);
		} else {
			return path;
		}
	}

}
