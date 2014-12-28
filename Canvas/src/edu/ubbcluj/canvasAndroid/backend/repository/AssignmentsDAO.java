package edu.ubbcluj.canvasAndroid.backend.repository;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.Assignment;

public interface AssignmentsDAO {
	public void addInformationListener(InformationListener il);

	public void removeInformationListener(InformationListener il);

	public List<Assignment> getData();

	public void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
