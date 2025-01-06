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

import java.util.Date;
import java.util.Optional;

import org.apache.subversion.javahl.types.Lock;
import org.eclipse.team.svn.core.connector.SVNLock;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class LockNullableAdapter extends SvnNullableConstructor<Lock, SVNLock> {

	public LockNullableAdapter(Lock source) {
		super(source);
	}

	@Override
	protected SVNLock adapt(Lock source) {
		return new SVNLock(
				source.getOwner(), //
				source.getPath(), //
				source.getToken(), //
				source.getComment(), //
				Optional.ofNullable(source.getCreationDate()).map(Date::getTime).orElse(0L), //
				Optional.ofNullable(source.getExpirationDate()).map(Date::getTime).orElse(0L)//
				);
	}

}
