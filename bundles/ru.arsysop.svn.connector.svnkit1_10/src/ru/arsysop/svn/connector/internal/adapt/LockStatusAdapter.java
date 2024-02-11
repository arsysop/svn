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

import org.apache.subversion.javahl.ClientNotifyInformation.LockStatus;
import org.eclipse.team.svn.core.connector.SVNNotification;
import org.eclipse.team.svn.core.connector.SVNNotification.NodeLock;

final class LockStatusAdapter extends SvnTypeMap<LockStatus, NodeLock> {

	LockStatusAdapter(LockStatus source) {
		super(source);
	}

	@Override
	protected Map<LockStatus, NodeLock> fill() {
		Map<LockStatus, NodeLock> map = new LinkedHashMap<>();
		map.put(LockStatus.inapplicable, SVNNotification.NodeLock.INAPPLICABLE);
		map.put(LockStatus.unknown, SVNNotification.NodeLock.UNKNOWN);
		map.put(LockStatus.unchanged, SVNNotification.NodeLock.UNCHANGED);
		map.put(LockStatus.locked, SVNNotification.NodeLock.LOCKED);
		map.put(LockStatus.unlocked, SVNNotification.NodeLock.UNLOCKED);
		return map;
	}

	@Override
	NodeLock defaults() {
		return NodeLock.UNKNOWN;
	}

}
