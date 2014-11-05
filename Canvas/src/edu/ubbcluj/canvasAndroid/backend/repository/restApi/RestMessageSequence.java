package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.backend.repository.MessageSequenceDAO;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationEvent;
import edu.ubbcluj.canvasAndroid.backend.util.informListener.InformationListener;
import edu.ubbcluj.canvasAndroid.model.MessageSequence;
import edu.ubbcluj.canvasAndroid.model.Person;

public class RestMessageSequence extends AsyncTask<String, Void, String>
		implements MessageSequenceDAO {
	private List<MessageSequence> data;
	private List<InformationListener> actionList;

	public RestMessageSequence() {
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
	protected String doInBackground(String... urls) {

		String response = "";

		// Get JSON data from url
		for (String url : urls) {
			response = RestInformationDAO.getData(url);
		}

		data = new ArrayList<MessageSequence>();

		// Decode JSON data and getting an ActivityStream array
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

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyListeners();
	}

	public synchronized void notifyListeners() {
		for (InformationListener il : actionList) {
			il.onComplete(new InformationEvent(this));
		}
	}

	// Convert JSON object to MessageSequence object
	private MessageSequence convertJSONtoMS(JSONObject obj,
			ArrayList<Person> participants) {

		MessageSequence messageSequence = new MessageSequence();

		try {
			messageSequence.setName("fuck");
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
