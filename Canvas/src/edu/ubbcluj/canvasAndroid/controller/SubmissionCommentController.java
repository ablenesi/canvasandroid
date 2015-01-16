package edu.ubbcluj.canvasAndroid.controller;

import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface SubmissionCommentController {
	void setComment(String comment);
	
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);
}
