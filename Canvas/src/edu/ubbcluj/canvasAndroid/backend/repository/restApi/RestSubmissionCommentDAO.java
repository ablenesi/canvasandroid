package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.backend.repository.SubmissionCommentDAO;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;

public class RestSubmissionCommentDAO extends AsyncTask<String, Void, String> implements SubmissionCommentDAO {

	private List<InformationListener> actionList;

	private String comment;
	
	
	public RestSubmissionCommentDAO() {
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

	@Override
	protected String doInBackground(String... urls) {	
		String response = "";

		for (String url : urls) {
			response = sendComment(url);
		}

		return response;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.d("SubmissionComment", "Response: " + result);
		notifyListeners();
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String sendComment(String url) {
		String response = "";
		
		List<NameValuePair> formData = new ArrayList<NameValuePair>();
		formData.add(new BasicNameValuePair("submission[comment]", comment));
		formData.add(new BasicNameValuePair("_method", "PUT"));
		
		response = RestInformationDAO.postData(url, formData);
		
		return response;
	}
	
	public synchronized void notifyListeners() {
		for (InformationListener il: actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}	

}
