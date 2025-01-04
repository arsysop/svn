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

import org.apache.subversion.javahl.types.Revision;
import org.eclipse.team.svn.core.connector.SVNRevision;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class RevisionAdapter extends SvnNullableConstructor<Revision, SVNRevision> {

	public RevisionAdapter(Revision revision) {
		super(revision);
	}

	@Override
	protected SVNRevision adapt(Revision source) {
		Revision.Kind kind = source.getKind();
		if (kind == org.apache.subversion.javahl.types.Revision.Kind.base) {
			return SVNRevision.BASE;
		} else if (kind == org.apache.subversion.javahl.types.Revision.Kind.committed) {
			return SVNRevision.COMMITTED;
		} else if (kind == org.apache.subversion.javahl.types.Revision.Kind.head) {
			return SVNRevision.HEAD;
		} else if (kind == org.apache.subversion.javahl.types.Revision.Kind.previous) {
			return SVNRevision.PREVIOUS;
		} else if (kind == org.apache.subversion.javahl.types.Revision.Kind.working) {
			return SVNRevision.WORKING;
		} else if (kind == org.apache.subversion.javahl.types.Revision.Kind.unspecified) {
			return SVNRevision.START;
		} else if (kind == org.apache.subversion.javahl.types.Revision.Kind.number) {
			return SVNRevision.fromNumber(((Revision.Number) source).getNumber());
		} else {
			return SVNRevision.fromDate(((Revision.DateSpec) source).getDate().getTime());
		}
	}

}