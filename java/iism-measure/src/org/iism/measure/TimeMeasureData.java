package org.iism.measure;

import java.util.ArrayList;

public class TimeMeasureData {
	public TimeMeasureData(String measureName) {
		this.measureName = measureName;
		intervals.add(new Rollups(60, 60));
		intervals.add(new Rollups(24, 3600));
		intervals.add(new Rollups(14, 86400));

	}

	String measureName;

	ArrayList<Rollups> intervals = new ArrayList<Rollups>();

	synchronized public void addMeasurement(long delta) {
		for (Rollups rollups : intervals) {
			rollups.addMeasure(delta);
		}
	}
	
	synchronized ArrayList<RollupData> getExportData() {
		ArrayList<RollupData> exportDataItems = new ArrayList<RollupData>();
		for (Rollups rollups : intervals) {
			for (RollupData exportData : rollups.getExportData()) {
				exportData.measureName = this.measureName;
				exportDataItems.add(exportData);
			}
		}
		return exportDataItems;
	}	
}

