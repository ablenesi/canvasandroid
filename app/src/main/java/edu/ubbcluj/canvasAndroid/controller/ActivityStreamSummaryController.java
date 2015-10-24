package edu.ubbcluj.canvasAndroid.controller;

import java.util.List;

import android.content.SharedPreferences;
import edu.ubbcluj.canvasAndroid.model.ActivityStreamSummary;

public interface ActivityStreamSummaryController {

	List<ActivityStreamSummary> getData();

	void setSharedPreferences(SharedPreferences sp);
	
	void clearData();
}
