package edu.ubbcluj.canvasAndroid.persistence.model;

import android.content.SharedPreferences;

public class SingletonSharedPreferences {
	
	private static SingletonSharedPreferences instance = null;
	private SharedPreferences sp;
	 
	private SingletonSharedPreferences() {}
	
	public static synchronized SingletonSharedPreferences getInstance() {
		if (instance == null) {
			instance = new SingletonSharedPreferences();
		}
		
		return instance;
	}
	
	public synchronized void init(SharedPreferences sharedPreferences){
	    this.sp = sharedPreferences;
	}

	public SharedPreferences getSharedPreferences() {
		return sp;
	}

	public void setSharedPreferences(SharedPreferences sp) {
		this.sp = sp;
	}
}
