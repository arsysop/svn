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

package ru.arsysop.svn.connector.internal.adapt.svjhl;

import org.apache.subversion.javahl.ConflictResult.Choice;
import org.eclipse.team.svn.core.connector.SVNConflictResolution;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class ChoiceAdapter extends SvnNullableConstructor<SVNConflictResolution.Choice, Choice> {

	public ChoiceAdapter(SVNConflictResolution.Choice choice) {
		super(choice);
	}

	@Override
	protected Choice adapt(SVNConflictResolution.Choice choice) {
		switch (choice) {
			case CHOOSE_BASE:
				return org.apache.subversion.javahl.ConflictResult.Choice.chooseBase;
			case CHOOSE_REMOTE_FULL:
				return org.apache.subversion.javahl.ConflictResult.Choice.chooseTheirsFull;
			case CHOOSE_LOCAL_FULL:
				return org.apache.subversion.javahl.ConflictResult.Choice.chooseMineFull;
			case CHOOSE_REMOTE:
				return org.apache.subversion.javahl.ConflictResult.Choice.chooseTheirsConflict;
			case CHOOSE_LOCAL:
				return org.apache.subversion.javahl.ConflictResult.Choice.chooseMineConflict;
			case CHOOSE_MERGED:
				return org.apache.subversion.javahl.ConflictResult.Choice.chooseMerged;
			default:
		}
		return org.apache.subversion.javahl.ConflictResult.Choice.postpone;
	}

}
