package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.ubbcluj.canvasAndroid.controller.AnnouncementCommentController;
import edu.ubbcluj.canvasAndroid.model.AnnouncementComment;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class RestAnnouncementCommentController extends AsyncTask<String, Void, String> implements
		AnnouncementCommentController {
	
	private List<InformationListener> actionList;
	private String comment;

	public RestAnnouncementCommentController() {
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
		
		response = RestInformation.postData(url, formData);
		
		return response;
	}
}
