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

package ru.arsysop.svn.connector.internal.adapt;

import java.util.Date;
import java.util.Optional;

import org.apache.subversion.javahl.types.Status;
import org.eclipse.team.svn.core.connector.SVNChangeStatus;

final class TypeStatusAdapter extends SvnTypeConstructor<org.apache.subversion.javahl.types.Status, SVNChangeStatus> {

	TypeStatusAdapter(Status source) {
		super(source);
	}

	@Override
	public SVNChangeStatus adapt() {
		return new SVNChangeStatus(
				source.getPath(), //
				source.getUrl(), //
				new NodeKindAdapter(source.getNodeKind()).adapt(), //
				source.getRevisionNumber(), //
				source.getLastChangedRevisionNumber(),
				Optional.ofNullable(source.getLastChangedDate()).map(Date::getTime).orElse(0L),
				source.getLastCommitAuthor(), //
				new StatusKindAdapter(source.getTextStatus()).adapt(), //
				new StatusKindAdapter(source.getPropStatus()).adapt(), //
				new StatusKindAdapter(source.getRepositoryTextStatus()).adapt(), //
				new StatusKindAdapter(source.getRepositoryPropStatus()).adapt(), //
				source.isLocked(), //
				source.isCopied(), //
				source.isSwitched(), //
				new LockAdapter(source.getLocalLock()).adapt(), //
				new LockAdapter(source.getReposLock()).adapt(), //
				source.getReposLastCmtRevisionNumber(), //
				Optional.ofNullable(source.getReposLastCmtDate()).map(Date::getTime).orElse(0L),
				new NodeKindAdapter(source.getReposKind()).adapt(), //
				source.getReposLastCmtAuthor(), //
				source.isFileExternal(), //
				source.isConflicted(), //
				null, //FIXME: AF: check if we can use empty array here
				source.getChangelist());
	}

}
