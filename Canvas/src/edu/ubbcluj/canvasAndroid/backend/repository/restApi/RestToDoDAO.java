package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.backend.repository.ToDoDAO;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.model.Assignment;

public class RestToDoDAO extends AsyncTask<String, Void, String>
implements ToDoDAO {
	
	private List<Assignment> data;
	private List<InformationListener> actionList;
	private int courseId;
	
	public RestToDoDAO() {
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
	
	public synchronized void notifyListeners() {
		for (InformationListener il: actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}	

	@Override
	public List<Assignment> getData() {	
		return data;
	}

	@Override
	protected String doInBackground(String... urls) {
		String response = "";
		int currentCourseId;
		
		Assignment ass = new Assignment();
		ass.setName("Nothing for now!");		
		
		for (String url : urls) {
			response = RestInformationDAO.getData(url);
		}
		
		if (!CheckNetwork.isNetworkOnline(null)) {
			return "No connection";
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
					currentCourseId = jObj.getInt("course_id");

					if (currentCourseId == this.courseId) {
						data.add(convertJSONtoStr(jObj));
					}	
				}
				
				if (data.size() == 0) {
					data.add(ass);					
				}
			}	

		} catch (JSONException e) {
			Log.e("Error getting toDo informations!", e.getMessage(), new Error());
		}

		if ( isCancelled() ) {
			Log.d("AsyncTask", "Todo asynctask cancelled");
		}
		
		return response;
	}
	
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
				assignment.setPointsPossible(assignmentObj.getDouble("points_possible"));
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
				assignment.setLockExplanation(assignmentObj.getString("lock_explanation"));
			}
			
			assignment.setIsGraded(false);
			
		} catch (JSONException e) {
			Log.e("JSON Courses", e.getMessage());
		}

		return assignment;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyListeners();
	}

	@Override
	public void setCourseId(int courseId) {
		this.courseId = courseId;		
	}	
	
}
