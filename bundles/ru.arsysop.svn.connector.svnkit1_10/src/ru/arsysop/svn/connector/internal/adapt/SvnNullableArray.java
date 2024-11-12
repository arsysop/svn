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

package ru.arsysop.svn.connector.internal.adapt;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

public final class SvnNullableArray<S, T> extends SvnNullableConstructor<S[], T[]> {

	private final IntFunction<T[]> array;
	private final Function<S, T> adapter;

	public SvnNullableArray(S[] source, IntFunction<T[]> array, Function<S, T> adapter) {
		super(source);
		this.array = Objects.requireNonNull(array);
		this.adapter = Objects.requireNonNull(adapter);
	}

	@Override
	protected T[] adapt(S[] source) {
		return Arrays.stream(source)//
				.map(adapter)//
				.toArray(array);
	}

}
