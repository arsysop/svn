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

import org.apache.subversion.javahl.types.Depth;
import org.eclipse.team.svn.core.connector.SVNDepth;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class DepthAdapter extends SvnNullableConstructor<Depth, SVNDepth> {

	public DepthAdapter(Depth depth) {
		super(depth);
	}

	@Override
	protected SVNDepth adapt(Depth source) {
		if (source == org.apache.subversion.javahl.types.Depth.exclude) {
			return SVNDepth.EXCLUDE;
		}
		if (source == org.apache.subversion.javahl.types.Depth.empty) {
			return SVNDepth.EMPTY;
		}
		if (source == org.apache.subversion.javahl.types.Depth.files) {
			return SVNDepth.FILES;
		}
		if (source == org.apache.subversion.javahl.types.Depth.immediates) {
			return SVNDepth.IMMEDIATES;
		}
		if (source == org.apache.subversion.javahl.types.Depth.infinity) {
			return SVNDepth.INFINITY;
		}
		return SVNDepth.UNKNOWN;
	}

}
