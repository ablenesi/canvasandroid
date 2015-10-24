package edu.ubbcluj.canvasAndroid.controller.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.persistence.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.util.network.CheckNetwork;

public class RestInformation extends AsyncTask<String, Void, String> {

	/**
	 * Sends data provided by the user to the given url with post method.
	 */
	public static String postData(String url, List<NameValuePair> formData) {
		String response = "";
		
		HttpContext context = null;
		HttpClient httpClient = null;
		HttpResponse httpResponse = null;
		HttpPost httpPost = new HttpPost(url);
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		if (!CheckNetwork.isNetworkOnline(null))
			return "No connection";
		
		try {
			httpClient = new DefaultHttpClient();
			
			BasicCookieStore cookieStore = new BasicCookieStore();
			List<Cookie> loginCookies = getLoginCookies();

			for (Cookie c : loginCookies) {
				cookieStore.addCookie(c);
			}
			
			context = new BasicHttpContext();
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
					formData, "UTF-8");
			httpPost.setEntity(urlEncodedFormEntity);
			
			httpResponse = httpClient.execute(httpPost, context);
			response = handler.handleResponse(httpResponse);
		} catch (ClientProtocolException e) {
			Log.e("Data transfer error", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IO error", e.getLocalizedMessage());
			e.printStackTrace();
		} 
		
		return response;
	}
	
	/**
	 * Sends data provided by the user to the given url with put method.
	 */
	public static String putData(String url){
		HttpContext context;
		HttpClient httpClient;
		HttpPut httpput = new HttpPut(url);
		httpClient = new DefaultHttpClient();
		ResponseHandler<String> handler = new BasicResponseHandler();
		String resp = "";
		
		try {
			BasicCookieStore cookieStore = new BasicCookieStore();
			List<Cookie> loginCookies = getLoginCookies();

			for (Cookie c : loginCookies) {
				cookieStore.addCookie(c);
			}
			
			context = new BasicHttpContext();
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		    // Execute HTTP Put Request
			Log.d("Rest",httpput.getMethod());
			httpput.setEntity(null);
		    HttpResponse response = httpClient.execute(httpput,context);
		    HttpEntity entity = response.getEntity();
		    Log.d("Rest",EntityUtils.toString(entity));
		    
		    resp = handler.handleResponse(response);
		} catch (ClientProtocolException e) {
			Log.e("Data transfer error", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IO error", e.getLocalizedMessage());
			e.printStackTrace();
		} 
		return resp;
	}
	
	/**
	 * Retrieves data from the given url.
	 */
	public static String getData(String url) {
		HttpContext context;
		HttpResponse httpResponse;
		HttpClient httpClient;

		HttpGet httpget = new HttpGet(url);
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		String response = "";

		try {
			// Get infromation from the server
			httpClient = new DefaultHttpClient();
			context = new BasicHttpContext();
			httpResponse = null;

			BasicCookieStore cookieStore = new BasicCookieStore();
			List<Cookie> loginCookies = getLoginCookies();

			for (Cookie c : loginCookies) {
				cookieStore.addCookie(c);
			}

			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			httpResponse = httpClient.execute(httpget, context);

			response = handler.handleResponse(httpResponse);
		} catch (ClientProtocolException e) {
			Log.e("Data transfer error", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IO error", e.getLocalizedMessage());
			e.printStackTrace();
		} 

		return response;
	}

	/**
	 * Clears all cookies from cookie store except the login cookies.
	 */
	//(_normandy_session, pseudonym_credentials)
	public static void clearData() {
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sPreferences.getSharedPreferences());

		List<Cookie> loginCookies = getLoginCookies();

		// Delete all cookies
		persistentCookieStore.clear();
		
		// Re-add the login cookies to the cookie store
		for (Cookie c : loginCookies) {
			persistentCookieStore.addCookie(c);
		}
		
	}

	/**
	 * Get cookies needed to user authorization.
	 */
	private static List<Cookie> getLoginCookies() {
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences
				.getInstance();
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sPreferences.getSharedPreferences());

		List<Cookie> cookies = persistentCookieStore.getCookies();
		List<Cookie> loginCookies = new ArrayList<Cookie>();

		// Iterate through all cookies, save the login cookies into list
		// "loginCookies"
		for (Cookie c : cookies) {
			if ((c.getName().startsWith("_normandy_session"))
					|| (c.getName().startsWith("pseudonym_credentials"))) {
				loginCookies.add(c);
			}
		}

		return loginCookies;
	}

	/**
	 * AsyncTask method overridden.
	 */
	@Override
	protected String doInBackground(String... urls) {
		String response = "";
		for (String url : urls) {
			response = RestInformation.getData(url);
		}
		return response;
	}
}
