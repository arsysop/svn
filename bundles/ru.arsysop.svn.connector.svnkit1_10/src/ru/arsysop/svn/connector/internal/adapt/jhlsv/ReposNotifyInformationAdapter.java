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

import org.apache.subversion.javahl.ReposNotifyInformation;
import org.eclipse.team.svn.core.connector.SVNRepositoryNotification;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeConstructor;

public final class ReposNotifyInformationAdapter
extends SvnTypeConstructor<ReposNotifyInformation, SVNRepositoryNotification> {

	public ReposNotifyInformationAdapter(ReposNotifyInformation source) {
		super(source);
	}

	@Override
	public SVNRepositoryNotification adapt() {
		return new SVNRepositoryNotification(//
				source.getPath(), //
				new NodeActionAdapter(source.getNodeAction()).adapt(),
				new ReposNotifyInformationActionAdapter(source.getAction()).adapt(), //
				source.getRevision(), source.getWarning(), //
				source.getShard(), //
				source.getNewRevision(), //
				source.getOldRevision());
	}

}