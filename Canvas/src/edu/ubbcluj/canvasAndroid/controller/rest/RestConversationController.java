package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.controller.ConversationController;
import edu.ubbcluj.canvasAndroid.model.Conversation;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public class RestConversationController extends AsyncTask<String, Void, String>
		implements ConversationController {

	private List<Conversation> data;
	private List<InformationListener> actionList;
	private SharedPreferences sp;

	public RestConversationController() {
		super();
		data = new ArrayList<Conversation>();
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
	public List<Conversation> getData() {
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
	
	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected String doInBackground(String... urls) {

		String response = "";

		// Get JSON data from url
		for (String url : urls) {
			if (CookieHandler.checkData(sp, url))
				response = CookieHandler.getData(sp, url);
			else
			{
				response = RestInformation.getData(url);
				CookieHandler.saveData(sp, url, response);
			}
		}

		data = new ArrayList<Conversation>();

		// Decode JSON data and getting an ActivityStream array
		String jsonSource = response.replace("while(1);", "");

		try {
			JSONArray jArr = new JSONArray(jsonSource);

			for (int i = 0; i < jArr.length(); i++) {
				if ( isCancelled() ) break;
				JSONObject obj = jArr.getJSONObject(i);
				data.add(convertJSONtoAS(obj));
			}

		} catch (JSONException e) {
			Log.e("Json", e.getMessage(), new Error());
		}

		if ( isCancelled() ) {
			Log.d("AsyncTask", "Conversation asynctask cancelled");
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
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.Conversation} object.
	 */
	private Conversation convertJSONtoAS(JSONObject obj) {
		Conversation conversation = new Conversation();

		try {
			// id
			conversation.setId(obj.getInt("id"));

			// last message date
			String dateTime = obj.getString("last_message_at").substring(0, 16);
			String lastMessageDate = dateTime.substring(0, 10);
			String lastMessageClock = dateTime.substring(11, 16);
			conversation.setDate(lastMessageDate);
			conversation.setClock(lastMessageClock);

			// participants
			JSONArray participantsArr = obj.getJSONArray("participants");

			for (int i = 0; i < participantsArr.length(); i++) {
				JSONObject participant = participantsArr.getJSONObject(i);
				int id = participant.getInt("id");
				String name = participant.getString("name");

				conversation.addParticipant(id, name);
			}

			// last message
			String lastMessage = obj.getString("last_message");
			if (lastMessage.length() > 50) {
				lastMessage = lastMessage.substring(0, 50);
				lastMessage += " ...";
			}
			lastMessage = lastMessage.replace('\n', ' ');
			conversation.setLastMessage(lastMessage);

		} catch (JSONException e) {
			Log.e("JSON", e.getMessage());
		}

		return conversation;
	}

}
