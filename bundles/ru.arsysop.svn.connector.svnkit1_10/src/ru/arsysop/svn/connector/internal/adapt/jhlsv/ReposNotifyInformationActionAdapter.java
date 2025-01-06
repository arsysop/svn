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

import org.apache.subversion.javahl.ReposNotifyInformation;
import org.apache.subversion.javahl.ReposNotifyInformation.Action;
import org.eclipse.team.svn.core.connector.SVNRepositoryNotification;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeMap;

final class ReposNotifyInformationActionAdapter
extends SvnTypeMap<ReposNotifyInformation.Action, SVNRepositoryNotification.Action> {

	ReposNotifyInformationActionAdapter(ReposNotifyInformation.Action source) {
		super(source);
	}

	@Override
	protected Map<ReposNotifyInformation.Action, SVNRepositoryNotification.Action> fill() {
		Map<Action, SVNRepositoryNotification.Action> map = new LinkedHashMap<>();
		map.put(Action.warning, SVNRepositoryNotification.Action.WARNING);
		map.put(Action.dump_rev_end, SVNRepositoryNotification.Action.DUMP_REV_END);
		map.put(Action.verify_rev_end, SVNRepositoryNotification.Action.VERIFY_REV_END);
		map.put(Action.pack_shard_start, SVNRepositoryNotification.Action.PACK_SHARD_START);
		map.put(Action.pack_shard_end, SVNRepositoryNotification.Action.PACK_SHARD_END);
		map.put(Action.pack_shard_start_revprop, SVNRepositoryNotification.Action.PACK_SHARD_START_REVPROP);
		map.put(Action.pack_shard_end_revprop, SVNRepositoryNotification.Action.PACK_SHARD_END_REVPROP);
		map.put(Action.load_txn_start, SVNRepositoryNotification.Action.LOAD_TXN_START);
		map.put(Action.load_txn_committed, SVNRepositoryNotification.Action.LOAD_TXN_COMMITTED);
		map.put(Action.load_node_start, SVNRepositoryNotification.Action.LOAD_NODE_START);
		map.put(Action.load_node_done, SVNRepositoryNotification.Action.LOAD_NODE_END);
		map.put(Action.load_copied_node, SVNRepositoryNotification.Action.LOAD_COPIED_NODE);
		map.put(Action.load_normalized_mergeinfo, SVNRepositoryNotification.Action.LOAD_NORMALIZED_MERGEINFO);
		map.put(Action.mutex_acquired, SVNRepositoryNotification.Action.MUTEX_ACQUIRED);
		map.put(Action.recover_start, SVNRepositoryNotification.Action.RECOVER_START);
		map.put(Action.upgrade_start, SVNRepositoryNotification.Action.UPGRADE_START);
		map.put(Action.load_skipped_rev, SVNRepositoryNotification.Action.LOAD_SKIPPED_REV);
		map.put(Action.verify_rev_structure, SVNRepositoryNotification.Action.VERIFY_REV_STRUCTURE);
		return map;
	}

	@Override
	protected SVNRepositoryNotification.Action defaults() {
		return SVNRepositoryNotification.Action.UNKNOWN;
	}

}
