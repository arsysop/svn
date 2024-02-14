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

package ru.arsysop.svn.connector.internal.svnkit1_10;

import org.eclipse.team.svn.core.connector.SVNDepth;

final class DepthJavahlSubversive {

	private final SVNDepth depth;

	DepthJavahlSubversive(SVNDepth depth) {
		this.depth = depth;
	}

	org.apache.subversion.javahl.types.Depth adapt() {
		switch (depth) {
			case EXCLUDE:
				return org.apache.subversion.javahl.types.Depth.exclude;
			case EMPTY:
				return org.apache.subversion.javahl.types.Depth.empty;
			case FILES:
				return org.apache.subversion.javahl.types.Depth.files;
			case IMMEDIATES:
				return org.apache.subversion.javahl.types.Depth.immediates;
			case INFINITY:
				return org.apache.subversion.javahl.types.Depth.infinity;
			default:
		}
		return org.apache.subversion.javahl.types.Depth.unknown;
	}

}
