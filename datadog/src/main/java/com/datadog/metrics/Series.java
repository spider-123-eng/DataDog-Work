package com.datadog.metrics;

import java.util.List;

public class Series {

	private String metric;

	private List<List<Long>> points = null;

	private String type;
	private String host;

	List<String> tags = null;

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}



	public List<List<Long>> getPoints() {
		return points;
	}

	public void setPoints(List<List<Long>> points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "Series [metric=" + metric + ", points=" + points + ", type=" + type + ", host=" + host + ", tags="
				+ tags + "]";
	}

}
