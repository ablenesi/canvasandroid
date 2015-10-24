package edu.ubbcluj.canvasAndroid.model;

public class Assignment {
	private int id;
	private String name;
	private String description;
	private String dueAt;
	private String lockAt;
	private String unlockAt;
	private int courseId;
	private String courseName;
	private String htmlUrl;
	private int assignmentGroupId;
	private int position;
	private boolean muted;
	private double pointsPossible;
	private double score;
	private boolean isGraded;
	private String[] submissionTypes;
	private String gradingType;
	private String lockInfo;
	private String lockExplanation;
	private Submission submission;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDueAt() {
		return dueAt;
	}
	public void setDueAt(String dueAt) {
		this.dueAt = dueAt;
	}
	public String getLockAt() {
		return lockAt;
	}
	public void setLockAt(String lockAt) {
		this.lockAt = lockAt;
	}
	public String getUnlockAt() {
		return unlockAt;
	}
	public void setUnlockAt(String unlockAt) {
		this.unlockAt = unlockAt;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getHtmlUrl() {
		return htmlUrl;
	}
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}
	public int getAssignmentGroupId() {
		return assignmentGroupId;
	}
	public void setAssignmentGroupId(int assignmentGroupId) {
		this.assignmentGroupId = assignmentGroupId;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public boolean isMuted() {
		return muted;
	}
	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	public double getPointsPossible() {
		return pointsPossible;
	}
	public void setPointsPossible(double pointsPossible) {
		this.pointsPossible = pointsPossible;
	}
	public String[] getSubmissionTypes() {
		return submissionTypes;
	}
	public void setSubmissionTypes(String[] submissionTypes) {
		this.submissionTypes = submissionTypes;
	}
	public String getGradingType() {
		return gradingType;
	}
	public void setGradingType(String gradingType) {
		this.gradingType = gradingType;
	}
	public String getLockInfo() {
		return lockInfo;
	}
	public void setLockInfo(String lockInfo) {
		this.lockInfo = lockInfo;
	}
	public String getLockExplanation() {
		return lockExplanation;
	}
	public void setLockExplanation(String lockExplanation) {
		this.lockExplanation = lockExplanation;
	}
	public Submission getSubmission() {
		return submission;
	}
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}	
	public boolean getIsGraded() {
		return isGraded;
	}
	public void setIsGraded(boolean isGraded) {
		this.isGraded = isGraded;
	}
}
