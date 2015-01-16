package edu.ubbcluj.canvasAndroid.backend.repository;

import edu.ubbcluj.canvasAndroid.MessageItemActivity;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.MessageSequence;

public interface NewMessageDAO {

	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);
	
	public void setMessageItemActivity(MessageItemActivity activity);

	public void setBody(String body);

	public MessageSequence getData();
}
