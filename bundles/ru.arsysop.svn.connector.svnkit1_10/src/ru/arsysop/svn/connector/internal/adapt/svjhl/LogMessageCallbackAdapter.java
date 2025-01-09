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

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.subversion.javahl.callback.LogMessageCallback;
import org.apache.subversion.javahl.types.ChangePath;
import org.eclipse.team.svn.core.connector.ISVNLogEntryCallback;
import org.eclipse.team.svn.core.connector.SVNLogEntry;
import org.eclipse.team.svn.core.connector.SVNLogPath;
import org.eclipse.team.svn.core.connector.SVNProperty;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableArray;
import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;
import ru.arsysop.svn.connector.internal.adapt.jhlsv.ChangePathAdapter;
import ru.arsysop.svn.connector.internal.adapt.jhlsv.RevMapPropertyAdapter;

public final class LogMessageCallbackAdapter extends SvnNullableConstructor<ISVNLogEntryCallback, LogMessageCallback> {

	public LogMessageCallbackAdapter(ISVNLogEntryCallback source) {
		super(source);
	}

	@Override
	protected LogMessageCallback adapt(ISVNLogEntryCallback source) {
		return new LogMessageCallback() {

			@Override
			public void singleMessage(Set<ChangePath> paths, long revision,
					Map<String, byte[]> revprops, boolean hasChildren) {
				SVNLogEntry entry = convert(//
						paths == null ? null : paths.toArray(ChangePath[]::new), //
								revision, //
								revprops, //
								hasChildren//
						);
				source.next(entry);
			}

			private SVNLogEntry convert(ChangePath[] paths, long revision,
					Map<String, byte[]> revprops, boolean hasChildren) {
				if (revprops == null) {
					// no access rights
					return new SVNLogEntry(//
							revision, //
							0l, //
							null, //
							null, //
							new SvnNullableArray<>(//
									paths, //
									SVNLogPath[]::new, ///
									p -> new ChangePathAdapter(p).adapt()).adapt(), //
							hasChildren);
				}
				Map<String, Object> converted = new RevMapPropertyAdapter(revprops).adapt();
				Date date = converted == null ? null : (Date) converted.get(SVNProperty.BuiltIn.REV_DATE);
				return new SVNLogEntry(//
						revision, //
						date == null ? 0 : date.getTime(), //
								converted == null ? null : (String) converted.get(SVNProperty.BuiltIn.REV_AUTHOR), //
										converted == null ? null : (String) converted.get(SVNProperty.BuiltIn.REV_LOG), //
												new SvnNullableArray<>(paths, SVNLogPath[]::new, p -> new ChangePathAdapter(p).adapt()).adapt(), //
												hasChildren);
			}

		};
	}

}
