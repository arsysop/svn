/*
 * Copyright (c) ArSysOp 2020-2025
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
