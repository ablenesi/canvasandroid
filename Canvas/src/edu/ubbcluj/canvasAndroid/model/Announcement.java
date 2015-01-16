package edu.ubbcluj.canvasAndroid.model;

public class Announcement {

	private String title;
	private String authorName;
	private String message;
	private String postedAt;
	private int courseId;
	private int announcementId;
	private boolean read_state;
	private AnnouncementComment[] ac;

	public AnnouncementComment[] getAc() {
		return ac;
	}
	public void setAc(AnnouncementComment[] ac) {
		this.ac = ac;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPostedAt() {
		return postedAt;
	}
	public void setPostedAt(String postedAt) {
		this.postedAt = postedAt;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getAnnouncementId() {
		return announcementId;
	}
	public void setAnnouncementId(int announcementId) {
		this.announcementId = announcementId;
	}
	public boolean getRead_state() {
		return read_state;
	}
	public void setRead_state(boolean read_state) {
		this.read_state = read_state;
	}
}
