package edu.ubbcluj.canvasAndroid.model;

public class Folder implements FileTreeElement {

	private int id;
	private String name;
	private String foldersUrl;
	private String filesUrl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFoldersUrl() {
		return foldersUrl;
	}

	public void setFoldersUrl(String foldersUrl) {
		this.foldersUrl = foldersUrl;
	}

	public String getFilesUrl() {
		return filesUrl;
	}

	public void setFilesUrl(String filesUrl) {
		this.filesUrl = filesUrl;
	}
}
