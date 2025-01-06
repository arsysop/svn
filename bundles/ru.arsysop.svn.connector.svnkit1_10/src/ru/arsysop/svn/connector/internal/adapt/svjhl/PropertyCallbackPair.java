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

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.team.svn.core.connector.ISVNPropertyCallback;
import org.eclipse.team.svn.core.connector.ISVNPropertyCallback.Pair;
import org.eclipse.team.svn.core.connector.SVNProperty;

final class PropertyCallbackPair {

	private final String path;
	private final Map<String, byte[]> properties;

	PropertyCallbackPair(String path, Map<String, byte[]> properties) {
		this.path = Objects.requireNonNull(path);
		this.properties = Objects.requireNonNull(properties);
	}

	Pair get() {
		return new ISVNPropertyCallback.Pair(//
				path, //
				properties.entrySet()
				.stream()
				.map(e -> new SVNProperty(e.getKey(), e.getValue()))
				.collect(Collectors.toList())
				.toArray(new SVNProperty[0]));
	}

}
