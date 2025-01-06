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

import java.util.ArrayList;
import java.util.List;

final class Watchdog extends Thread {

	private final long timeout = 3000L;
	private final List<ProgressCallback> callbacks = new ArrayList<>();

	public Watchdog(String name) {
		super(name);
	}

	void add(ProgressCallback callback) {
		synchronized (callbacks) {
			callbacks.add(callback);
			callbacks.notify();
		}
	}

	void remove(ProgressCallback callback) {
		synchronized (callbacks) {
			callbacks.remove(callback);
		}
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			watch();
			try {
				synchronized (callbacks) {
					if (callbacks.size() == 0) {
						callbacks.wait();
					} else {
						callbacks.wait(100);
					}
				}
			} catch (InterruptedException ex) {
				break;
			}
		}
	}

	private void watch() {
		List<ProgressCallback> known = new ArrayList<>();
		synchronized (callbacks) {
			known.addAll(callbacks);
		}
		known.forEach(this::watch);
	}

	private void watch(ProgressCallback callback) {
		if (callback.monitor.isActivityCancelled()) {
			callback.cancel();
			return;
		}
		callback.cancellation()//
		.filter(l -> l < System.currentTimeMillis() - timeout)//
		.ifPresent(l -> callback.interrupt());
	}

}
