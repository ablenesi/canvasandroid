package edu.ubbcluj.canvasAndroid.model;

public class SubmissionAttachment {

	private int id;
	private String contentType;
	private String dispalyName;
	private String fileName;
	private String url;
	private int size;
	private String createdAt;
	private String updatedAt;
	private String unlockAt;
	private boolean locked;
	private boolean hidden;
	private boolean hiddenForUser;
	private String thumbnailUrl;
	private boolean lockedForUser;
	private String previewUrl;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getDispalyName() {
		return dispalyName;
	}
	public void setDispalyName(String dispalyName) {
		this.dispalyName = dispalyName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getUnlockAt() {
		return unlockAt;
	}
	public void setUnlockAt(String unlockAt) {
		this.unlockAt = unlockAt;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean isHiddenForUser() {
		return hiddenForUser;
	}
	public void setHiddenForUser(boolean hiddenForUser) {
		this.hiddenForUser = hiddenForUser;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public boolean isLockedForUser() {
		return lockedForUser;
	}
	public void setLockedForUser(boolean lockedForUser) {
		this.lockedForUser = lockedForUser;
	}
	public String getPreviewUrl() {
		return previewUrl;
	}
	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

}
