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

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.subversion.javahl.types.Status;
import org.apache.subversion.javahl.types.Status.Kind;
import org.eclipse.team.svn.core.connector.SVNEntryStatus;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeMap;

final class StatusKindAdapter extends SvnTypeMap<Status.Kind, SVNEntryStatus.Kind> {

	protected StatusKindAdapter(Kind source) {
		super(source);
	}

	@Override
	protected Map<Status.Kind, SVNEntryStatus.Kind> fill() {
		Map<Status.Kind, SVNEntryStatus.Kind> map = new LinkedHashMap<>();
		map.put(Status.Kind.none, SVNEntryStatus.Kind.NONE);
		map.put(Status.Kind.unversioned, SVNEntryStatus.Kind.UNVERSIONED);
		map.put(Status.Kind.normal, SVNEntryStatus.Kind.NORMAL);
		map.put(Status.Kind.added, SVNEntryStatus.Kind.ADDED);
		map.put(Status.Kind.missing, SVNEntryStatus.Kind.MISSING);
		map.put(Status.Kind.deleted, SVNEntryStatus.Kind.DELETED);
		map.put(Status.Kind.replaced, SVNEntryStatus.Kind.REPLACED);
		map.put(Status.Kind.modified, SVNEntryStatus.Kind.MODIFIED);
		map.put(Status.Kind.merged, SVNEntryStatus.Kind.MERGED);
		map.put(Status.Kind.conflicted, SVNEntryStatus.Kind.CONFLICTED);
		map.put(Status.Kind.ignored, SVNEntryStatus.Kind.IGNORED);
		map.put(Status.Kind.obstructed, SVNEntryStatus.Kind.OBSTRUCTED);
		map.put(Status.Kind.external, SVNEntryStatus.Kind.EXTERNAL);
		map.put(Status.Kind.incomplete, SVNEntryStatus.Kind.INCOMPLETE);
		return map;
	}

	@Override
	protected SVNEntryStatus.Kind defaults() {
		return SVNEntryStatus.Kind.NONE;
	}

}
