package edu.ubbcluj.canvasAndroid.model;

public class ActiveCourse {
	private int id;
	private String name;

	public ActiveCourse(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public ActiveCourse() {
		this(0, null);
	}

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

}
