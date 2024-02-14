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

import org.apache.subversion.javahl.ClientNotifyInformation.Action;
import org.eclipse.team.svn.core.connector.SVNNotification;
import org.eclipse.team.svn.core.connector.SVNNotification.PerformedAction;

final class ClientNotifyInformationActionAdapter extends SvnTypeMap<Action, PerformedAction> {

	ClientNotifyInformationActionAdapter(Action source) {
		super(source);
	}

	@Override
	protected Map<Action, PerformedAction> fill() {
		Map<Action, PerformedAction> map = new LinkedHashMap<>();
		map.put(Action.add, PerformedAction.ADD);
		map.put(Action.blame_revision, PerformedAction.BLAME_REVISION);
		map.put(Action.changelist_clear, PerformedAction.CHANGELIST_CLEAR);
		map.put(Action.changelist_moved, PerformedAction.CHANGELIST_MOVED);
		map.put(Action.changelist_set, PerformedAction.CHANGELIST_SET);
		map.put(Action.commit_added, PerformedAction.COMMIT_ADDED);
		map.put(Action.commit_copied, PerformedAction.COMMIT_COPIED);
		map.put(Action.commit_copied_replaced, PerformedAction.COMMIT_COPIED_REPLACED);
		map.put(Action.commit_deleted, PerformedAction.COMMIT_DELETED);
		map.put(Action.commit_modified, PerformedAction.COMMIT_MODIFIED);
		map.put(Action.commit_postfix_txdelta, PerformedAction.COMMIT_POSTFIX_TXDELTA);
		map.put(Action.commit_replaced, PerformedAction.COMMIT_REPLACED);
		map.put(Action.conflict_resolver_done, PerformedAction.CONFLICT_RESOLVER_DONE);
		map.put(Action.conflict_resolver_starting, PerformedAction.CONFLICT_RESOLVER_STARTING);
		map.put(Action.copy, PerformedAction.COPY);
		map.put(Action.delete, PerformedAction.DELETE);
		map.put(Action.exclude, PerformedAction.EXCLUDE);
		map.put(Action.exists, PerformedAction.EXISTS);
		map.put(Action.failed_conflict, PerformedAction.FAILED_CONFLICT);
		map.put(Action.failed_external, PerformedAction.FAILED_EXTERNAL);
		map.put(Action.failed_forbidden_by_server, PerformedAction.FAILED_FORBIDDEN_BY_SERVER);
		map.put(Action.failed_lock, PerformedAction.FAILED_LOCK);
		map.put(Action.failed_locked, PerformedAction.FAILED_LOCKED);
		map.put(Action.failed_missing, PerformedAction.FAILED_MISSING);
		map.put(Action.failed_no_parent, PerformedAction.FAILED_NO_PARENT);
		map.put(Action.failed_obstructed, PerformedAction.FAILED_OBSTRUCTED);
		map.put(Action.failed_out_of_date, PerformedAction.FAILED_OUT_OF_DATE);
		map.put(Action.failed_revert, PerformedAction.FAILED_REVERT);
		map.put(Action.failed_unlock, PerformedAction.FAILED_UNLOCK);
		map.put(Action.foreign_copy_begin, PerformedAction.FOREIGN_COPY_BEGIN);
		map.put(Action.foreign_merge_begin, PerformedAction.FOREIGN_MERGE_BEGIN);
		map.put(Action.left_local_modifications, PerformedAction.LEFT_LOCAL_MODIFICATIONS);
		map.put(Action.locked, PerformedAction.LOCKED);
		map.put(Action.merge_begin, PerformedAction.MERGE_BEGIN);
		map.put(Action.merge_completed, PerformedAction.MERGE_COMPLETED);
		map.put(Action.merge_elide_info, PerformedAction.MERGE_ELIDE_INFO);
		map.put(Action.merge_record_info, PerformedAction.MERGE_RECORD_INFO);
		map.put(Action.merge_record_info_begin, PerformedAction.MERGE_RECORD_INFO_BEGIN);
		map.put(Action.move_broken, PerformedAction.MOVE_BROKEN);
		map.put(Action.patch, PerformedAction.PATCH);
		map.put(Action.patch_applied_hunk, PerformedAction.PATCH_APPLIED_HUNK);
		map.put(Action.patch_hunk_already_applied, PerformedAction.PATCH_HUNK_ALREADY_APPLIED);
		map.put(Action.patch_rejected_hunk, PerformedAction.PATCH_REJECTED_HUNK);
		map.put(Action.path_nonexistent, PerformedAction.PATH_NONEXISTENT);
		map.put(Action.property_added, PerformedAction.PROPERTY_ADDED);
		map.put(Action.property_deleted, PerformedAction.PROPERTY_DELETED);
		map.put(Action.property_deleted_nonexistent, PerformedAction.PROPERTY_DELETED_NONEXISTENT);
		map.put(Action.property_modified, PerformedAction.PROPERTY_MODIFIED);
		map.put(Action.resolved, PerformedAction.RESOLVED);
		map.put(Action.restore, PerformedAction.RESTORE);
		map.put(Action.revert, PerformedAction.REVERT);
		map.put(Action.revprop_deleted, PerformedAction.REVPROP_DELETE);
		map.put(Action.revprop_set, PerformedAction.REVPROP_SET);
		map.put(Action.skip, PerformedAction.SKIP);
		map.put(Action.skip_conflicted, PerformedAction.SKIP_CONFLICTED);
		map.put(Action.status_completed, PerformedAction.STATUS_COMPLETED);
		map.put(Action.status_external, PerformedAction.STATUS_EXTERNAL);
		map.put(Action.tree_conflict, PerformedAction.TREE_CONFLICT);
		map.put(Action.unlocked, PerformedAction.UNLOCKED);
		map.put(Action.update_add, PerformedAction.UPDATE_ADD);
		map.put(Action.update_broken_lock, PerformedAction.UPDATE_BROKEN_LOCK);
		map.put(Action.update_completed, PerformedAction.UPDATE_COMPLETED);
		map.put(Action.update_delete, PerformedAction.UPDATE_DELETE);
		map.put(Action.update_external, PerformedAction.UPDATE_EXTERNAL);
		map.put(Action.update_external_removed, PerformedAction.UPDATE_EXTERNAL_REMOVED);
		map.put(Action.update_replaced, PerformedAction.UPDATE_REPLACED);
		map.put(Action.update_shadowed_add, PerformedAction.UPDATE_SHADOWED_ADD);
		map.put(Action.update_shadowed_delete, PerformedAction.UPDATE_SHADOWED_DELETE);
		map.put(Action.update_shadowed_update, PerformedAction.UPDATE_SHADOWED_UPDATE);
		map.put(Action.update_skip_access_denied, PerformedAction.UPDATE_SKIP_ACCESS_DENIED);
		map.put(Action.update_skip_obstruction, PerformedAction.UPDATE_SKIP_OBSTRUCTION);
		map.put(Action.update_skip_working_only, PerformedAction.UPDATE_SKIP_WORKING_ONLY);
		map.put(Action.update_started, PerformedAction.UPDATE_STARTED);
		map.put(Action.update_update, PerformedAction.UPDATE_UPDATE);
		map.put(Action.upgraded_path, PerformedAction.UPGRADED_PATH);
		map.put(Action.url_redirect, PerformedAction.URL_REDIRECT);
		return map;
	}

	@Override
	SVNNotification.PerformedAction defaults() {
		return SVNNotification.PerformedAction._UNKNOWN_ACTION;
	}

}
