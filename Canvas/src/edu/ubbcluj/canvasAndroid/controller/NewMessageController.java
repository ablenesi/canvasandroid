package edu.ubbcluj.canvasAndroid.controller;

import edu.ubbcluj.canvasAndroid.model.MessageSequence;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;
import edu.ubbcluj.canvasAndroid.view.activity.MessageItemActivity;

public interface NewMessageController {

	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);
	
	public void setMessageItemActivity(MessageItemActivity activity);

	public void setBody(String body);

	public MessageSequence getData();
}
