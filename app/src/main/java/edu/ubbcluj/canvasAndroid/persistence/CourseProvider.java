package edu.ubbcluj.canvasAndroid.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import edu.ubbcluj.canvasAndroid.model.ActiveCourse;

public class CourseProvider {

	private static CourseProvider instance;
	private String username;
	private SharedPreferences sp;
	private String spName;
	private List<ActiveCourse> courses;
	private List<ActiveCourse> selectedCourses;
	
	private CourseProvider() {
		this.sp = null;
		this.spName = null;
		this.username = null;
		courses = new ArrayList<ActiveCourse>();
		selectedCourses = new ArrayList<ActiveCourse>();
	}
	
	public static CourseProvider getInstance() {
		if (instance == null) 
			instance = new CourseProvider();
		
		return instance;
	}

	public void initalize(Context context, String username) {
		
		courses.clear();
		selectedCourses.clear();
		
		spName = new String("CourseInfo_" + username);
		sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		
		Map<String, ?> savedCourses = sp.getAll();
		Set<String> keySet = savedCourses.keySet();
		
		for (String key : keySet) {
			String value = (String) savedCourses.get(key);
			String[] splittedValue = value.split(";");
			
			boolean isSelected = splittedValue[1].compareTo("true") == 0;
			ActiveCourse course = new ActiveCourse(Integer.parseInt(key), splittedValue[0], isSelected);
			courses.add(course);
		}
		
		updateSelectedCourses();
	}
	
	public void initialize(Context context) {
		SharedPreferences userSp = context.getSharedPreferences(
				"CanvasAndroid-users", Context.MODE_PRIVATE);
		username = CookieHandler.getData(userSp, "lastusername").replace('@', '.');
		
		initalize(context, username);
	}
	
	public List<ActiveCourse> getAllCourses() {
		return courses;
	}
	
	public List<ActiveCourse> getSelectedCourses() {
		return selectedCourses;
	}
	
	public void setSelected(ActiveCourse course, boolean selected) {
		ActiveCourse c = getCourseWithID(course.getId());
		if (c != null) {
			c.setSelected(selected);
			saveStateToSharedPreferences();
			updateSelectedCourses();
		}
	}
	
	public void updateWith(List<ActiveCourse> courseList) {
		List<ActiveCourse> newCourses = new ArrayList<ActiveCourse>();
		
		for (ActiveCourse course : courseList) {
			ActiveCourse c = getCourseWithID(course.getId());
			if (c != null) {
				newCourses.add(c);
			} else {
				course.setSelected(true);
				newCourses.add(course);
			}
		}
		
		for (ActiveCourse course : courses) {
			if (!containsCourseWithId(newCourses, course.getId())) {
				newCourses.add(course);
			}
		}
		
		courses = newCourses;
		saveStateToSharedPreferences();
		updateSelectedCourses();
	}
	
	private void updateSelectedCourses() {
		selectedCourses.clear();
		
		for (ActiveCourse c : courses) {
			if (c.isSelected()) {
				selectedCourses.add(c);
			}
		}
	}
	
	private boolean containsCourseWithId(List<ActiveCourse> courses, int id) {
		Iterator<ActiveCourse> it = courses.iterator();
		
		while (it.hasNext()) {
			ActiveCourse c = it.next();
			if (c.getId() == id) {
				return true;
			}
		}
		
		return false;
	}
	
	public ActiveCourse getCourseWithID(int id) {
		Iterator<ActiveCourse> it = courses.iterator();
		
		while (it.hasNext()) {
			ActiveCourse c = it.next();
			if (c.getId() == id) {
				return c;
			}
		}
		
		return null;
	}
	
	private void saveStateToSharedPreferences() {
		Editor editor = sp.edit();
		editor.clear();
		
		for (ActiveCourse c : courses) {
			String key = new String(c.getId() + "");
			String value = new String(c.getName() + ";" + c.isSelected());
			editor.putString(key, value);
		}
		
		editor.commit();
	}
	
}
