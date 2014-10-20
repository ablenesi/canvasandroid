package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.backend.repository.AssignmentsDAO;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.model.Assignment;

public class RestAssignmentsDAO extends AsyncTask<String, Void, String>
		implements AssignmentsDAO {

	private List<Assignment> data;
	private List<InformationListener> actionList;

	public RestAssignmentsDAO() {
		super();
		actionList = new ArrayList<InformationListener>();
	}
	
	@Override
	protected String doInBackground(String... urls) {
		String response = "";

		for (String url : urls) {
			response = RestInformationDAO.getData(url);
		}

		data = new ArrayList<Assignment>();

		if (!CheckNetwork.isNetworkOnline(null)) {
			return "No connection";
		}
		
		// Decode JSON data and getting an ActivityStream array
		String jsonSource = response.replace("while(1);", "");

		if (jsonSource.startsWith("[")) {
			try {
				JSONArray jArr = new JSONArray(jsonSource);

				for (int i = 0; i < jArr.length(); i++) {
					JSONObject jObj = jArr.getJSONObject(i);
					data.add(convertJSONtoStr(jObj));
				}

			} catch (JSONException e) {
				Log.e("Json Assignments", e.getMessage(), new Error());
			}
		} else {
			try {
				JSONObject jObj = new JSONObject(jsonSource);
				data.add(convertJSONtoStr(jObj));
			} catch (JSONException e) {
				Log.e("Json Assignment", e.getMessage(), new Error());
			}
		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyListeners();
	}

	private Assignment convertJSONtoStr(JSONObject jObj) {

		Assignment assignment = new Assignment();
		int courseId;
		int assignmentId;

		try {
			courseId = jObj.getInt("course_id");
			assignmentId = jObj.getInt("id");

			assignment.setCourseId(courseId);
			assignment.setId(assignmentId);
			assignment.setName(jObj.getString("name"));
			if (!jObj.isNull("points_possible")) {
				assignment.setPointsPossible(jObj.getDouble("points_possible"));
			} else {
				assignment.setPointsPossible(0);
			}
			assignment.setDescription(jObj.getString("description"));
	
			if (jObj.isNull("due_at")) {
				assignment.setDueAt("No due date");
			} else {
				assignment.setDueAt(jObj.getString("due_at"));
			}
			
			if (jObj.isNull("lock_explanation")) {
				assignment.setLockExplanation(null);
			} else {
				assignment.setLockExplanation(jObj.getString("lock_explanation"));
			}

			String url = PropertyProvider.getProperty("url")
					+ "/api/v1/courses/" + courseId + "/assignments/"
					+ assignmentId + "/submissions/self";
			String response = RestInformationDAO.getData(url).replace(
					"while(1);", "");

			JSONObject subbmissionObj = new JSONObject(response);

			if (subbmissionObj.getString("score").equals("null"))
				assignment.setIsGraded(false);
			else {
				assignment.setIsGraded(true);
				assignment.setScore(subbmissionObj.getDouble("score"));
			}

		} catch (JSONException e) {
			Log.e("JSON Courses", e.getMessage());
		}

		return assignment;
	}

	public synchronized void addInformationListener(InformationListener il) {
		actionList.add(il);
	}
	
	public synchronized void removeInformationListener(InformationListener il) {
		actionList.remove(il);
	}
	
	public synchronized void notifyListeners() {
		for (InformationListener il: actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}

	public List<Assignment> getData() {
		return data;
	}
}
