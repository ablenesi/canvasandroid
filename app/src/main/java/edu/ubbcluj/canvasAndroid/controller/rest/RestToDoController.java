package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.controller.ToDoController;
import edu.ubbcluj.canvasAndroid.model.Assignment;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public class RestToDoController extends AsyncTask<String, Void, String> implements
		ToDoController {

	private List<Assignment> data;
	private List<InformationListener> actionList;
	private SharedPreferences sp;

	public RestToDoController() {
		super();
		data = new ArrayList<Assignment>();
		actionList = new ArrayList<InformationListener>();
	}

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
		for (InformationListener il : actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}

	@Override
	public List<Assignment> getData() {
		return data;
	}

	@Override
	public void setSharedPreferences(SharedPreferences sp) {
		this.sp = sp;
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

		Assignment ass = new Assignment();
		ass.setName("Nothing for now!");

		for (String url : urls) {
			if (CookieHandler.checkData(sp, url))
				response = CookieHandler.getData(sp, url);
			else
			{
				response = RestInformation.getData(url);
				CookieHandler.saveData(sp, url, response);
			}
		}

		// Decode JSON data and getting an ActivityStream array
		String jsonSource = response.replace("while(1);", "");

		try {
			JSONArray jArr = new JSONArray(jsonSource);

			if (jArr.length() == 0) {
				data.add(ass);
			} else {
				for (int i = 0; i < jArr.length(); i++) {
					if ( isCancelled() ) break;
					JSONObject jObj = jArr.getJSONObject(i);
					data.add(convertJSONtoStr(jObj));
				}

				if (data.size() == 0) {
					data.add(ass);
				}
			}

		} catch (JSONException e) {
			Log.e("Error getting toDo informations!", e.getMessage(),
					new Error());
		}

		if ( isCancelled() ) {
			Log.d("AsyncTask", "Todo asynctask cancelled");
		}
		
		return response;
	}

	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.Assignment} object.
	 */
	private Assignment convertJSONtoStr(JSONObject jObj) {

		JSONObject assignmentObj;
		Assignment assignment = new Assignment();
		int courseId;
		int assignmentId;

		try {
			assignmentObj = jObj.getJSONObject("assignment");

			courseId = assignmentObj.getInt("course_id");
			assignmentId = assignmentObj.getInt("id");

			assignment.setCourseId(courseId);
			assignment.setId(assignmentId);
			assignment.setName(assignmentObj.getString("name"));
			if (!assignmentObj.isNull("points_possible")) {
				assignment.setPointsPossible(assignmentObj
						.getDouble("points_possible"));
			} else {
				assignment.setPointsPossible(0);
			}
			assignment.setDescription(assignmentObj.getString("description"));

			if (assignmentObj.isNull("due_at")) {
				assignment.setDueAt("No due date");
			} else {
				assignment.setDueAt(assignmentObj.getString("due_at"));
			}

			if (assignmentObj.isNull("lock_explanation")) {
				assignment.setLockExplanation(null);
			} else {
				assignment.setLockExplanation(assignmentObj
						.getString("lock_explanation"));
			}

			assignment.setIsGraded(false);

		} catch (JSONException e) {
			Log.e("JSON Courses", e.getMessage());
		}

		return assignment;
	}

	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyListeners();
	}

}
