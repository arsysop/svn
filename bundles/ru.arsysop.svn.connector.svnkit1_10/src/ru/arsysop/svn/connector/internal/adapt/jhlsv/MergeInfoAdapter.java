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

package ru.arsysop.svn.connector.internal.adapt.jhlsv;

import java.util.Collections;
import java.util.Optional;

import org.apache.subversion.javahl.types.Mergeinfo;
import org.apache.subversion.javahl.types.RevisionRange;
import org.eclipse.team.svn.core.connector.SVNMergeInfo;
import org.eclipse.team.svn.core.connector.SVNRevisionRange;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableArray;
import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class MergeInfoAdapter extends SvnNullableConstructor<Mergeinfo, SVNMergeInfo> {

	public MergeInfoAdapter(Mergeinfo source) {
		super(source);
	}

	@Override
	protected SVNMergeInfo adapt(Mergeinfo source) {
		SVNMergeInfo result = new SVNMergeInfo();
		for (String path : Optional.ofNullable(source.getPaths()).orElseGet(Collections::emptySet)) {
			result.addRevisions(path,
					new SvnNullableArray<>(//
							source.getRevisionRange(path).toArray(RevisionRange[]::new), //
							SVNRevisionRange[]::new, //
							s -> new RevisionRangeAdapter(s).adapt()).adapt());
		}
		return result;
	}

}
