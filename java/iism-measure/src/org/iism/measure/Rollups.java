package org.iism.measure;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Rollups {
	ArrayList<Rollup> rollups = new ArrayList<Rollup>();
	int rollupsToKeep;
	int intervalSeconds;
	Rollup currentRollup;
	int currentRollupIndex = 0;

	public Rollups(int rollupsToKeep, int intervalSeconds) {
		this.rollupsToKeep = rollupsToKeep;
		this.intervalSeconds = intervalSeconds;
		for (int i = 0; i < rollupsToKeep; i++) {
			rollups.add(new Rollup(System.currentTimeMillis(), System.currentTimeMillis() + intervalSeconds * 1000));
		}
		currentRollup = rollups.get(currentRollupIndex);
		currentRollup.active = true;
	}

	public void addMeasure(long value) {
		rotateRollupIfNeeded();
		currentRollup.addMeasure(value);
	}

	private void rotateRollupIfNeeded() {
		if (System.currentTimeMillis() > currentRollup.endTime) {
			currentRollupIndex++;
			if (currentRollupIndex >= rollups.size()) {
				currentRollupIndex = 0;
			}
			currentRollup = rollups.get(currentRollupIndex);
			currentRollup.startTime = System.currentTimeMillis();
			currentRollup.endTime = System.currentTimeMillis() + intervalSeconds * 1000;
			currentRollup.count = 0;
			currentRollup.max = Long.MIN_VALUE;
			currentRollup.min = Long.MAX_VALUE;
			currentRollup.total = 0;			
			currentRollup.active = true;
		}
	}
	
	private static String getIntervalName(int intervalSeconds) {
		switch (intervalSeconds) {
		case 60:
			return "Minute";
		case 60 * 60:
			return "Hour";
		case 60 * 60 * 24:
			return "Day";
		case 60 * 15:
			return "15 Minute";
		default:
			return intervalSeconds + " second";
		}

	}
	
	private RollupData exportRollup(Rollup rollup) {
		RollupData exportData = new RollupData();
		exportData.average = (rollup.count > 0 ? rollup.total / rollup.count : 0);
		exportData.intervalSeconds = intervalSeconds;
		exportData.intervalName = getIntervalName(intervalSeconds);
		exportData.max = rollup.max;
		exportData.min = rollup.min;

		Date roundedStartTime = new Date();
		roundedStartTime
				.setTime(Math.round((double) rollup.startTime / (intervalSeconds * 1000)) * (intervalSeconds * 1000));

		exportData.startTime = roundedStartTime;
		Date roundedEndTime = new Date();
		roundedEndTime
				.setTime(Math.round((double) rollup.endTime / (intervalSeconds * 1000)) * (intervalSeconds * 1000));
		exportData.endTime = roundedEndTime;
		exportData.count = rollup.count;
		return exportData;
	}	
	
	public Set<RollupData> getExportData() {
		HashSet<RollupData> totals = new HashSet<RollupData>();

		for (Rollup rollup : rollups) {
			if (rollup.active) {
				if (rollup.startTime > System.currentTimeMillis() - rollupsToKeep * intervalSeconds * 1000) {
					RollupData exportData = exportRollup(rollup);
					totals.add(exportData);
				}
			}
		}
		return totals;
	}	
}

