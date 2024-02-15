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

package ru.arsysop.svn.connector.internal.adapt;

import java.util.Optional;

/**
 * 
 * They are not kidding, and really transfer <code>null</code>> values here and there
 */
public abstract class SvnNullableConstructor<S, T> implements SvnNullableAdapter<S, T> {

	private final Optional<S> optional;

	public SvnNullableConstructor(S source) {
		optional = Optional.ofNullable(source);
	}

	@Override
	public final T adapt() {
		return optional.map(this::adapt).orElse(null);
	}

	protected abstract T adapt(S source);

}
