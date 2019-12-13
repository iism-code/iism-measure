package org.iism.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.iism.measure.RollupData;
import org.iism.measure.TimeMeasure;
import org.junit.jupiter.api.Test;


class TestMeasure {

	@Test
	void testTry() throws InterruptedException {
		try (TimeMeasure timeMeasure = TimeMeasure.get("someImportantBusinessLogicMethod Response Time")) {
			someImportantBusinessLogicMethod();
		}
		
		verifyMeasureCreated("someImportantBusinessLogicMethod");
	}

	@Test
	void testProcedural() throws InterruptedException {
		TimeMeasure timeMeasure = TimeMeasure.get("someOtherImportantBusinessLogicMethod Response Time");
		timeMeasure.startMeasure();
		someOtherImportantBusinessLogicMethod();
		timeMeasure.endMeasure();
		verifyMeasureCreated("someOtherImportantBusinessLogicMethod");
	}
	
	@Test
	void testMultipleMeasurements() throws InterruptedException {
		int measuresToGenerate = 100;
		for( int i = 0; i < measuresToGenerate; i++) {
			TimeMeasure timeMeasure = TimeMeasure.get("MyImportantMeasure " + i);
			for(int j = 0; j < 100; j++) {
				timeMeasure.startMeasure();
				someIntermittentlySlowImportantBusinessLogicMethod();
				timeMeasure.endMeasure();
			}
		}
		
		HashSet<RollupData> rollupData = new HashSet<RollupData>();
		TimeMeasure.snapshotTimeMeasureData(rollupData);
		
		assertTrue(rollupData.size() > 0);
		
		boolean foundAverageGtZero = false;
		for(RollupData data : rollupData) {
			if (data.measureName.startsWith("MyImportantMeasure")) {
				if (data.average > 0) {
					foundAverageGtZero = true;
				}
			}
		}
		
		assertTrue(foundAverageGtZero);
	}
	
	void someIntermittentlySlowImportantBusinessLogicMethod() throws InterruptedException {
		if (Math.random() > 0.95) {
			Thread.sleep(25);
		}
	}
 	
	void someImportantBusinessLogicMethod() throws InterruptedException {
		Thread.sleep(100);
	}
	
	void someOtherImportantBusinessLogicMethod() throws InterruptedException {
		Thread.sleep(100);
	}
	
	private void verifyMeasureCreated(String partialMeasureName) {
		HashSet<RollupData> rollupData = new HashSet<RollupData>();
		TimeMeasure.snapshotTimeMeasureData(rollupData);
	
     	boolean foundMyMeasure = false;
		
		for(RollupData data : rollupData) {
			if (data.measureName.startsWith(partialMeasureName)) {
				foundMyMeasure = true;
				break;
				
			}
		}
		
		assertTrue(foundMyMeasure);
	}
}
