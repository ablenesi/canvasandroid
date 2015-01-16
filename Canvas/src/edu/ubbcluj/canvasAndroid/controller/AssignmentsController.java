package edu.ubbcluj.canvasAndroid.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.Assignment;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public interface AssignmentsController {
	public void addInformationListener(InformationListener il);

	public void removeInformationListener(InformationListener il);

	public List<Assignment> getData();

	public void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
