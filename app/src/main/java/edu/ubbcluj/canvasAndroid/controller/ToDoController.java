package edu.ubbcluj.canvasAndroid.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.Assignment;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface ToDoController {
	void addInformationListener(InformationListener il);
	
	void removeInformationListener(InformationListener il);

	List<Assignment> getData();	

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
