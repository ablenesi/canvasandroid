package edu.ubbcluj.canvasAndroid.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.MessageSequence;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface MessageSequenceController {
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<MessageSequence> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
