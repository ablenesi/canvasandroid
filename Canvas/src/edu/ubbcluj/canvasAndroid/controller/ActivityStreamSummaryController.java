package edu.ubbcluj.canvasAndroid.backend.repository;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.ActivityStreamSummary;

public interface ActivityStreamSummaryDAO {

	List<ActivityStreamSummary> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
