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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.subversion.javahl.ConflictDescriptor;
import org.apache.subversion.javahl.ConflictDescriptor.Operation;
import org.apache.subversion.javahl.ConflictDescriptor.Reason;
import org.eclipse.team.svn.core.connector.SVNConflictDescriptor;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class ConflictDescriptorNullableAdapter
extends SvnNullableConstructor<ConflictDescriptor, SVNConflictDescriptor> {

	private Map<ConflictDescriptor.Reason, SVNConflictDescriptor.Reason> reasons;

	public ConflictDescriptorNullableAdapter(ConflictDescriptor source) {
		super(source);
		reasons = new HashMap<>();
		reasons.put(org.apache.subversion.javahl.ConflictDescriptor.Reason.edited,
				SVNConflictDescriptor.Reason.MODIFIED);
		reasons.put(org.apache.subversion.javahl.ConflictDescriptor.Reason.obstructed,
				SVNConflictDescriptor.Reason.OBSTRUCTED);
		reasons.put(org.apache.subversion.javahl.ConflictDescriptor.Reason.deleted,
				SVNConflictDescriptor.Reason.DELETED);
		reasons.put(org.apache.subversion.javahl.ConflictDescriptor.Reason.missing,
				SVNConflictDescriptor.Reason.MISSING);
		reasons.put(org.apache.subversion.javahl.ConflictDescriptor.Reason.unversioned,
				SVNConflictDescriptor.Reason.UNVERSIONED);
		reasons.put(org.apache.subversion.javahl.ConflictDescriptor.Reason.added, SVNConflictDescriptor.Reason.ADDED);
		reasons.put(org.apache.subversion.javahl.ConflictDescriptor.Reason.replaced,
				SVNConflictDescriptor.Reason.REPLACED);
		reasons.put(org.apache.subversion.javahl.ConflictDescriptor.Reason.moved_away,
				SVNConflictDescriptor.Reason.MOVED_AWAY);
		reasons.put(org.apache.subversion.javahl.ConflictDescriptor.Reason.moved_here,
				SVNConflictDescriptor.Reason.MOVED_HERE);

	}

	@Override
	protected SVNConflictDescriptor adapt(ConflictDescriptor descr) {
		return new SVNConflictDescriptor(
				descr.getPath(), //
				kind(descr.getKind()), //
				new NodeKindAdapter(descr.getNodeKind()).adapt(), //
				descr.getPropertyName(), //
				descr.isBinary(), //
				descr.getMIMEType(), //
				action(descr.getAction()), //
				reason(descr.getReason()), //
				operation(descr.getOperation()), //
				descr.getBasePath(), //
				descr.getTheirPath(), //
				descr.getMyPath(), //
				descr.getMergedPath(), //
				new ConflictVersionNullableAdapter(descr.getSrcLeftVersion()).adapt(),
				new ConflictVersionNullableAdapter(descr.getSrcRightVersion()).adapt());
	}

	private SVNConflictDescriptor.Kind kind(org.apache.subversion.javahl.ConflictDescriptor.Kind kind) {
		if (kind == org.apache.subversion.javahl.ConflictDescriptor.Kind.property) {
			return SVNConflictDescriptor.Kind.PROPERTIES;
		} else if (kind == org.apache.subversion.javahl.ConflictDescriptor.Kind.tree) {
			return SVNConflictDescriptor.Kind.TREE;
		}
		return SVNConflictDescriptor.Kind.CONTENT;
	}

	private SVNConflictDescriptor.Action action(org.apache.subversion.javahl.ConflictDescriptor.Action tAction) {
		SVNConflictDescriptor.Action action = SVNConflictDescriptor.Action.ADD;
		if (tAction == org.apache.subversion.javahl.ConflictDescriptor.Action.edit) {
			action = SVNConflictDescriptor.Action.MODIFY;
		} else if (tAction == org.apache.subversion.javahl.ConflictDescriptor.Action.delete) {
			action = SVNConflictDescriptor.Action.DELETE;
		} else if (tAction == org.apache.subversion.javahl.ConflictDescriptor.Action.replace) {
			action = SVNConflictDescriptor.Action.REPLACE;
		}
		return action;
	}

	private SVNConflictDescriptor.Reason reason(Reason reason) {
		return Optional.ofNullable(reasons.get(reason)).orElse(SVNConflictDescriptor.Reason.MODIFIED);
	}

	private SVNConflictDescriptor.Operation operation(Operation operation) {
		if (operation == Operation.merge) {
			return SVNConflictDescriptor.Operation.MERGE;
		} else if (operation == Operation.switched) {
			return SVNConflictDescriptor.Operation.SWITCHED;
		} else if (operation == Operation.update) {
			return SVNConflictDescriptor.Operation.UPDATE;
		}
		return SVNConflictDescriptor.Operation.NONE;
	}

}
