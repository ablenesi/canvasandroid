package edu.ubbcluj.canvasAndroid.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.Announcement;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface AnnouncementController {
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<Announcement> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
