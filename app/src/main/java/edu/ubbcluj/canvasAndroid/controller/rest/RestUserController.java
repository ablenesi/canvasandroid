package edu.ubbcluj.canvasAndroid.controller.rest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.ubbcluj.canvasAndroid.controller.UserController;
import edu.ubbcluj.canvasAndroid.persistence.CookieHandler;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.persistence.model.SingletonCookie;
import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.view.activity.LoginActivity;

public class RestUserController extends AsyncTask<String, Void, String> implements
		UserController {
	private PersistentCookieStore cookieStore;
	private HttpContext context;
	private HttpResponse httpResponse;
	private HttpClient httpClient;
	private LoginActivity loginActivity;
	private String usernameOriginal;
	private String username;
	private String password;
	private SharedPreferences sp;

	@Override
	public String loginUser(String host) {
		
		String response = "";

		cookieStore = new PersistentCookieStore(
				loginActivity.getSharedPreferences("CanvasAndroid",
						Context.MODE_PRIVATE));
		HttpPost httpPost = new HttpPost(host + "/login");

		context = new BasicHttpContext();

		httpClient = new DefaultHttpClient();
		httpResponse = null;

		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		nameValuePairList.add(new BasicNameValuePair(
				"pseudonym_session[unique_id]", username));
		nameValuePairList.add(new BasicNameValuePair(
				"pseudonym_session[password]", password));
		nameValuePairList.add(new BasicNameValuePair(
				"pseudonym_session[remember_me]", "1"));

		try {
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
					nameValuePairList);
			httpPost.setEntity(urlEncodedFormEntity);
		} catch (UnsupportedEncodingException e2) {
			response += e2.toString();
			Log.d("Rest", "Unsupported encode exc.! " + e2);
		}

		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		try {
			httpResponse = httpClient.execute(httpPost, context);
			response += httpResponse.getStatusLine();
		} catch (Exception e1) {
			response += e1.toString();
			Log.d("Rest", "Exception: " + e1);
		}

		return response;
	}

	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected String doInBackground(String... urls) {

		String response = "";

		for (String url : urls) {
            Log.d("Login",url);
            response = loginUser(url);
		}

		return response;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		loginActivity.showDialog("Connecting... Please wait!");
	}

	/**
	 * AsyncTask method overridden. Store cookie in SingletonCookie if logged in.
	 */
	@Override
	protected void onPostExecute(String result) {
        Log.d("Login",result);
		if (!CheckNetwork.isNetworkOnline(loginActivity)) {
			Toast.makeText(loginActivity, "No network connection!",
					Toast.LENGTH_SHORT).show();
		}
		else {
			if (!(result.compareTo("HTTP/1.1 200 OK") == 0)) {
				loginActivity.closeDialog();
				Log.d("Rest", "Login failed: " + result);
				Toast.makeText(loginActivity, "Invalid username or password!",
						Toast.LENGTH_SHORT).show();
				cookieStore.clear();
			} else {
				SingletonCookie sCookie = SingletonCookie.getInstance();
				sCookie.setCookieStore(cookieStore);

				// save username
				ArrayList<String> users = getSavedUsersArrayList();

				if (!users.contains(usernameOriginal)) {
					users.add(usernameOriginal);

					Gson gson = new Gson();
					String userString = gson.toJson(users);
					CookieHandler.saveData(sp, "usernames", userString, null);
				}
				CookieHandler.saveData(sp, "lastusername", username, null);
				loginActivity.loginCompleted();
			}
		}
	}

	public PersistentCookieStore getCookieStore() {
		return cookieStore;
	}

	public String getUsername() {
		return username;
	}

	public String getLastUsername() {
		return CookieHandler.getData(sp, "lastusername");
	}

	public void setUsername(String username) {
		this.username = username;
		this.usernameOriginal = username;
		if (!username.contains("@")) {
			this.username = username.concat(PropertyProvider
					.getProperty("default_email"));
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LoginActivity getLoginActivity() {
		return loginActivity;
	}

	public void setLoginActivity(LoginActivity loginActivity) {
		this.loginActivity = loginActivity;
	}

	public void setSharedPreferences(SharedPreferences sp) {
		this.sp = sp;
	}

	public ArrayAdapter<String> getSavedUsersAdapter() {

		return new ArrayAdapter<String>(loginActivity,
				android.R.layout.simple_dropdown_item_1line,
				getSavedUsersArrayList());

	}

	private ArrayList<String> getSavedUsersArrayList() {
		ArrayList<String> arrayList = new ArrayList<String>();

		if (CookieHandler.checkData(sp, "usernames")) {
			String usernames = CookieHandler.getData(sp, "usernames");

			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<String>>() {
			}.getType();
			arrayList = gson.fromJson(usernames, type);
		}

		return arrayList;
	}
}
