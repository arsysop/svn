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

import org.apache.subversion.javahl.types.ChangePath;
import org.eclipse.team.svn.core.connector.SVNLogPath;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class ChangePathAdapter extends SvnNullableConstructor<ChangePath, SVNLogPath> {

	public ChangePathAdapter(ChangePath source) {
		super(source);
	}

	@Override
	protected SVNLogPath adapt(ChangePath source) {
		return new SVNLogPath(//
				source.getPath(), //
				changeType(source.getAction()), //
				source.getCopySrcPath(), //
				source.getCopySrcRevision(), //
				new TristateAdapter(source.getTextMods()).adapt(), //
				new TristateAdapter(source.getPropMods()).adapt()//
				);
	}

	private SVNLogPath.ChangeType changeType(org.apache.subversion.javahl.types.ChangePath.Action tAction) {
		switch (tAction) {
			case delete:
				return SVNLogPath.ChangeType.DELETED;
			case modify:
				return SVNLogPath.ChangeType.MODIFIED;
			case replace:
				return SVNLogPath.ChangeType.REPLACED;
			default:
				return SVNLogPath.ChangeType.ADDED;
		}
	}

}
