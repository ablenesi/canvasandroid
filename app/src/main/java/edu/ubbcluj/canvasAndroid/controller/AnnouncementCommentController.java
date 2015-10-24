package edu.ubbcluj.canvasAndroid.controller;

import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface AnnouncementCommentController {
	void setComment(String comment);
	
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

}
