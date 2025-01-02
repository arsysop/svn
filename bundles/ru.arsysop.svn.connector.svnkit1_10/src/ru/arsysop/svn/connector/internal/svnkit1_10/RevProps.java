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
