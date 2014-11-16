package edu.ubbcluj.canvasAndroid.backend.repository.restApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;
import edu.ubbcluj.canvasAndroid.backend.util.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonSharedPreferences;
import edu.ubbcluj.canvasAndroid.backend.util.network.CheckNetwork;
import edu.ubbcluj.canvasAndroid.backend.util.network.RestHttpClient;

public class RestInformationDAO {
	
	//Get informations from the given url
	public static String getData(String url) {
		HttpContext context;
		HttpResponse httpResponse;
		HttpClient httpClient;
		
		//ide kerult az url
		HttpGet httpget = new HttpGet(url);
		ResponseHandler<String> handler = new BasicResponseHandler();
		
		
		// Check if network connection is available
		if (!CheckNetwork.isNetworkOnline(null))
			return "No connection";
		
		//Get infromation from the server
		httpClient =  RestHttpClient.getNewHttpClient();
		context = new BasicHttpContext();
		httpResponse = null;
		
		String response = "";
		
		try {
			BasicCookieStore cookieStore = new BasicCookieStore();
			List<Cookie> loginCookies = getLoginCookies();
			
			for (Cookie c : loginCookies) {
				cookieStore.addCookie(c);
			}
			
			//context.setAttribute(ClientContext.COOKIE_STORE, SingletonCookie.getInstance().getCookieStore());
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			httpResponse = httpClient.execute(httpget, context);
			
			//get keres
			response = handler.handleResponse(httpResponse);
		} catch (ClientProtocolException e) {
			Log.e("Data transfer error", e.getMessage());
		} catch (IOException e) {
			Log.e("IO error", e.getLocalizedMessage());
		}
		
		return response;
	}
	
	/*
	 * Clears all cookies from cookie store except the login cookies 
	 * (_normandy_session, pseudonym_credentials)
	 */
//	public static void clearData() {
//		SingletonSharedPreferences sPreferences = SingletonSharedPreferences.getInstance();
//		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(sPreferences.getSharedPreferences());
//		
//		List<Cookie> loginCookies = getLoginCookies();
//		
//		// Delete all cookies
//		persistentCookieStore.clear();
//		
//		// Re-add the login cookies to the cookie store
//		for (Cookie c : loginCookies) {
//			persistentCookieStore.addCookie(c);
//		}
//	}
	
	/*
	 * Get cookies needed to user authorization
	 */
	private static List<Cookie> getLoginCookies() {
		SingletonSharedPreferences sPreferences = SingletonSharedPreferences.getInstance();
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(sPreferences.getSharedPreferences());
		
		List<Cookie> cookies = persistentCookieStore.getCookies();
		List<Cookie> loginCookies = new ArrayList<Cookie>();
		
		// Iterate through all cookies, save the login cookies into list "loginCookies"
		for (Cookie c : cookies) {
			if ((c.getName().startsWith("_normandy_session")) || (c.getName().startsWith("pseudonym_credentials"))) {
				loginCookies.add(c);
			}
		}
		
		return loginCookies;
	}
}
