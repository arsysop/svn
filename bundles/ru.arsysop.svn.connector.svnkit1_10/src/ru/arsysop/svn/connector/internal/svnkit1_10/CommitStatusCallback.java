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

import org.eclipse.team.svn.core.connector.ISVNProgressMonitor;
import org.eclipse.team.svn.core.connector.SVNCommitStatus;

final class CommitStatusCallback implements org.apache.subversion.javahl.callback.CommitCallback {

	org.apache.subversion.javahl.CommitInfo info;
	private final ISVNProgressMonitor monitor;

	CommitStatusCallback(ISVNProgressMonitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void commitInfo(org.apache.subversion.javahl.CommitInfo info) {
		this.info = info;
		monitor.commitStatus(new SVNCommitStatus(//
				info.getPostCommitError(), //
				info.getReposRoot(), //
				info.getRevision(), //
				info.getDate() != null ? info.getDate().getTime() : 0, //
						info.getAuthor()//
				));
	}
}
