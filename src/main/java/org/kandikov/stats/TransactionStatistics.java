package org.kandikov.stats;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TransactionStatistics {
	private double sum;
	private double avg;
	private double max;
	private double min;
	private long count;

	@JsonIgnore
	private long lastUpdated;

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	private double updateAvg() {
		if (count > 0)
			return this.sum / this.count;
		else
			return 0;
	}

	public double getAvg() {
		this.avg = updateAvg();
		return this.avg;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
