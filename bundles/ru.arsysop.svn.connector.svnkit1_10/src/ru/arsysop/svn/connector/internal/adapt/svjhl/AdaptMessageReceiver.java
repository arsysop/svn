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

import org.apache.subversion.javahl.ISVNRepos.MessageReceiver;
import org.eclipse.team.svn.core.connector.ISVNRepositoryMessageCallback;

public final class AdaptMessageReceiver implements MessageReceiver {

	private final ISVNRepositoryMessageCallback callback;

	public AdaptMessageReceiver(ISVNRepositoryMessageCallback notify) {
		callback = Objects.requireNonNull(notify);
	}

	@Override
	public void receiveMessageLine(String message) {
		callback.nextMessage(message);

	}
}