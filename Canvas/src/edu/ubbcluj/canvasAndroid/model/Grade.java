package edu.ubbcluj.canvasAndroid.model;

public class Grade {

	private int id;
	private String htmlUrl;
	private Double currentGrade;
	private Double finalGrade;
	private Double currentScore;
	private Double finalScore;

	public Grade() {
		this(0, null, null, null,null, null);
	}
	
	public Grade(int id, String htmlUrl, Double currentGrade,
			Double finalGrade, Double currentScore, Double finalScore) {
		super();
		this.id = id;
		this.htmlUrl = htmlUrl;
		this.currentGrade = currentGrade;
		this.finalGrade = finalGrade;
		this.currentScore = currentScore;
		this.finalScore = finalScore;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public Double getCurrentGrade() {
		return currentGrade;
	}

	public void setCurrentGrade(Double currentGrade) {
		this.currentGrade = currentGrade;
	}

	public Double getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(Double finalGrade) {
		this.finalGrade = finalGrade;
	}

	public Double getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(Double currentScore) {
		this.currentScore = currentScore;
	}

	public Double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(Double finalScore) {
		this.finalScore = finalScore;
	}
	
	
	
}
