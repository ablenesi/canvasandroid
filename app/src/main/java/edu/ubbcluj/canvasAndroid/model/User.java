package edu.ubbcluj.canvasAndroid.model;

public class User {
	private int id;
	private String name;
	private String shortName;
	private String primaryEmail; 	//"sample_user@example.com"
	private String loginId; 		//"sample_user@example.com"
	private String avatarUrl; 		//http://....
	   
	public User() {
		this(0,null,null,null,null,null);
	}
	
	public User(int id, String name, String shortName, String primaryEmail,
			String loginId, String avatarUrl) {
		super();
		this.id = id;
		this.name = name;
		this.shortName = shortName;
		this.primaryEmail = primaryEmail;
		this.loginId = loginId;
		this.avatarUrl = avatarUrl;
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
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPrimaryEmail() {
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
}
