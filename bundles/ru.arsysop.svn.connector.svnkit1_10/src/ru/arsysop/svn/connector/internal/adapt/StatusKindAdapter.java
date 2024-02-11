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

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.subversion.javahl.types.Status;
import org.apache.subversion.javahl.types.Status.Kind;
import org.eclipse.team.svn.core.connector.SVNEntryStatus;

final class StatusKindAdapter extends SvnTypeMap<Status.Kind, SVNEntryStatus.Kind> {

	protected StatusKindAdapter(Kind source) {
		super(source);
	}

	@Override
	protected Map<Status.Kind, SVNEntryStatus.Kind> fill() {
		Map<Status.Kind, SVNEntryStatus.Kind> map = new LinkedHashMap<>();
		map.put(Status.Kind.none, SVNEntryStatus.Kind.NONE);
		map.put(Status.Kind.unversioned, SVNEntryStatus.Kind.UNVERSIONED);
		map.put(Status.Kind.normal, SVNEntryStatus.Kind.NORMAL);
		map.put(Status.Kind.added, SVNEntryStatus.Kind.ADDED);
		map.put(Status.Kind.missing, SVNEntryStatus.Kind.MISSING);
		map.put(Status.Kind.deleted, SVNEntryStatus.Kind.DELETED);
		map.put(Status.Kind.replaced, SVNEntryStatus.Kind.REPLACED);
		map.put(Status.Kind.modified, SVNEntryStatus.Kind.MODIFIED);
		map.put(Status.Kind.merged, SVNEntryStatus.Kind.MERGED);
		map.put(Status.Kind.conflicted, SVNEntryStatus.Kind.CONFLICTED);
		map.put(Status.Kind.ignored, SVNEntryStatus.Kind.IGNORED);
		map.put(Status.Kind.obstructed, SVNEntryStatus.Kind.OBSTRUCTED);
		map.put(Status.Kind.external, SVNEntryStatus.Kind.EXTERNAL);
		map.put(Status.Kind.incomplete, SVNEntryStatus.Kind.INCOMPLETE);
		return map;
	}

	@Override
	SVNEntryStatus.Kind defaults() {
		return SVNEntryStatus.Kind.NONE;
	}

}
