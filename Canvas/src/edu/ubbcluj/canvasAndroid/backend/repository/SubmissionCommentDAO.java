package edu.ubbcluj.canvasAndroid.backend.repository;

import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;

public interface SubmissionCommentDAO {
	void setComment(String comment);
	
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);
}
