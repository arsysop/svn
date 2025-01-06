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

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.subversion.javahl.ClientNotifyInformation.LockStatus;
import org.eclipse.team.svn.core.connector.SVNNotification;
import org.eclipse.team.svn.core.connector.SVNNotification.NodeLock;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeMap;

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
	protected NodeLock defaults() {
		return NodeLock.UNKNOWN;
	}

}
