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

import java.util.ArrayList;
import java.util.List;

import org.apache.subversion.javahl.types.DiffOptions;
import org.eclipse.team.svn.core.connector.ISVNConnector.Options;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class DiffOptionsAdapter extends SvnNullableConstructor<Long, DiffOptions> {

	public DiffOptionsAdapter(Long options) {
		super(options != Options.NONE ? options : null);
	}

	@Override
	protected DiffOptions adapt(Long source) {
		List<DiffOptions.Flag> flags = new ArrayList<>();
		if ((source & org.eclipse.team.svn.core.connector.ISVNConnector.DiffOptions.IGNORE_WHITESPACE) != 0) {
			flags.add(org.apache.subversion.javahl.types.DiffOptions.Flag.IgnoreWhitespace);
		}
		if ((source & org.eclipse.team.svn.core.connector.ISVNConnector.DiffOptions.IGNORE_SPACE_CHANGE) != 0) {
			flags.add(org.apache.subversion.javahl.types.DiffOptions.Flag.IgnoreSpaceChange);
		}
		if ((source & org.eclipse.team.svn.core.connector.ISVNConnector.DiffOptions.IGNORE_EOL_STYLE) != 0) {
			flags.add(org.apache.subversion.javahl.types.DiffOptions.Flag.IgnoreEOLStyle);
		}
		if ((source & org.eclipse.team.svn.core.connector.ISVNConnector.DiffOptions.SHOW_FUNCTION) != 0) {
			flags.add(org.apache.subversion.javahl.types.DiffOptions.Flag.ShowFunction);
		}
		if ((source & org.eclipse.team.svn.core.connector.ISVNConnector.DiffOptions.GIT_FORMAT) != 0) {
			flags.add(org.apache.subversion.javahl.types.DiffOptions.Flag.GitFormat);
		}
		return new DiffOptions(flags.toArray(DiffOptions.Flag[]::new));
	}

}
