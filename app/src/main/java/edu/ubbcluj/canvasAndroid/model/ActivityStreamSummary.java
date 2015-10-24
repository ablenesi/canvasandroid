package edu.ubbcluj.canvasAndroid.model;

public class ActivityStreamSummary {
	private String type;
	private int unread_count;
	private int count;
	
	public ActivityStreamSummary(String type, int unread_count, int count) {
		this.type = type;
		this.unread_count = unread_count;
		this.count = count;
	}

	public ActivityStreamSummary() {
		this(null,0,0);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getUnread_count() {
		return unread_count;
	}

	public void setUnread_count(int unread_count) {
		this.unread_count = unread_count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
