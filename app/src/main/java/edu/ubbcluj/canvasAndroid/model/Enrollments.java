package edu.ubbcluj.canvasAndroid.model;

public class Enrollments {

	private int id;
	private int courseId;
	private int courseSectionId;
	private String enrollmentState; //active
	private String type; //StudentEnrollment
	private int userId; //1
	private int associatedUserId;
	private String role;
	private String updatedAt; //"2012-04-18T23:08:51Z"
	private String startAt; //"2012-04-18T23:08:51Z"
	private String endAt; //"2012-04-18T23:08:51Z"
	private String lastActivityAt; //"2012-04-18T23:08:51Z"
	private String htmlUrl; //"https://..."
	private Grade grades;
	private User user;	
	
	private Enrollments() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCourseSectionId() {
		return courseSectionId;
	}

	public void setCourseSectionId(int courseSectionId) {
		this.courseSectionId = courseSectionId;
	}

	public String getEnrollmentState() {
		return enrollmentState;
	}

	public void setEnrollmentState(String enrollmentState) {
		this.enrollmentState = enrollmentState;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAssociatedUserId() {
		return associatedUserId;
	}

	public void setAssociatedUserId(int associatedUserId) {
		this.associatedUserId = associatedUserId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getStartAt() {
		return startAt;
	}

	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}

	public String getEndAt() {
		return endAt;
	}

	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}

	public String getLastActivityAt() {
		return lastActivityAt;
	}

	public void setLastActivityAt(String lastActivityAt) {
		this.lastActivityAt = lastActivityAt;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public Grade getGrades() {
		return grades;
	}

	public void setGrades(Grade grades) {
		this.grades = grades;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
