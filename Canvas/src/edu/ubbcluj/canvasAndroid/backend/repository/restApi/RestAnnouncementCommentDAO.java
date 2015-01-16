package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
	
	private List<InformationListener> actionList;
	private String comment;

	public RestAnnouncementCommentDAO() {
		actionList = new ArrayList<InformationListener>();
		comment = new String();
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
	protected String doInBackground(String... urls) {	
		Log.d("sendmymess","doinback");
		String response = "";

		for (String url : urls) {
			response = sendComment(url);
		}

		return response;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.d("AnnounceComment", "Response: " + result);
		notifyListeners();
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String sendComment(String url) {
		String response = "";
		
		List<NameValuePair> formData = new ArrayList<NameValuePair>();
		formData.add(new BasicNameValuePair("announcement[comment]", comment));
		formData.add(new BasicNameValuePair("_method", "PUT"));
		Log.d("sendmymess",url);
		Log.d("sendmymess",comment);
		response = RestInformationDAO.postData(url, formData);
		
		return response;
	}
}
