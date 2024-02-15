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

package ru.arsysop.svn.connector.internal.adapt.svjhl;

import java.util.Date;

import org.apache.subversion.javahl.types.Revision;
import org.eclipse.team.svn.core.connector.SVNRevision;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeConstructor;

public final class RevisionAdapter extends SvnTypeConstructor<SVNRevision, Revision> {

	public RevisionAdapter(SVNRevision revision) {
		super(revision);
	}

	@Override
	public org.apache.subversion.javahl.types.Revision adapt() {
		switch (source.getKind()) {
			case BASE:
				return org.apache.subversion.javahl.types.Revision.BASE;
			case COMMITTED:
				return org.apache.subversion.javahl.types.Revision.COMMITTED;
			case HEAD:
				return org.apache.subversion.javahl.types.Revision.HEAD;
			case PREVIOUS:
				return org.apache.subversion.javahl.types.Revision.PREVIOUS;
			case WORKING:
				return org.apache.subversion.javahl.types.Revision.WORKING;
			case START:
				return org.apache.subversion.javahl.types.Revision.START;
			case NUMBER:
				return org.apache.subversion.javahl.types.Revision
						.getInstance(((SVNRevision.Number) source).getNumber());
			case DATE:
				return org.apache.subversion.javahl.types.Revision
						.getInstance(new Date(((SVNRevision.Date) source).getDate()));
			default:
				return org.apache.subversion.javahl.types.Revision
						.getInstance(new Date(((SVNRevision.Date) source).getDate()));
		}

	}

}