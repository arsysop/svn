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

package ru.arsysop.svn.connector.internal.svnkit1_10;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.eclipse.team.svn.core.connector.SVNProperty;

import ru.arsysop.svn.connector.internal.adapt.RepositoryDateFormat;
import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class RevProps extends SvnNullableConstructor<Map<String, Object>, Map<String, String>> {

	private final DateFormat formatter = new RepositoryDateFormat().get();

	public RevProps(Map<String, Object> source) {
		super(source);
	}

	@Override
	protected Map<String, String> adapt(Map<String, Object> source) {
		Map<String, String> result = new HashMap<>();
		for (Entry<String, Object> entry : source.entrySet()) {
			if (SVNProperty.BuiltIn.REV_DATE.equals(entry.getKey()) && entry.getValue() instanceof Date) {
				Date date = (Date) entry.getValue();
				result.put(SVNProperty.BuiltIn.REV_DATE, formatter.format(date));
			} else {
				result.put(entry.getKey(), Optional.ofNullable(entry.getValue()).map(String::valueOf).orElse(null));
			}
		}
		return result;
	}

}
