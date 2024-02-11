/*
 * Copyright (c) ArSysOp 2020-2024
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

package ru.arsysop.svn.connector.internal.adapt;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

abstract class SvnTypeMap<S, T> implements SvnTypeAdapter<S, T> {

	private final S source;
	private final Map<S, T> map;

	protected SvnTypeMap(S source) {
		this.source = Objects.requireNonNull(source);
		map = fill();
	}

	protected abstract Map<S, T> fill();

	@Override
	public final T adapt() {
		return Optional.ofNullable(map.get(source)).orElseGet(this::defaults);
	}

	abstract T defaults();

}
