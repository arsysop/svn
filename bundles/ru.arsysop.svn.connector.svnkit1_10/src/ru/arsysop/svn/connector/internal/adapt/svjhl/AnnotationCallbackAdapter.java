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

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.apache.subversion.javahl.ClientException;
import org.apache.subversion.javahl.callback.BlameCallback;
import org.eclipse.team.svn.core.connector.ISVNAnnotationCallback;
import org.eclipse.team.svn.core.connector.SVNAnnotationData;
import org.eclipse.team.svn.core.connector.SVNProperty;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;
import ru.arsysop.svn.connector.internal.adapt.jhlsv.RevPropertyConverter;

public final class AnnotationCallbackAdapter extends SvnNullableConstructor<ISVNAnnotationCallback, BlameCallback> {

	public AnnotationCallbackAdapter(ISVNAnnotationCallback source) {
		super(source);
	}

	@Override
	protected BlameCallback adapt(ISVNAnnotationCallback source) {
		return new org.apache.subversion.javahl.callback.BlameCallback() {

			public void singleLine(long number, long revision, Map<String, byte[]> irp, long mrevision,
					Map<String, byte[]> imp, String mpath, String line, boolean localChange) throws ClientException {
				Map<String, Object> orp = new RevPropertyConverter(irp).adapt();
				Map<String, Object> omp = new RevPropertyConverter(imp).adapt();
				source.annotate(line, new SVNAnnotationData(//
						number, //
						localChange, //
						revision, //
						time(orp), //
						author(orp), //
						mrevision, //
						time(omp), //
						author(omp), //
						mpath));
			}

			private String author(Map<String, Object> props) {
				return Optional.ofNullable(props)
						.map(m -> m.get(SVNProperty.BuiltIn.REV_AUTHOR))
						.map(String.class::cast)
						.orElse(null);
			}

			private long time(Map<String, Object> props) {
				return Optional.ofNullable(props)
						.map(m -> m.get(SVNProperty.BuiltIn.REV_DATE))
						.map(Date.class::cast)
						.map(Date::getTime)
						.orElse((long) 0);
			}

		};
	}

}
