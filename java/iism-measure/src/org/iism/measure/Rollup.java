package org.iism.measure;


public class Rollup {
	public Rollup(long startTime, long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public long min = Long.MAX_VALUE;
	public long max = Long.MIN_VALUE;
	public long count = 0;
	public long total = 0;
	public long startTime;
	public long endTime;
	public boolean active = false;

	public void addMeasure(long value) {
		total += value;
		count++;
		if (this.max < value) {
			this.max = value;
		}
		if (this.min > value) {
			this.min = value;
		}
	}
}


