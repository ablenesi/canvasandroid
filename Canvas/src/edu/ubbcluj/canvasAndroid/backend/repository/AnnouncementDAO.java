package edu.ubbcluj.canvasAndroid.backend.repository;

import java.util.List;

import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.Announcement;

public interface AnnouncementDAO {
	void addInformationListener(InformationListener il);
	void removeInformationListener(InformationListener il);
	List<Announcement> getData();	
}
