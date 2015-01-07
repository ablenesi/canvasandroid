package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ubbcluj.canvasAndroid.MyService;
import edu.ubbcluj.canvasAndroid.backend.repository.ActivityStreamSummaryDAO;
import edu.ubbcluj.canvasAndroid.backend.util.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.model.ActivityStreamSummary;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class RestActivityStreamSummaryDAO extends AsyncTask<String, Void, String> implements ActivityStreamSummaryDAO
{

	private List<ActivityStreamSummary> data;
	private SharedPreferences	sp;
	
	public RestActivityStreamSummaryDAO() {
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

	@Override
	protected String doInBackground(String... urls) {
		String response = "";

		// Get JSON data from url
		for (String url : urls) {
			response = RestInformationDAO.getData(url);
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
			MyService.newAnnouncementUnreadCount = convertJSONtoASS(obj).getUnread_count();

		} catch (JSONException e) {
			Log.e("Json", e.getMessage(), new Error());
		}

		if ( isCancelled() ) {
			Log.d("AsyncTask", "ActivityStreamSummary asynctask cancelled");
		}
		
		return response;
	}
	
	// Convert JSON object to ActivityStream object
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
