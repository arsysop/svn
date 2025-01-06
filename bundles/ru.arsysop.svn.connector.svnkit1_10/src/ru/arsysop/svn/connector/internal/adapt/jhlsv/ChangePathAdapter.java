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

package ru.arsysop.svn.connector.internal.adapt.jhlsv;

import org.apache.subversion.javahl.types.ChangePath;
import org.eclipse.team.svn.core.connector.SVNLogPath;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class ChangePathAdapter extends SvnNullableConstructor<ChangePath, SVNLogPath> {

	public ChangePathAdapter(ChangePath source) {
		super(source);
	}

	@Override
	protected SVNLogPath adapt(ChangePath source) {
		return new SVNLogPath(//
				source.getPath(), //
				changeType(source.getAction()), //
				source.getCopySrcPath(), //
				source.getCopySrcRevision(), //
				new TristateAdapter(source.getTextMods()).adapt(), //
				new TristateAdapter(source.getPropMods()).adapt()//
				);
	}

	private SVNLogPath.ChangeType changeType(org.apache.subversion.javahl.types.ChangePath.Action tAction) {
		switch (tAction) {
			case delete:
				return SVNLogPath.ChangeType.DELETED;
			case modify:
				return SVNLogPath.ChangeType.MODIFIED;
			case replace:
				return SVNLogPath.ChangeType.REPLACED;
			default:
				return SVNLogPath.ChangeType.ADDED;
		}
	}

}
