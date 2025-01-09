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

package ru.arsysop.svn.connector.internal.svnkit1_10;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;

import org.eclipse.team.svn.core.connector.ISVNCredentialsPrompt;
import org.eclipse.team.svn.core.connector.ssl.SSLServerCertificateFailures;
import org.eclipse.team.svn.core.connector.ssl.SSLServerCertificateInfo;
import org.eclipse.team.svn.core.utility.SVNUtility;
import org.tmatesoft.svn.core.javahl17.UserPasswordSSHCallback;
import org.tmatesoft.svn.core.javahl17.UserPasswordSSLCallback;

final class CredentialsCallback implements UserPasswordSSHCallback, UserPasswordSSLCallback {

	private final ISVNCredentialsPrompt prompt;

	CredentialsCallback(ISVNCredentialsPrompt prompt) {
		this.prompt = Objects.requireNonNull(prompt);
	}

	@Deprecated
	@Override
	public String askQuestion(String arg0, String arg1, boolean arg2) {
		// ignore
		return null;
	}

	@Deprecated
	@Override
	public String askQuestion(String arg0, String arg1, boolean arg2, boolean arg3) {
		// ignore
		return null;
	}

	@Deprecated
	@Override
	public int askTrustSSLServer(String message, boolean permanently) {
		return prompt.askTrustSSLServer(//
				null, //
				new SSLServerCertificateFailures(SSLServerCertificateFailures.OTHER), //
				certificateInfo(message), //
				permanently//
				).id;
	}

	private SSLServerCertificateInfo certificateInfo(String message) {
		try {
			return SVNUtility.decodeCertificateData(SVNUtility.splitCertificateString(message));
		} catch (ParseException e) {
			return new SSLServerCertificateInfo("", //$NON-NLS-1$
					"", //$NON-NLS-1$
					0l, //
					0l, //
					new byte[0], //
					Arrays.asList(""), //$NON-NLS-1$
					null);
		}
	}

	@Deprecated
	@Override
	public boolean askYesNo(String arg0, String arg1, boolean arg2) {
		// ignore
		return false;
	}

	@Deprecated
	@Override
	public String getPassword() {
		return prompt.getPassword();
	}

	@Deprecated
	@Override
	public String getUsername() {
		return prompt.getUsername();
	}

	@Deprecated
	@Override
	public boolean prompt(String realm, String username) {
		return prompt.prompt(null, realm);
	}

	@Deprecated
	@Override
	public boolean prompt(String realm, String username, boolean save) {
		return prompt.prompt(null, realm);
	}

	@Deprecated
	@Override
	public boolean userAllowedSave() {
		// ignore
		return false;
	}

	@Override
	public boolean promptSSL(String realm, boolean save) {
		return prompt.promptSSL(null, realm);
	}

	@Override
	public String getSSLClientCertPath() {
		return prompt.getSSLClientCertPath();
	}

	@Override
	public String getSSLClientCertPassword() {
		return prompt.getSSLClientCertPassword();
	}

	@Override
	public boolean promptSSH(String realm, String username, int port, boolean save) {
		return prompt.promptSSH(null, realm);
	}

	@Override
	public String getSSHPrivateKeyPath() {
		return prompt.getSSHPrivateKeyPath();
	}

	@Override
	public String getSSHPrivateKeyPassphrase() {
		return prompt.getSSHPrivateKeyPassphrase();
	}

	@Override
	public int getSSHPort() {
		return prompt.getSSHPort();
	}

}
