package edu.ubbcluj.canvasAndroid.model;

public class AnnouncementComment {
	private int id;
	private int userId;
	private String userName;
	private String message;
	private AnnouncementCommentReplies[] acr;
	
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
	public AnnouncementCommentReplies[] getAcr() {
		return acr;
	}
	public void setAcr(AnnouncementCommentReplies[] acr) {
		this.acr = acr;
	}
	
	
}
