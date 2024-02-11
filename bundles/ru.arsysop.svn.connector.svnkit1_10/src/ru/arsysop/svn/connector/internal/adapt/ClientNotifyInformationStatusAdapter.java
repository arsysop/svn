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

import org.apache.subversion.javahl.ClientNotifyInformation;
import org.apache.subversion.javahl.ClientNotifyInformation.Status;
import org.eclipse.team.svn.core.connector.SVNNotification;
import org.eclipse.team.svn.core.connector.SVNNotification.NodeStatus;

final class ClientNotifyInformationStatusAdapter
extends SvnTypeMap<org.apache.subversion.javahl.ClientNotifyInformation.Status, NodeStatus> {

	ClientNotifyInformationStatusAdapter(Status source) {
		super(source);
	}

	@Override
	protected Map<Status, NodeStatus> fill() {
		Map<Status, NodeStatus> map = new LinkedHashMap<>();
		map.put(ClientNotifyInformation.Status.inapplicable, NodeStatus.INAPPLICABLE);
		map.put(ClientNotifyInformation.Status.unknown, NodeStatus.UNKNOWN);
		map.put(ClientNotifyInformation.Status.unchanged, NodeStatus.UNCHANGED);
		map.put(ClientNotifyInformation.Status.missing, NodeStatus.MISSING);
		map.put(ClientNotifyInformation.Status.obstructed, NodeStatus.OBSTRUCTED);
		map.put(ClientNotifyInformation.Status.changed, NodeStatus.CHANGED);
		map.put(ClientNotifyInformation.Status.merged, NodeStatus.MERGED);
		map.put(ClientNotifyInformation.Status.conflicted, SVNNotification.NodeStatus.CONFLICTED);
		return map;
	}

	@Override
	NodeStatus defaults() {
		return NodeStatus.UNKNOWN;
	}

}
