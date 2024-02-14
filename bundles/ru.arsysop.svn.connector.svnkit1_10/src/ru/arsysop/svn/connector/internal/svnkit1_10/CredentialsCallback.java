/*
 * Copyright (c) ArSysOp 2020-2024
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
