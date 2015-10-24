package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.controller.ActivityStreamController;
import edu.ubbcluj.canvasAndroid.model.ActivityStream;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public class RestActivityStreamController extends AsyncTask<String, Void, String> implements
		ActivityStreamController {

	private List<ActivityStream> data;
	private List<InformationListener> actionList;
	private SharedPreferences	sp;
	
	public RestActivityStreamController() {
		super();
		data = new ArrayList<ActivityStream>();
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
		for (InformationListener il: actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}	

	@Override
	public List<ActivityStream> getData() {	
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

		// Get JSON data from url
		for (String url : urls) {
			if (CookieHandler.checkData(sp, url))
				response = CookieHandler.getData(sp, url);
			else
			{
				response = RestInformation.getData(url);
				CookieHandler.saveData(sp, url, response);
			}
		}
		
		data = new ArrayList<ActivityStream>();
		
		// Decode JSON data and getting an ActivityStream array
		String jsonSource = response.replace("while(1);", "");

		try {	
			JSONArray jArr = new JSONArray(jsonSource);

			for (int i = 0; i < jArr.length(); i++) {
				if ( isCancelled() ) break;
				JSONObject obj = jArr.getJSONObject(i);
				ActivityStream as = convertJSONtoAS(obj);
				if (as != null) {
					data.add(as);
				}
			}

		} catch (JSONException e) {
			Log.e("Json", e.getMessage(), new Error());
		}

		if ( isCancelled() ) {
			Log.d("AsyncTask", "ActivityStream asynctask cancelled");
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
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.ActivityStream} object.
	 */
	private ActivityStream convertJSONtoAS(JSONObject obj) {
		ActivityStream as = new ActivityStream();

		try {
			as.setId(obj.getInt("id"));
			as.setMessage(obj.getString("message"));
			as.setTitle(obj.getString("title"));
			as.setType(obj.getString("type"));
			as.setRead_state(obj.getBoolean("read_state"));
			
			if (!obj.isNull("course_id")) {
				as.setCourseId(obj.getInt("course_id"));
			}
			
			if (as.getType().equals("Submission")) {
				JSONObject assignment = obj.getJSONObject("assignment");
				as.setSecondaryId(assignment.getInt("id"));
			} 
			
			if (as.getType().equals("Announcement")) {
				as.setSecondaryId(obj.getInt("announcement_id"));
			}
			
			if (as.getType().equals("Message")) {
				String htmlUrl = obj.getString("html_url");
				String[] parts = htmlUrl.split("/");
				
				int i = 0;
				
				while (!parts[i].startsWith("courses"))
					i++;
								
				if (parts.length <= i + 3) {
					return null;
				}
				
				as.setCourseId(Integer.parseInt(parts[i + 1]));
				as.setSecondaryId(Integer.parseInt(parts[i + 3]));
			}
			
			// Get conversations
			if (as.getType().equals("Conversation")) {
				int nParticipants = obj.getInt("participant_count");

				//conversation id 
				as.setSecondaryId(obj.getInt("conversation_id"));
				
				String newTitle = "";
				
				if (nParticipants <= 6) {
					int convID = obj.getInt("conversation_id");
					
					String url = PropertyProvider.getProperty("url") + "/api/v1/conversations/" + convID;

					String response = RestInformation.getData(url).replace("while(1);", "");
					
					JSONObject convObj = new JSONObject(response);
					JSONArray participantsArr = convObj
							.getJSONArray("participants");

					newTitle = "Conversation between: ";
					
					for (int i = 0; i < participantsArr.length(); i++) {
						JSONObject participant = participantsArr
								.getJSONObject(i);
						String name = participant.getString("name");

						newTitle += name + ", ";
					}
					
					newTitle = newTitle.substring(0, newTitle.length() - 2);
				} else {
					newTitle = "Conversation between "
							+ nParticipants + " participants";
				}
				
				as.setTitle(newTitle);
			}
			
			if (as.getType().equals("DiscussionTopic")) {
				as.setSecondaryId(obj.getInt("discussion_topic_id"));
			} 
		} catch (JSONException e) {
			Log.e("JSON", e.getMessage());
		}

		return as;
	}
}
