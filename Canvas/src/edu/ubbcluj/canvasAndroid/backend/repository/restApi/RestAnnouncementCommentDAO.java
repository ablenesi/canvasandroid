package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ubbcluj.canvasAndroid.backend.repository.AnnouncementCommentDAO;
import edu.ubbcluj.canvasAndroid.backend.util.CookieHandler;
import edu.ubbcluj.canvasAndroid.backend.util.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.model.AnnouncementComment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class RestAnnouncementCommentDAO extends AsyncTask<String, Void, String> implements
		AnnouncementCommentDAO {
	
	private List<AnnouncementComment> data;
	private List<InformationListener> actionList;
	private SharedPreferences sp;

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
	public List<AnnouncementComment> getData() {
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
			if (CookieHandler.checkData(sp, url))
				response = CookieHandler.getData(sp, url);
			else
			{
				response = RestInformationDAO.getData(url);
				CookieHandler.saveData(sp, url, response);
			}
		}
		
		data = new ArrayList<AnnouncementComment>();

		if (!CheckNetwork.isNetworkOnline(null)) {
			return "No connection";
		}
		
		// Decode JSON data and getting an ActivityStream array
		String jsonSource = response.replace("while(1);", "");

		try {	
			JSONArray jArr = new JSONArray(jsonSource);

			for (int i = 0; i < jArr.length(); i++) {
				if ( isCancelled() ) break;
				JSONObject obj = jArr.getJSONObject(i);
				AnnouncementComment ac = convertJSONtoAS(obj);
				if (ac != null) {
					data.add(ac);
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
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyListeners();
	}

	// Convert JSON object to ActivityStream object
	private AnnouncementComment convertJSONtoAS(JSONObject obj) {
		AnnouncementComment ac = new AnnouncementComment();

		try {
			JSONArray participants = obj.getJSONArray("participants");
			JSONArray view = obj.getJSONArray("view");
			for(int i = 0; i<view.length();i++){
				JSONObject commentObj = view.getJSONObject(i);
				ac.setId(commentObj.getInt("id"));
				ac.setMessage(commentObj.getString("message"));
				ac.setUserId(commentObj.getInt("user_id"));
				for(int j = 0;j<participants.length();j++){
					JSONObject userObj = participants.getJSONObject(j);
					if (userObj.getInt("id") == commentObj.getInt("user_id")){
						ac.setUserName(userObj.getString("display_name"));
					}
				}
			}
		} catch (JSONException e) {
			Log.e("JSON", e.getMessage());
		}

		return ac;
	}

}
