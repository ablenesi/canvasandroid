package edu.ubbcluj.canvasAndroid.backend.repository;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.AnnouncementComment;

public interface AnnouncementCommentDAO {
	void addInformationListener(InformationListener il);

	void removeInformationListener(InformationListener il);

	List<AnnouncementComment> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
