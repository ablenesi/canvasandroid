package edu.ubbcluj.canvasAndroid.model;

public class ActiveCourse {
	private int id;
	private String name;
	private boolean selected;

	public ActiveCourse(int id, String name) {
		this.id = id;
		this.name = name;
		this.selected = false;
	}

	public ActiveCourse(int id, String name, boolean selected) {
		this.id = id;
		this.name = name;
		this.selected = selected;
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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
