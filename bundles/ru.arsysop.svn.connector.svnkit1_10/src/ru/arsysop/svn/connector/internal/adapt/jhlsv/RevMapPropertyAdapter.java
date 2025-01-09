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

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TimeZone;

import org.eclipse.team.svn.core.connector.SVNProperty;

import ru.arsysop.svn.connector.internal.adapt.RepositoryDateFormat;
import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class RevMapPropertyAdapter extends SvnNullableConstructor<Map<String, byte[]>, Map<String, Object>> {

	private final DateFormat formatter = new RepositoryDateFormat().get();

	public RevMapPropertyAdapter(Map<String, byte[]> source) {
		super(source);
	}

	@Override
	protected Map<String, Object> adapt(Map<String, byte[]> source) {
		Map<String, Object> result = new HashMap<>();
		for (Entry<String, byte[]> entry : source.entrySet()) {
			if (entry.getKey().equals(SVNProperty.BuiltIn.REV_DATE)) {
				String raw = new String(entry.getValue());
				Optional<Date> parsed = parseDate(raw);
				if (parsed.isPresent()) {
					result.put(SVNProperty.BuiltIn.REV_DATE, parsed.get());
				} else {
					//FIXME: AF: log?
				}
			} else {
				result.put(entry.getKey(), new String(entry.getValue(), StandardCharsets.UTF_8));
			}
		}
		return result;
	}

	private Optional<Date> parseDate(String raw) {
		if (raw.length() != 27 || raw.charAt(26) != 'Z') {
			return Optional.empty();
		}
		try {
			Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC")); //$NON-NLS-1$
			date.setTime(formatter.parse(raw.substring(0, 23) + " UTC")); //$NON-NLS-1$
			return Optional.of(date.getTime());
		} catch (ParseException | NumberFormatException e) {
			// uninteresting in this context
			return Optional.empty();
		}
	}

}
