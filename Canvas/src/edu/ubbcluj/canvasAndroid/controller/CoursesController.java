package edu.ubbcluj.canvasAndroid.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.ActiveCourse;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface CoursesController {
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il); 

	List<ActiveCourse> getData();
	
	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
