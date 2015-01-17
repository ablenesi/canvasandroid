package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.controller.NewMessageController;
import edu.ubbcluj.canvasAndroid.model.MessageSequence;
import edu.ubbcluj.canvasAndroid.model.Person;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;
import edu.ubbcluj.canvasAndroid.view.activity.MessageItemActivity;

public class RestNewMessageController extends AsyncTask<String, Void, String>
		implements NewMessageController {

	private String body;

	private MessageSequence data;
	
	private MessageItemActivity messageItemActivity;
	private List<InformationListener> actionList;
	
	public RestNewMessageController(){
		actionList = new ArrayList<InformationListener>();
	}
	
	@Override
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Sends data provided by the user to the given url.
	 */
	public String sendMessage(String url) {
		String response = "";

		List<NameValuePair> formData = new ArrayList<NameValuePair>();
		formData.add(new BasicNameValuePair("body", body));
		
		response = RestInformation.postData(url, formData);
		
		return response;
	}

	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		messageItemActivity.setToastMessageSending();
	}

	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected String doInBackground(String... urls) {
		String response = "";
		
		for (String url : urls) {
			response = sendMessage(url);
		}
		
		String jsonSource = response.replace("while(1);", "");

		try {
			JSONObject jObj = new JSONObject(jsonSource);

			// participants - this is in the front of the json respons. contains
			// contains id-name pairs
			JSONArray participantsArr = jObj.getJSONArray("participants");
			ArrayList<Person> participants = new ArrayList<Person>();

			for (int i = 0; i < participantsArr.length(); i++) {
				if ( isCancelled() ) break;
				JSONObject participant = participantsArr.getJSONObject(i);
				int id = participant.getInt("id");
				String name = participant.getString("name");

				participants.add(new Person(id, name));
			}

			JSONArray messagesArr = jObj.getJSONArray("messages");

			for (int i = messagesArr.length() - 1; i >= 0; i--) {
				if ( isCancelled() ) break;
				JSONObject messageObj = messagesArr.getJSONObject(i);
				data = convertJSONtoMS(messageObj, participants);
			}

		} catch (JSONException e) {
			Log.e("Json", e.getMessage(), new Error());
		}

		if ( isCancelled() ) {
			Log.d("AsyncTask", "MessageSequence asynctask cancelled");
		}
		
		return response;
	}
	
	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.MessageSequence} object.
	 */
	private MessageSequence convertJSONtoMS(JSONObject obj,
			ArrayList<Person> participants) {

		MessageSequence messageSequence = new MessageSequence();

		try {
			// messages - every message contain : id, body, created_at, author
			// (the author is an id, from the participants arraylist, so have to
			// find the corresponding name from the list)

			// message id
			messageSequence.setId(obj.getInt("id"));

			// message body
			messageSequence.setBody(obj.getString("body"));

			// message created date
			String createdAt = obj.getString("created_at");
			createdAt = createdAt.replace('T', ' ');
			createdAt = createdAt.substring(0, 16);
			messageSequence.setCreatedAt(createdAt);

			// author id and name
			int authorID = obj.getInt("author_id");
			messageSequence.setAuthorID(authorID);

			int i = 0; // we have to find the corresponding name in the
						// participants
			while (i < participants.size()
					&& participants.get(i).getId() != authorID) {
				i++;
			}
			if (i == participants.size())
				messageSequence.setName("Unknown");
			else
				messageSequence.setName(participants.get(i).getName());

		} catch (JSONException e) {
			Log.e("JSON", e.getMessage());
		}

		return messageSequence;
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
	 * Notifies listeners that the data has been retrieved from the server, and it's conversion is finished.
	 */
	public synchronized void notifyListeners() {
		for (InformationListener il : actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}

	@Override
	public void setMessageItemActivity(MessageItemActivity activity) {
		this.messageItemActivity  = activity;
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
	public MessageSequence getData() {
		return data;
	}

}
