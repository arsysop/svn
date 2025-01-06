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

import org.apache.subversion.javahl.ConflictResult.Choice;
import org.eclipse.team.svn.core.connector.SVNConflictResolution;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class ChoiceAdapter extends SvnNullableConstructor<Choice, SVNConflictResolution.Choice> {

	public ChoiceAdapter(Choice choice) {
		super(choice);
	}

	@Override
	protected SVNConflictResolution.Choice adapt(org.apache.subversion.javahl.ConflictResult.Choice choice) {
		if (choice == org.apache.subversion.javahl.ConflictResult.Choice.chooseBase) {
			return SVNConflictResolution.Choice.CHOOSE_BASE;
		}
		if (choice == org.apache.subversion.javahl.ConflictResult.Choice.chooseTheirsFull) {
			return SVNConflictResolution.Choice.CHOOSE_REMOTE_FULL;
		}
		if (choice == org.apache.subversion.javahl.ConflictResult.Choice.chooseMineFull) {
			return SVNConflictResolution.Choice.CHOOSE_LOCAL_FULL;
		}
		if (choice == org.apache.subversion.javahl.ConflictResult.Choice.chooseTheirsConflict) {
			return SVNConflictResolution.Choice.CHOOSE_REMOTE;
		}
		if (choice == org.apache.subversion.javahl.ConflictResult.Choice.chooseMineConflict) {
			return SVNConflictResolution.Choice.CHOOSE_LOCAL;
		}
		if (choice == org.apache.subversion.javahl.ConflictResult.Choice.chooseMerged) {
			return SVNConflictResolution.Choice.CHOOSE_MERGED;
		}
		return SVNConflictResolution.Choice.POSTPONE;
	}

}
