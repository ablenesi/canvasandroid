package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import edu.ubbcluj.canvasAndroid.LoginActivity;
import edu.ubbcluj.canvasAndroid.backend.repository.UserDAO;
import edu.ubbcluj.canvasAndroid.backend.util.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.backend.util.PropertyProvider;
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonCookie;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.backend.util.network.RestHttpClient;

public class RestUserDAO extends AsyncTask<String, Void, String> implements UserDAO {
	private PersistentCookieStore cookieStore;
	private HttpContext context;
	private HttpResponse httpResponse;
	private HttpClient httpClient;
	private LoginActivity loginActivity;
	private String username;
	private String password;

	// Try to login
	@Override
	public String loginUser(String host) {
		String response = "";
		
		cookieStore = new PersistentCookieStore(loginActivity.getSharedPreferences("CanvasAndroid",
				Context.MODE_PRIVATE));
		HttpPost httpPost = new HttpPost(host + "/login");

		context = new BasicHttpContext();
		httpClient = RestHttpClient.getNewHttpClient();
		httpResponse = null;
		
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		nameValuePairList.add(new BasicNameValuePair("pseudonym_session[unique_id]", username));
		nameValuePairList.add(new BasicNameValuePair("pseudonym_session[password]", password));
		nameValuePairList.add(new BasicNameValuePair("pseudonym_session[remember_me]","1"));
		
		try {
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);
			httpPost.setEntity(urlEncodedFormEntity);
		} catch (UnsupportedEncodingException e2) {
			response += e2.toString();
		}

		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		try {
			httpResponse = httpClient.execute(httpPost, context);
			response += httpResponse.getStatusLine();
		} catch (IOException e1) {
			response += e1.toString();
		}

		return response;
	}

	@Override
	protected String doInBackground(String... urls) {

		String response = "";

		for (String url : urls) {
			response = loginUser(url);
		}

		return response;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		loginActivity.showDialog("Connecting... Please wait!");
	}

	// Store cookie in SingletonCookie if logged in
	// Else show the errorMessage
	@Override
	protected void onPostExecute(String result) {
		loginActivity.closeDialog();

		if (!CheckNetwork.isNetworkOnline(loginActivity))
			Toast.makeText(loginActivity, "No network connection!",
					Toast.LENGTH_SHORT).show();
		else {
			if (!(result.compareTo("HTTP/1.1 200 OK") == 0)) {
				Toast.makeText(loginActivity, "Invalid username or password!",
						Toast.LENGTH_SHORT).show();
				cookieStore.clear();
			} else {
				SingletonCookie sCookie = SingletonCookie.getInstance();
				sCookie.setCookieStore(cookieStore);
				loginActivity.redirect();
			}
		}
	}

	public PersistentCookieStore getCookieStore() {
		return cookieStore;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		if (!username.contains("@")) {
			this.username = username.concat(PropertyProvider.getProperty("default_email"));
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
}
