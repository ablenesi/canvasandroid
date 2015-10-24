package edu.ubbcluj.canvasAndroid.model;

public class Submission {

	private int id;
	private int assignmentId;
	private Assignment assignment;
	private ActiveCourse course;
	private int attempt;
	private String body;
	private String grade;
	private boolean gradeMatchesCurrentSubmission;
	private String html_url;
	private String previewUrl;
	private double score;
	private SubmissionAttachment[] attachments;
	private SubmissionComment[] submissionComments;
	private String submissionType;
	private String submittedAt;
	private String workflowState;
	private String url;
	private int userId;
	private int graderId;
	private User user;
	private boolean late;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public ActiveCourse getCourse() {
		return course;
	}

	public void setCourse(ActiveCourse course) {
		this.course = course;
	}

	public int getAttempt() {
		return attempt;
	}

	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public boolean getGradeMatchesCurrentSubmission() {
		return gradeMatchesCurrentSubmission;
	}

	public void setGradeMatchesCurrentSubmission(
			boolean gradeMatchesCurrentSubmission) {
		this.gradeMatchesCurrentSubmission = gradeMatchesCurrentSubmission;
	}

	public String getHtml_url() {
		return html_url;
	}

	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public SubmissionAttachment[] getAttachments() {
		return attachments;
	}

	public void setAttachments(SubmissionAttachment[] attachments) {
		this.attachments = attachments;
	}

	public SubmissionComment[] getSubmissionComments() {
		return submissionComments;
	}

	public void setSubmissionComments(SubmissionComment[] submissionComments) {
		this.submissionComments = submissionComments;
	}

	public String getSubmissionType() {
		return submissionType;
	}

	public void setSubmissionType(String submissionType) {
		this.submissionType = submissionType;
	}

	public String getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(String submittedAt) {
		this.submittedAt = submittedAt;
	}

	public String getWorkflowState() {
		return workflowState;
	}

	public void setWorkflowState(String workflowState) {
		this.workflowState = workflowState;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGraderId() {
		return graderId;
	}

	public void setGraderId(int graderId) {
		this.graderId = graderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean getLate() {
		return late;
	}

	public void setLate(boolean late) {
		this.late = late;
	}
}
