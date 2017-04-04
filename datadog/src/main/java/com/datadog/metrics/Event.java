package com.datadog.metrics;

import java.util.List;

public class Event {

	private String title;
	private String text;
	private String host;
	private String priority;
	private String alert_type;
	private Long date_happened;
	private List<String> tags = null;
	public Long getDate_happened() {
		return date_happened;
	}

	public void setDate_happened(Long date_happened) {
		this.date_happened = date_happened;
	}

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getAlert_type() {
		return alert_type;
	}

	public void setAlert_type(String alert_type) {
		this.alert_type = alert_type;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "Event [title=" + title + ", text=" + text + ", host=" + host + ", priority=" + priority
				+ ", alert_type=" + alert_type + ", date_happened=" + date_happened + ", tags=" + tags + "]";
	}

}
