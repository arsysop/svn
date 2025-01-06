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

import java.util.Objects;

import org.eclipse.team.svn.core.connector.ISVNNotificationCallback;

import ru.arsysop.svn.connector.internal.adapt.jhlsv.ClientNotifyInformationAdapter;

public final class AdaptClientNotifyCallback implements org.apache.subversion.javahl.callback.ClientNotifyCallback {

	private final ISVNNotificationCallback callback;

	public AdaptClientNotifyCallback(ISVNNotificationCallback notify) {
		callback = Objects.requireNonNull(notify);
	}

	@Override
	public void onNotify(org.apache.subversion.javahl.ClientNotifyInformation info) {
		callback.notify(new ClientNotifyInformationAdapter(info).adapt());
	}
}