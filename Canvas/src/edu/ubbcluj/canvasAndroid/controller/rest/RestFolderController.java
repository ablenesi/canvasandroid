package edu.ubbcluj.canvasAndroid.controller.rest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.controller.FolderController;
import edu.ubbcluj.canvasAndroid.model.File;
import edu.ubbcluj.canvasAndroid.model.FileTreeElement;
import edu.ubbcluj.canvasAndroid.model.Folder;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.util.listener.InformationEvent;
import edu.ubbcluj.canvasAndroid.util.listener.InformationListener;

public class RestFolderController extends AsyncTask<String, Void, String> implements
		FolderController {

	private List<FileTreeElement> data;
	private List<InformationListener> actionList;
	private SharedPreferences sp;

	public RestFolderController() {
		super();
		data = new ArrayList<FileTreeElement>();

		data.add(null);

		actionList = new ArrayList<InformationListener>();

	}

	@Override
	public void addInformationListener(InformationListener il) {
		this.actionList.add(il);
	}

	@Override
	public void removeInformationListener(InformationListener il) {
		actionList.remove(il);
	}

	@Override
	public List<FileTreeElement> getData() {
		return data;
	}

	@Override
	public void setSharedPreferences(SharedPreferences sp) {
		this.sp = sp;
	}

	@Override
	public void clearData() {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sp);
		persistentCookieStore.clear();
	}

	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected String doInBackground(String... urls) {

		String[] response = new String[2];

		int k = 0;
		for (String url : urls) {
			if (CookieHandler.checkData(sp, url)) {
				response[k] = CookieHandler.getData(sp, url);
			} else {
				response[k] = RestInformation.getData(url);
				CookieHandler.saveData(sp, url, response[k]);
			}
			k++;
		}

		// Convert Folders Json
		String jsonSource = response[0].replace("while(1);", "");

		try {
			if (jsonSource.startsWith("[")) {
				JSONArray jArr = new JSONArray(jsonSource);

				for (int i = 0; i < jArr.length(); i++) {
					if (isCancelled())
						break;
					JSONObject obj = jArr.getJSONObject(i);
					data.add(convertJSONtoFolder(obj));
				}
			} else {
				JSONObject jObj = new JSONObject(jsonSource);
				data.add(convertJSONtoFolder(jObj));
			}

		} catch (JSONException e) {
			Log.e("Json", e.getMessage(), new Error());
		}

		if (urls.length == 2) {
			// Convert Files Json
			jsonSource = response[1].replace("while(1);", "");

			try {
				if (jsonSource.startsWith("[")) {
					JSONArray jArr = new JSONArray(jsonSource);

					for (int i = 0; i < jArr.length(); i++) {
						if (isCancelled())
							break;
						JSONObject obj = jArr.getJSONObject(i);
						data.add(convertJSONtoFile(obj));
					}
				} else {
					JSONObject jObj = new JSONObject(jsonSource);
					data.add(convertJSONtoFile(jObj));
				}

			} catch (JSONException e) {
				Log.e("Json", e.getMessage(), new Error());
			}

		}

		if (isCancelled()) {
			Log.d("AsyncTask", "Folder asynctask cancelled");
		}

		return null;
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
	 * AsyncTask method overridden.
	 */
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		notifyListeners();
	}

	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.Folder} object.
	 */
	private Folder convertJSONtoFolder(JSONObject obj) {

		Folder folder = new Folder();

		try {
			folder.setId(obj.getInt("id"));
			folder.setName(obj.getString("name"));
			folder.setFoldersUrl(obj.getString("folders_url"));
			folder.setFilesUrl(obj.getString("files_url"));
		} catch (JSONException e) {
			Log.e("JSON Folder", e.getMessage());
		}

		return folder;
	}

	/**
	 * Converts a JSON Object to an {@link edu.ubbcluj.canvasAndroid.model.File} object.
	 */
	private File convertJSONtoFile(JSONObject obj) {
		File file = new File();

		try {
			file.setId(obj.getInt("id"));
			file.setName(obj.getString("display_name"));
			file.setUrl(obj.getString("url"));
		} catch (JSONException e) {
			Log.e("JSON File", e.getMessage());
		}

		return file;
	}

}
