/*
 * Copyright (c) 2023, 2024 ArSysOp
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
