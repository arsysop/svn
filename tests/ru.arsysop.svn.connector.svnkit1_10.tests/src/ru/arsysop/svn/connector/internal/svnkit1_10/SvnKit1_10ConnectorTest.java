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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.team.svn.core.connector.ISVNCallListener;
import org.eclipse.team.svn.core.connector.SVNConnectorException;
import org.eclipse.team.svn.core.connector.SVNDepth;
import org.eclipse.team.svn.core.connector.SVNEntryRevisionReference;
import org.eclipse.team.svn.core.operation.SVNNullProgressMonitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


class SvnKit1_10ConnectorTest {

	private final SvnKit1_10Connector connector = new SvnKit1_10Connector(
			SvnKit1_10ConnectorTest.class.getName() + ".connector"); //$NON-NLS-1$

	@AfterEach
	void tearDown() {
		connector.dispose();
	}

	@Test
	void getSetConfigDirectory() throws SVNConnectorException {
		SVNCallListener listener = new SVNCallListener();
		connector.addCallListener(listener);

		connector.setConfigDirectory(".configdir"); //$NON-NLS-1$
		assertEquals(".configdir", connector.getConfigDirectory()); //$NON-NLS-1$

		connector.setConfigDirectory(null);
		assertEquals(null, connector.getConfigDirectory());

		assertEquals(4, listener.asked.size());
		assertEquals(4, listener.succeeded.size());
		assertEquals(0, listener.failed.size());

		SVNCallListener.Asked asked0 = listener.asked.get(0);
		Map<String, Object> parameters0 = Map.of("configDir", ".configdir"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(ISVNCallListener.SET_CONFIG_DIRECTORY, asked0.methodName);
		assertEquals(parameters0, asked0.parameters);

		SVNCallListener.Asked asked1 = listener.asked.get(1);
		assertEquals(ISVNCallListener.GET_CONFIG_DIRECTORY, asked1.methodName);
		assertEquals(Map.of(), asked1.parameters);

		SVNCallListener.Asked asked2 = listener.asked.get(2);
		Map<String, Object> parameters2 = new HashMap<>();
		parameters2.put("configDir", null); //$NON-NLS-1$
		assertEquals(ISVNCallListener.SET_CONFIG_DIRECTORY, asked2.methodName);
		assertEquals(parameters2, asked2.parameters);

		SVNCallListener.Asked asked3 = listener.asked.get(3);
		assertEquals(ISVNCallListener.GET_CONFIG_DIRECTORY, asked3.methodName);
		assertEquals(Map.of(), asked3.parameters);

		SVNCallListener.Succeeded succeeded0 = listener.succeeded.get(0);
		assertEquals(ISVNCallListener.SET_CONFIG_DIRECTORY, succeeded0.methodName);
		assertEquals(parameters0, succeeded0.parameters);
		assertEquals(null, succeeded0.returnValue);

		SVNCallListener.Succeeded succeeded1 = listener.succeeded.get(1);
		assertEquals(ISVNCallListener.GET_CONFIG_DIRECTORY, succeeded1.methodName);
		assertEquals(Map.of(), succeeded1.parameters);
		assertEquals(".configdir", succeeded1.returnValue); //$NON-NLS-1$

		SVNCallListener.Succeeded succeeded2 = listener.succeeded.get(2);
		assertEquals(ISVNCallListener.SET_CONFIG_DIRECTORY, succeeded2.methodName);
		assertEquals(parameters2, succeeded2.parameters);
		assertEquals(null, succeeded2.returnValue);

		SVNCallListener.Succeeded succeeded3 = listener.succeeded.get(3);
		assertEquals(ISVNCallListener.GET_CONFIG_DIRECTORY, succeeded3.methodName);
		assertEquals(Map.of(), succeeded3.parameters);
		assertEquals(null, succeeded3.returnValue);
	}

	@Test
	void setNullUsernamePassword() {
		SVNCallListener listener = new SVNCallListener();
		connector.addCallListener(listener);

		connector.setUsername(null);
		connector.setPassword(null);

		assertEquals(2, listener.asked.size());
		assertEquals(2, listener.succeeded.size());
		assertEquals(0, listener.failed.size());

		SVNCallListener.Asked asked0 = listener.asked.get(0);
		Map<String, Object> parameters0 = new HashMap<>();
		parameters0.put("username", null); //$NON-NLS-1$
		assertEquals(ISVNCallListener.SET_USERNAME, asked0.methodName);
		assertEquals(parameters0, asked0.parameters);

		SVNCallListener.Asked asked1 = listener.asked.get(1);
		Map<String, Object> parameters1 = new HashMap<>();
		parameters1.put("password", null); //$NON-NLS-1$
		assertEquals(ISVNCallListener.SET_PASSWORD, asked1.methodName);
		assertEquals(parameters1, asked1.parameters);

		SVNCallListener.Succeeded succeeded0 = listener.succeeded.get(0);
		assertEquals(ISVNCallListener.SET_USERNAME, succeeded0.methodName);
		assertEquals(parameters0, succeeded0.parameters);
		assertEquals(null, succeeded0.returnValue);

		SVNCallListener.Succeeded succeeded1 = listener.succeeded.get(1);
		assertEquals(ISVNCallListener.SET_PASSWORD, succeeded1.methodName);
		assertEquals(parameters1, succeeded1.parameters);
		assertEquals(null, succeeded1.returnValue);
	}

	@Test
	void getInfoFailure() throws SVNConnectorException {
		SVNCallListener listener = new SVNCallListener();
		connector.addCallListener(listener);

		SVNConnectorException ex = assertThrows(SVNConnectorException.class,
				() -> connector.getInfo(new SVNEntryRevisionReference("/"), //$NON-NLS-1$
						SVNDepth.INFINITY, 0, new String[0], info -> {
						}, new SVNNullProgressMonitor()));

		assertEquals(1, listener.asked.size());
		assertEquals(0, listener.succeeded.size());
		assertEquals(1, listener.failed.size());

		SVNCallListener.Asked asked0 = listener.asked.get(0);
		Set<String> parameters0KeySet = Set.of("reference", "depth", "changeLists", "options", "monitor", "cb"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		assertEquals(ISVNCallListener.GET_INFO, asked0.methodName);
		assertEquals(parameters0KeySet, asked0.parameters.keySet());

		SVNCallListener.Failed failed0 = listener.failed.get(0);
		assertEquals(ISVNCallListener.GET_INFO, failed0.methodName);
		assertEquals(asked0.parameters, failed0.parameters);
		assertEquals(ex, failed0.exception);
	}

	private static final class SVNCallListener implements ISVNCallListener {

		final List<Asked> asked = new ArrayList<>();
		final List<Succeeded> succeeded = new ArrayList<>();
		final List<Failed> failed = new ArrayList<>();

		@Override
		public void asked(String methodName, Map<String, Object> parameters) {
			asked.add(new Asked(methodName, parameters));
		}

		@Override
		public void succeeded(String methodName, Map<String, Object> parameters, Object returnValue) {
			succeeded.add(new Succeeded(methodName, parameters, returnValue));
		}

		@Override
		public void failed(String methodName, Map<String, Object> parameters, SVNConnectorException exception) {
			failed.add(new Failed(methodName, parameters, exception));
		}

		static final class Asked {

			final String methodName;
			final Map<String, Object> parameters;

			Asked(String methodName, Map<String, Object> parameters) {
				this.methodName = methodName;
				this.parameters = parameters;
			}

		}

		static final class Succeeded {

			final String methodName;
			final Map<String, Object> parameters;
			final Object returnValue;

			Succeeded(String methodName, Map<String, Object> parameters, Object returnValue) {
				this.methodName = methodName;
				this.parameters = parameters;
				this.returnValue = returnValue;
			}

		}

		static final class Failed {

			final String methodName;
			final Map<String, Object> parameters;
			final SVNConnectorException exception;

			Failed(String methodName, Map<String, Object> parameters, SVNConnectorException exception) {
				this.methodName = methodName;
				this.parameters = parameters;
				this.exception = exception;
			}

		}

	}
}
