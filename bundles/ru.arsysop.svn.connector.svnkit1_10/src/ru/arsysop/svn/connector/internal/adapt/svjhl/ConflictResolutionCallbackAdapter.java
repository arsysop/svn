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

import org.apache.subversion.javahl.ClientException;
import org.apache.subversion.javahl.ConflictDescriptor;
import org.apache.subversion.javahl.ConflictResult;
import org.apache.subversion.javahl.SubversionException;
import org.apache.subversion.javahl.callback.ConflictResolverCallback;
import org.eclipse.team.svn.core.connector.ISVNConflictResolutionCallback;
import org.eclipse.team.svn.core.connector.SVNConnectorException;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;
import ru.arsysop.svn.connector.internal.adapt.jhlsv.ConflictDescriptorNullableAdapter;

public final class ConflictResolutionCallbackAdapter
extends SvnNullableConstructor<ISVNConflictResolutionCallback, ConflictResolverCallback> {

	public ConflictResolutionCallbackAdapter(ISVNConflictResolutionCallback source) {
		super(source);
	}

	@Override
	protected ConflictResolverCallback adapt(ISVNConflictResolutionCallback source) {
		return new ConflictResolverCallback() {

			public ConflictResult resolve(ConflictDescriptor descrip) throws SubversionException {
				try {
					return new ConflictResolutionAdapter(
							source.resolve(new ConflictDescriptorNullableAdapter(descrip).adapt())).adapt();
				} catch (SVNConnectorException ex) {
					throw ClientException.fromException(ex);
				}
			}

		};
	}

}
