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

import java.util.Map;

import org.apache.subversion.javahl.callback.ProplistCallback;
import org.eclipse.team.svn.core.connector.ISVNPropertyCallback;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeConstructor;

public final class PropertyCallbackAdapter extends SvnTypeConstructor<ISVNPropertyCallback, ProplistCallback> {

	public PropertyCallbackAdapter(ISVNPropertyCallback source) {
		super(source);
	}

	@Override
	public ProplistCallback adapt() {
		return new ProplistCallback() {

			@Override
			public void singlePath(String path, Map<String, byte[]> properties) {
				source.next(new PropertyCallbackPair(path, properties).get(), new ISVNPropertyCallback.Pair[0]);
			}

		};
	}

}
