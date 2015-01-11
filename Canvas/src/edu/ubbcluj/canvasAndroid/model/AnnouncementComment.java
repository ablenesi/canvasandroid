package edu.ubbcluj.canvasAndroid.model;

public class AnnouncementComment {
	private int id;
	private int userId;
	private String userName;
	private String message;
	
	public AnnouncementComment(int id, int userId, String userName, String message) {
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.message = message;
	}

	public AnnouncementComment() {
		this(0, 0, null, null);
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
