package edu.ubbcluj.canvasAndroid.model;

public class ActivityStream {
	private int id;
	private int courseId;
	private int secondaryId; // assigmnentId, announcementId, conversationId
	private String title;
	private String message;
	private String type;
	private Boolean read_state;
	
	public ActivityStream(int id, String title, String message, String type) {
		this.id = id;
		this.title = title;
		this.message = message;
		this.type = type;
	}
	
	public ActivityStream() {
		this(0, null, null, null);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getSecondaryId() {
		return secondaryId;
	}

	public void setSecondaryId(int secondaryId) {
		this.secondaryId = secondaryId;
	}

	public Boolean getRead_state() {
		return read_state;
	}

	public void setRead_state(Boolean read_state) {
		this.read_state = read_state;
	}
}
