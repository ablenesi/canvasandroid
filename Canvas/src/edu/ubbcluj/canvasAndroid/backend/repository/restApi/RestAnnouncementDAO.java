package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.backend.repository.AnnouncementDAO;
import edu.ubbcluj.canvasAndroid.backend.util.CookieHandler;
import edu.ubbcluj.canvasAndroid.backend.util.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.Announcement;
import edu.ubbcluj.canvasAndroid.model.AnnouncementComment;

public class RestAnnouncementDAO extends AsyncTask<String, Void, String>
		implements AnnouncementDAO {

	private List<Announcement> data;
	private List<InformationListener> actionList;
	private SharedPreferences sp;

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
		for (InformationListener il : actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}

	@Override
	public List<Announcement> getData() {
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

		data = new ArrayList<Announcement>();

		// Decode JSON data and getting an Announcement array / object
		String jsonSource = response.replace("while(1);", "");

		try {
			if (jsonSource.startsWith("[")) {
				JSONArray jArr = new JSONArray(jsonSource);

				for (int i = 0; i < jArr.length(); i++) {
					if ( isCancelled() ) break;
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

		if ( isCancelled() ) {
			Log.d("AsyncTask", "Announcement asynctask cancelled");
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
			
			if (!obj.isNull("read_state")) {
				announcement.setRead_state(obj.getString("read_state").equals("read")||obj.getString("read_state").equals("true")?true:false);
			}
			
			String url = PropertyProvider.getProperty("url")
					+ "/api/v1/courses/" + announcement.getCourseId() + "/discussion_topics/"
					+ announcement.getAnnouncementId() + "/view";
			Log.d("t",url);
			String response = RestInformationDAO.getData(url).replace(
					"while(1);", "");
			Log.d("t",response);
			JSONObject annCommObj = new JSONObject(response);
			announcement.setAc(convertJSONtoAC(annCommObj));

		} catch (JSONException e) {
			Log.e("JSON", e.getMessage());
		}
		
		return announcement;
	}

	//!!!!!!!!!!!!
	private AnnouncementComment[] convertJSONtoAC(JSONObject obj) {
		Log.d("t", "convert");
		AnnouncementComment[] ac = new AnnouncementComment[100];
		try {
			ac = new AnnouncementComment[obj.getJSONArray("view").length()];
			Log.d("t","ac hossz: "+ac.length);
			Log.d("t", "convert0");
			JSONArray participants = obj.getJSONArray("participants");
			Log.d("t","p hossz: "+participants.length());
			Log.d("t", "convert00");
			JSONArray view = obj.getJSONArray("view");
			Log.d("t","v hossz: "+view.length());
			Log.d("t", "convert000");
			for(int i = 0; i<view.length();i++){
				ac[i] = new AnnouncementComment();
				JSONObject commentObj = view.getJSONObject(i);
				Log.d("t", "convertw0"+i);
				Log.d("t","obj "+ commentObj.toString());
				String mess = commentObj.getString("message");
				Log.d("t","mess "+mess.substring(3, mess.length()-4));
				ac[i].setMessage(mess.substring(3, mess.length()-4));
				Log.d("t", "convertw00"+i);
				ac[i].setId(commentObj.getInt("id"));
				Log.d("t", "convertw000"+i);
				ac[i].setUserId(commentObj.getInt("user_id"));
				Log.d("t", "convertw0000"+i);
				for(int j = 0;j<participants.length();j++){
					Log.d("t", "convert"+i);
					JSONObject userObj = participants.getJSONObject(j);
					if (userObj.getInt("id") == commentObj.getInt("user_id")){
						ac[i].setUserName(userObj.getString("display_name"));
					}
				}
			}
		} catch (JSONException e) {
			Log.d("t", "baj van!!!!");
			Log.e("JSON", e.getMessage());
		}

		return ac;
	}
	
}
