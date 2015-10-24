package edu.ubbcluj.canvasAndroid.persistence;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ServiceProvider {

	private static ServiceProvider instance;
	private SharedPreferences sp;
	private String spName;
	private int announcementUnreadCount;
	private int newAnnouncementUnreadCount;
	private Boolean service_started;
	
	private ServiceProvider() {
		this.sp = null;
		this.spName = null;
		announcementUnreadCount = 0;
		newAnnouncementUnreadCount = 0;
		service_started = false;
	}
	
	public static ServiceProvider getInstance() {
		if (instance == null) 
			instance = new ServiceProvider();
		
		return instance;
	}
	
	public void initalize(Context context) {
		spName = new String("ServiceInfo");
		sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		
		Map<String, ?> savedCourses = sp.getAll();
		Set<String> keySet = savedCourses.keySet();
		
		for (String key : keySet) {
			String value = (String) savedCourses.get(key);
			String[] splittedValue = value.split(";");
			service_started = Boolean.valueOf(splittedValue[0]);
			announcementUnreadCount = Integer.parseInt(splittedValue[1]);
			newAnnouncementUnreadCount = Integer.parseInt(splittedValue[2]);
		}
	}

	public int getAnnouncementUnreadCount() {
		return announcementUnreadCount;
	}

	public int getNewAnnouncementUnreadCount() {
		return newAnnouncementUnreadCount;
	}

	public Boolean getService_started() {
		return service_started;
	}
	
	public void setAnnouncementUnreadCount(int announcementUnreadCount) {
		this.announcementUnreadCount = announcementUnreadCount;
		saveStateToSharedPreferences();
	}

	public void setNewAnnouncementUnreadCount(int newAnnouncementUnreadCount) {
		this.newAnnouncementUnreadCount = newAnnouncementUnreadCount;
		saveStateToSharedPreferences();
	}

	public void setService_started(Boolean service_started) {
		this.service_started = service_started;
		saveStateToSharedPreferences();
	}
	
	private void saveStateToSharedPreferences() {
		Editor editor = sp.edit();
		editor.clear();
		
		String key = new String("service");
		String value = new String((service_started?"true":"false") + ";" + announcementUnreadCount+";"+newAnnouncementUnreadCount);
		
		editor.putString(key, value);
		editor.commit();
	}

}
