package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.controller.CoursesController;
import edu.ubbcluj.canvasAndroid.model.ActiveCourse;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public class RestCoursesController extends AsyncTask<String, Void, String> implements
		CoursesController {

	private ArrayList<ActiveCourse> data;
	private List<InformationListener> actionList;
	private SharedPreferences sp;

	public RestCoursesController() {
		actionList = new LinkedList<InformationListener>();
	}

	@Override
	public void setSharedPreferences(SharedPreferences sp) {
		this.sp = sp;
	};

	@Override
	public void addInformationListener(InformationListener il) {
		actionList.add(il);
	}

	@Override
	public void removeInformationListener(InformationListener il) {
		actionList.remove(il);
	}
	
	/**
	 * Notifies listeners that the data has been retrieved from the server, and it's conversion is finished.
	 */
	public synchronized void notifyListeners() {
		for (InformationListener il: actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}	

	@Override
	public void clearData() {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(sp);
		
		persistentCookieStore.clear();		
	}

	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected String doInBackground(String... urls) {
		String response = "";

		for (String url : urls) {
			if (CookieHandler.checkData(sp, url))
				response = CookieHandler.getData(sp, url);
			else
			{
				response = RestInformation.getData(url);
				CookieHandler.saveData(sp, url, response);
			}
		}

		data = new ArrayList<ActiveCourse>();

		// Decode JSON data and getting an ActivityStream array
		String jsonSource = response.replace("while(1);", "");

		try {
			JSONArray jArr = new JSONArray(jsonSource);

			for (int i = 0; i < jArr.length(); i++) {
				if ( isCancelled() ) break;
				JSONObject jObj = jArr.getJSONObject(i);
				data.add(convertJSONtoStr(jObj));
			}

		} catch (JSONException e) {
			Log.e("Json Courses", e.getMessage(), new Error());
		}
		
		if ( isCancelled() ) {
			Log.d("AsyncTask", "Courses asynctask cancelled");
		}

		return response;
	}

	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyListeners();
	}

	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.ActiveCourse} object.
	 */
	private ActiveCourse convertJSONtoStr(JSONObject jObj) {

		ActiveCourse activeCourse = new ActiveCourse();

		try {
			activeCourse.setId(jObj.getInt("id"));
			activeCourse.setName(jObj.getString("name"));
		} catch (JSONException e) {
			Log.e("JSON Courses", e.getMessage());
		}

		return activeCourse;
	}

	@Override
	public List<ActiveCourse> getData() {
		return data;
	}
}
