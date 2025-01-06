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

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class SvnTypeMap<S, T> implements SvnTypeAdapter<S, T> {

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

	protected abstract T defaults();

}
