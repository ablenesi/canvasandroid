package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.controller.MessageSequenceController;
import edu.ubbcluj.canvasAndroid.model.MessageSequence;
import edu.ubbcluj.canvasAndroid.model.Person;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public class RestMessageSequenceController extends AsyncTask<String, Void, String>
		implements MessageSequenceController {
	private List<MessageSequence> data;
	private List<InformationListener> actionList;
	private SharedPreferences sp;

	public RestMessageSequenceController() {
		super();
		data = new ArrayList<MessageSequence>();
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

	@Override
	public List<MessageSequence> getData() {
		return data;
	}

	@Override
	public void setSharedPreferences(SharedPreferences sp) {
		this.sp = sp;
	}
	
	/**
	 * AsyncTask method overridden.
	 */
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
//			if (CookieHandler.checkData(sp, url))
//				response = CookieHandler.getData(sp, url);
//			else
//			{
				response = RestInformation.getData(url);
				CookieHandler.saveData(sp, url, response);
//			}
		}

		data = new ArrayList<MessageSequence>();

		// Decode JSON data and getting an ActivityStream array
		String jsonSource = response.replace("while(1);", "");

		try {
			JSONObject jObj = new JSONObject(jsonSource);

			// participants - this is in the front of the json response
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
				data.add(convertJSONtoMS(messageObj, participants));
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

	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.Folder} object.
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
}
