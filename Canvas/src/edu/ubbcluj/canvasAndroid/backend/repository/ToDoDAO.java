package edu.ubbcluj.canvasAndroid.backend.repository;

import java.util.List;

import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.Assignment;

public interface ToDoDAO {
	void addInformationListener(InformationListener il);
	void removeInformationListener(InformationListener il);
	void setCourseId(int courseId);
	List<Assignment> getData();	
}


