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

import java.util.Map;

import org.apache.subversion.javahl.types.NodeKind;
import org.eclipse.team.svn.core.connector.SVNEntry;
import org.eclipse.team.svn.core.connector.SVNEntry.Kind;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeMap;

public final class NodeKindAdapter extends SvnTypeMap<NodeKind, SVNEntry.Kind> {

	public NodeKindAdapter(NodeKind source) {
		super(source);
	}

	@Override
	protected Map<NodeKind, Kind> fill() {
		return Map.of(//
				NodeKind.none, SVNEntry.Kind.NONE, //
				NodeKind.file, SVNEntry.Kind.FILE, //
				NodeKind.dir, SVNEntry.Kind.DIR, //
				NodeKind.unknown, SVNEntry.Kind.UNKNOWN, //
				NodeKind.symlink, SVNEntry.Kind.SYMLINK//
				);
	}

	@Override
	protected Kind defaults() {
		return Kind.UNKNOWN;
	}

}
