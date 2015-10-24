package edu.ubbcluj.canvasAndroid.model;

public class File implements FileTreeElement {

	private int id;
	private String displayName;
	private String url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String displayName) {
		this.displayName = displayName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getName() {
		return displayName;
	}

}
