package edu.ubbcluj.canvasAndroid.model;

public class MessageSequence {
	private int id;
	private int authorID;
	private String name;
	private String body;
	private String createdAt;

	public MessageSequence(int id, int authorID, String name, String body,
			String createdAt) {
		super();
		this.id = id;
		this.authorID = authorID;
		this.name = name;
		this.body = body;
		this.createdAt = createdAt;
	}

	public MessageSequence() {
		this(0, 0, null, null, null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getAuthorID() {
		return authorID;
	}

	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

}
