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

package ru.arsysop.svn.connector.internal.adapt.jhlsv;

import java.util.HashMap;
import java.util.Map;

import org.apache.subversion.javahl.ReposNotifyInformation;
import org.eclipse.team.svn.core.connector.SVNRepositoryNotification;
import org.eclipse.team.svn.core.connector.SVNRepositoryNotification.NodeAction;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeMap;

public final class NodeActionAdapter
extends SvnTypeMap<ReposNotifyInformation.NodeAction, SVNRepositoryNotification.NodeAction> {

	public NodeActionAdapter(ReposNotifyInformation.NodeAction source) {
		super(source);
	}

	@Override
	protected Map<ReposNotifyInformation.NodeAction, SVNRepositoryNotification.NodeAction> fill() {
		Map<ReposNotifyInformation.NodeAction, SVNRepositoryNotification.NodeAction> map = new HashMap<>();
		map.put(ReposNotifyInformation.NodeAction.change, SVNRepositoryNotification.NodeAction.CHANGE);
		map.put(ReposNotifyInformation.NodeAction.add, SVNRepositoryNotification.NodeAction.ADD);
		map.put(ReposNotifyInformation.NodeAction.deleted, SVNRepositoryNotification.NodeAction.DELETE);
		map.put(ReposNotifyInformation.NodeAction.replace, SVNRepositoryNotification.NodeAction.REPLACE);
		return map;
	}

	@Override
	protected SVNRepositoryNotification.NodeAction defaults() {
		return NodeAction.UNKNOWN;
	}

}
