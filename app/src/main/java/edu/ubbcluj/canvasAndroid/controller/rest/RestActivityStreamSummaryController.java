package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ubbcluj.canvasAndroid.controller.ActivityStreamSummaryController;
import edu.ubbcluj.canvasAndroid.model.ActivityStreamSummary;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.persistence.ServiceProvider;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class RestActivityStreamSummaryController extends AsyncTask<String, Void, String> implements ActivityStreamSummaryController
{
	private List<ActivityStreamSummary> data;
	private SharedPreferences	sp;
	
	public RestActivityStreamSummaryController() {
		super();
		data = new ArrayList<ActivityStreamSummary>();
	}
	
	@Override
	public List<ActivityStreamSummary> getData() {
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
			response = RestInformation.getData(url);
		}
		
		data = new ArrayList<ActivityStreamSummary>();

		if (!CheckNetwork.isNetworkOnline(null)) {
			return "No connection";
		}
		
		// Decode JSON data and getting an ActivityStreamSummary array
		String jsonSource = response.replace("while(1);", "");

		try {	
			JSONArray jArr = new JSONArray(jsonSource);
			JSONObject obj = jArr.getJSONObject(0);
			ServiceProvider.getInstance().setNewAnnouncementUnreadCount(
					convertJSONtoASS(obj).getUnread_count());

		} catch (JSONException e) {
			Log.e("Json", e.getMessage(), new Error());
		}

		if ( isCancelled() ) {
			Log.d("AsyncTask", "ActivityStreamSummary asynctask cancelled");
		}
		
		return response;
	}
	
	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.ActivityStreamSummary} object.
	 */
	private ActivityStreamSummary convertJSONtoASS(JSONObject obj) {
		ActivityStreamSummary ass = new ActivityStreamSummary();

		try {
			ass.setType(obj.getString("type"));
			ass.setCount(obj.getInt("count"));
			ass.setUnread_count(obj.getInt("unread_count"));
		} catch (JSONException e) {
			Log.e("JSON", e.getMessage());
		}

		return ass;
	}

}
