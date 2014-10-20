package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.backend.repository.AnnouncementDAO;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.model.Announcement;

public class RestAnnouncementDAO extends AsyncTask<String, Void, String> implements
		AnnouncementDAO {

	private List<Announcement> data;
	private List<InformationListener> actionList;

	public RestAnnouncementDAO() {
		super();
		data = new ArrayList<Announcement>();
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
	public List<Announcement> getData() {	
		return data;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		String response = "";

		// Get JSON data from url
		for (String url : urls) {
			response = RestInformationDAO.getData(url);
		}

		data = new ArrayList<Announcement>();

		if (!CheckNetwork.isNetworkOnline(null)) {
			return "No connection";
		}
		
		// Decode JSON data and getting an Announcement array / object
		String jsonSource = response.replace("while(1);", "");

		try {	
			if (jsonSource.startsWith("[")) {
				JSONArray jArr = new JSONArray(jsonSource);

				for (int i = 0; i < jArr.length(); i++) {
					JSONObject obj = jArr.getJSONObject(i);

					if (obj.getString("type").compareTo("Announcement") == 0)
						data.add(convertJSONtoA(obj));
				}
			} else {
				JSONObject jObj = new JSONObject(jsonSource);
				data.add(convertJSONtoA(jObj));
			}

		} catch (JSONException e) {
			Log.e("Json", e.getMessage(), new Error());
		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyListeners();		
	}

	private Announcement convertJSONtoA(JSONObject obj) {
		Announcement announcement = new Announcement();

		try {
			announcement.setMessage(obj.getString("message"));
			
			String htmlUrl = obj.getString("html_url");
			String[] parts = htmlUrl.split("/");
			
			int i = 0;
			
			while (!parts[i].startsWith("courses"))
				i++;
			
			announcement.setCourseId(Integer.parseInt(parts[i + 1]));
			announcement.setAnnouncementId(Integer.parseInt(parts[i + 3]));
			
			if (!obj.isNull("title")) {
				announcement.setTitle(obj.getString("title"));
			} else {
				JSONObject permissions = obj.getJSONObject("permissions");
				
				if (permissions != null) {
					announcement.setTitle(permissions.getString("title"));
				}
			}
			
			if (!obj.isNull("user_name")) {
				announcement.setAuthorName(obj.getString("user_name"));
			}
			
			if (!obj.isNull("posted_at")) {
				announcement.setPostedAt(obj.getString("posted_at"));
			}
			
		} catch (JSONException e) {
			Log.e("JSON", e.getMessage());
		}

		return announcement;
	}
}
