package edu.ubbcluj.canvasAndroid.backend.repository;

import java.util.List;

import android.content.SharedPreferences;

import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.Conversation;

public interface ConversationDAO {

	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<Conversation> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
