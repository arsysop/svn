/*
 * Copyright (c) ArSysOp 2020-2025
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

import java.util.Optional;
import java.util.Set;

final class CommitMessage implements org.apache.subversion.javahl.callback.CommitMessageCallback {

	private final String message;

	CommitMessage(String message) {
		this.message = Optional.ofNullable(message).orElse(""); //$NON-NLS-1$
	}

	@Override
	public String getLogMessage(Set<org.apache.subversion.javahl.CommitItem> elementsToBeCommitted) {
		return message;
	}

}
