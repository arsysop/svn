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

package ru.arsysop.svn.connector.internal.svnkit1_10;

import org.eclipse.core.runtime.Platform;
import org.eclipse.team.svn.core.connector.ISVNConnector;
import org.eclipse.team.svn.core.connector.ISVNManager;
import org.eclipse.team.svn.core.extension.factory.ISVNConnectorFactory;
import org.tmatesoft.svn.core.javahl17.SVNClientImpl;
import org.tmatesoft.svn.util.Version;

public final class SvnKit1_10ConnectorFactory implements ISVNConnectorFactory {

	public ISVNConnector createConnector() {
		return new SvnKit1_10Connector();
	}

	public ISVNManager createManager() {
		return new SvnKit1_10Manager();
	}

	public String getName() {
		return String.format("%1$s %2$s %3$s (SVN %4$s compatible, all platforms)", //$NON-NLS-1$
				"SVNKit", //$NON-NLS-1$
				Version.getShortVersionString(), //
				Version.getRevisionString(), //
				Version.getSVNVersion()); //
	}

	public String getId() {
		return "org.eclipse.team.svn.connector.svnkit18"; //$NON-NLS-1$
	}

	public String getClientVersion() {
		org.apache.subversion.javahl.types.Version version = SVNClientImpl.newInstance().getVersion(); // TODO: nurse this client instance
		return String.format("%s.%s.%s", version.getMajor(), version.getMinor(), version.getPatch()); //$NON-NLS-1$
	}

	public String getVersion() {
		return Platform.getBundle("org.tmatesoft.svnkit.svnkit") //$NON-NLS-1$
				.getHeaders()
				.get(org.osgi.framework.Constants.BUNDLE_VERSION);
	}

	public String getCompatibilityVersion() {
		return "4.0.0.I20160427-1700"; //$NON-NLS-1$
	}

	public int getSupportedFeatures() {
		return OptionalFeatures.SSH_SETTINGS //
				| OptionalFeatures.PROXY_SETTINGS //
				| OptionalFeatures.ATOMIC_X_COMMIT //
				| OptionalFeatures.CREATE_REPOSITORY_FSFS;
	}

	public int getSVNAPIVersion() {
		return APICompatibility.SVNAPI_1_8_x;
	}

	@Override
	public String toString() {
		return getId();
	}

}
