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
