package edu.ubbcluj.canvasAndroid.backend.repository;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.NavigationDrawerFragment;

public interface CoursesDAO {
	void setNdf(NavigationDrawerFragment ndf);

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
