package edu.ubbcluj.canvasAndroid.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.Conversation;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface ConversationController {

	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<Conversation> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
