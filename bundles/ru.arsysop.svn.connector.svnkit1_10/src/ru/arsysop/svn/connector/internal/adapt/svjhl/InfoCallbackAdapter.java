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

import org.apache.subversion.javahl.callback.InfoCallback;
import org.eclipse.team.svn.core.connector.ISVNEntryInfoCallback;

import ru.arsysop.svn.connector.internal.adapt.SvnTypeConstructor;
import ru.arsysop.svn.connector.internal.adapt.jhlsv.InfoNullableAdapter;

public final class InfoCallbackAdapter extends SvnTypeConstructor<ISVNEntryInfoCallback, InfoCallback> {

	public InfoCallbackAdapter(ISVNEntryInfoCallback source) {
		super(source);
	}

	@Override
	public InfoCallback adapt() {
		return new org.apache.subversion.javahl.callback.InfoCallback() {

			public void singleInfo(org.apache.subversion.javahl.types.Info info) {
				//FIXME: AF: should we filter out null value?
				source.next(new InfoNullableAdapter(info).adapt());
			}

		};
	}

}
