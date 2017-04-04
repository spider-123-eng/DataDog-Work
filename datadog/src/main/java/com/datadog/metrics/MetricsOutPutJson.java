package com.datadog.metrics;

import java.util.List;

public class MetricsOutPutJson {

	private List<Series> series = null;

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}

	@Override
	public String toString() {
		return "SamzaOutPutJson [series=" + series + "]";
	}

}