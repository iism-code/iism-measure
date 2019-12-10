package org.iism.measure;

import java.util.HashMap;
import java.util.HashSet;

public class TimeMeasure implements AutoCloseable {

	static HashMap<String, TimeMeasureData> timeMeasureMap = new HashMap<String, TimeMeasureData>();

	private TimeMeasure(TimeMeasureData timeMeasureData) {
		this.timeMeasureData = timeMeasureData;
	}

	TimeMeasureData timeMeasureData;

	long startTime;

	public synchronized void startMeasure() {
		startTime = System.currentTimeMillis();
	}

	public synchronized void endMeasure() {
		long delta = System.currentTimeMillis() - startTime;
		timeMeasureData.addMeasurement(delta);
	}


	synchronized public static TimeMeasure getOrCreateMeasure(String measureName) {

		if (timeMeasureMap.containsKey(measureName)) {

			TimeMeasure measure = new TimeMeasure(timeMeasureMap.get(measureName));
			measure.startMeasure();
			return measure;
		}
		TimeMeasureData timeMeasureData = new TimeMeasureData(measureName);
		TimeMeasure measure = new TimeMeasure(timeMeasureData);
		timeMeasureMap.put(measureName, timeMeasureData);
		measure.startMeasure();
		return measure;
	}


	@Override
	public void close() {
		endMeasure();
	}

	public void addMeasure(long delta) {
		timeMeasureData.addMeasurement(delta);
	}
	
	synchronized public static HashSet<RollupData> snapshotTimeMeasureData(HashSet<RollupData> exportData) {
		for (TimeMeasureData timeMeasureData : timeMeasureMap.values()) {
			for (RollupData data : timeMeasureData.getExportData()) {
				exportData.add(data);
			}
		}
		return exportData;
	}	
}
