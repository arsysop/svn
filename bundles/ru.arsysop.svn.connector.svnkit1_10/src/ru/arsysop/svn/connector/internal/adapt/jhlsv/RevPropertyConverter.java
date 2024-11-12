/*
 * Copyright (c) ArSysOp 2020-2025
 * 
 * ArSysOp and its affiliates make no warranty of any kind
 * with regard to this material.
 * 
 * ArSysOp expressly disclaims all warranties as to the material, express,
 * and implied, including but not limited to the implied warranties of
 * merchantability, fitness for a particular purpose and non-infringement of third
 * party rights.
 * 
 * In no event shall ArSysOp be liable to you or any other person for any damages,
 * including, without limitation, any direct, indirect, incidental or consequential
 * damages, expenses, lost profits, lost data or other damages arising out of the use,
 * misuse or inability to use the material and any derived software, even if ArSysOp,
 * its affiliate or an authorized dealer has been advised of the possibility of such damages.
 *
 */

package ru.arsysop.svn.connector.internal.adapt.jhlsv;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TimeZone;

import org.eclipse.team.svn.core.connector.SVNProperty;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class RevPropertyConverter extends SvnNullableConstructor<Map<String, byte[]>, Map<String, Object>> {

	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS z"); //$NON-NLS-1$

	public RevPropertyConverter(Map<String, byte[]> source) {
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
