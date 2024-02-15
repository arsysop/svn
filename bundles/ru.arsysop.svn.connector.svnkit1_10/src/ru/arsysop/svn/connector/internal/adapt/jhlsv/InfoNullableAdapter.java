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

package ru.arsysop.svn.connector.internal.adapt.jhlsv;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.subversion.javahl.types.Info;
import org.eclipse.team.svn.core.connector.SVNConflictDescriptor;
import org.eclipse.team.svn.core.connector.SVNEntryInfo;

import ru.arsysop.svn.connector.internal.adapt.SvnNullableConstructor;

public final class InfoNullableAdapter extends SvnNullableConstructor<Info, SVNEntryInfo> {

	public InfoNullableAdapter(Info source) {
		super(source);
	}

	@Override
	protected SVNEntryInfo adapt(Info info) {
		org.apache.subversion.javahl.types.Info.ScheduleKind tScheduleKind = info.getSchedule();
		SVNEntryInfo.ScheduledOperation scheduleKind = SVNEntryInfo.ScheduledOperation.NORMAL;
		if (tScheduleKind == org.apache.subversion.javahl.types.Info.ScheduleKind.add) {
			scheduleKind = SVNEntryInfo.ScheduledOperation.ADD;
		} else if (tScheduleKind == org.apache.subversion.javahl.types.Info.ScheduleKind.delete) {
			scheduleKind = SVNEntryInfo.ScheduledOperation.DELETE;
		} else if (tScheduleKind == org.apache.subversion.javahl.types.Info.ScheduleKind.replace) {
			scheduleKind = SVNEntryInfo.ScheduledOperation.REPLACE;
		}
		long changeTime = info.getTextTime() == null ? 0 : info.getTextTime().getTime();
		return new SVNEntryInfo(//
				info.getPath(), //
				info.getWcroot(), //
				info.getUrl(), //
				info.getRev(), //
				new NodeKindAdapter(info.getKind()).adapt(), //
				info.getReposRootUrl(), //
				info.getReposUUID(), //
				info.getLastChangedRev(), //
				Optional.ofNullable(info.getLastChangedDate()).map(Date::getTime).orElse(0L),
				info.getLastChangedAuthor(), //
				new LockNullableAdapter(info.getLock()).adapt(), //
				info.isHasWcInfo(), //
				scheduleKind, //
				info.getCopyFromUrl(), //
				info.getCopyFromRev(), //
				changeTime, //
				changeTime, //
				new ChecksumNullableAdapter(info.getChecksum()).adapt(), //
				info.getChangelistName(), //
				info.getWorkingSize(), //
				info.getReposSize(), //
				new DepthAdapter(info.getDepth()).adapt(), conflictDescriptors(info)//
				);
	}

	private SVNConflictDescriptor[] conflictDescriptors(Info info) {
		return Optional.ofNullable(info.getConflicts())
				.orElseGet(Collections::emptySet)
				.stream()
				.map(ConflictDescriptorNullableAdapter::new)
				.collect(Collectors.toList())
				.toArray(new SVNConflictDescriptor[0]);
	}

}
