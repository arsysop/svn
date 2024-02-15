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

package ru.arsysop.svn.connector.internal.adapt.jhlsv;

import java.util.Date;
import java.util.Optional;

import org.apache.subversion.javahl.types.Status;
import org.eclipse.team.svn.core.connector.SVNChangeStatus;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeConstructor;

public final class TypeStatusAdapter
extends SvnTypeConstructor<org.apache.subversion.javahl.types.Status, SVNChangeStatus> {

	public TypeStatusAdapter(Status source) {
		super(source);
	}

	@Override
	public SVNChangeStatus adapt() {
		return new SVNChangeStatus(
				source.getPath(), //
				source.getUrl(), //
				new NodeKindAdapter(source.getNodeKind()).adapt(), //
				source.getRevisionNumber(), //
				source.getLastChangedRevisionNumber(),
				Optional.ofNullable(source.getLastChangedDate()).map(Date::getTime).orElse(0L),
				source.getLastCommitAuthor(), //
				new StatusKindAdapter(source.getTextStatus()).adapt(), //
				new StatusKindAdapter(source.getPropStatus()).adapt(), //
				new StatusKindAdapter(source.getRepositoryTextStatus()).adapt(), //
				new StatusKindAdapter(source.getRepositoryPropStatus()).adapt(), //
				source.isLocked(), //
				source.isCopied(), //
				source.isSwitched(), //
				new LockNullableAdapter(source.getLocalLock()).adapt(), //
				new LockNullableAdapter(source.getReposLock()).adapt(), //
				source.getReposLastCmtRevisionNumber(), //
				Optional.ofNullable(source.getReposLastCmtDate()).map(Date::getTime).orElse(0L),
				new NodeKindAdapter(source.getReposKind()).adapt(), //
				source.getReposLastCmtAuthor(), //
				source.isFileExternal(), //
				source.isConflicted(), //
				null, //FIXME: AF: check if we can use empty array here
				source.getChangelist());
	}

}
