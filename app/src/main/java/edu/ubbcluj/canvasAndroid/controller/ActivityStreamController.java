package edu.ubbcluj.canvasAndroid.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.ActivityStream;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface ActivityStreamController {
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<ActivityStream> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
