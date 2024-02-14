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

import org.apache.subversion.javahl.ClientNotifyInformation;
import org.eclipse.team.svn.core.connector.SVNNotification;

final class ClientNotifyInformationAdapter extends SvnTypeConstructor<ClientNotifyInformation, SVNNotification> {

	ClientNotifyInformationAdapter(ClientNotifyInformation source) {
		super(source);
	}

	@Override
	public SVNNotification adapt() {
		return new SVNNotification(//
				source.getPath(), //
				new ClientNotifyInformationActionAdapter(source.getAction()).adapt(), //
				new NodeKindAdapter(source.getKind()).adapt(), //
				source.getMimeType(), //
				new LockAdapter(source.getLock()).adapt(), //
				source.getErrMsg(), //
				new ClientNotifyInformationStatusAdapter(source.getContentState()).adapt(), //
				new ClientNotifyInformationStatusAdapter(source.getPropState()).adapt(), //
				new LockStatusAdapter(source.getLockState()).adapt(), //
				source.getRevision());
	}

}