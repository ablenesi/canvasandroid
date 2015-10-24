package edu.ubbcluj.canvasAndroid.persistence;

import java.util.Date;

import org.apache.http.impl.cookie.BasicClientCookie2;

import edu.ubbcluj.canvasAndroid.util.PropertyProvider;
import android.content.SharedPreferences;

public class CookieHandler {
	/**
	 * Checks if the given SharedPreferences object contains data for the given url.
	 */
	public static boolean checkData(SharedPreferences sp, String url) {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sp);

		return persistentCookieStore.existCookie(url);
	}

	/**
	 * Get data from the given SharedPreferences object for the given url.
	 */
	public static String getData(SharedPreferences sp, String url) {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sp);
		BasicClientCookie2 dataStoreCookie = (BasicClientCookie2) persistentCookieStore
				.getCookie(url);

		return dataStoreCookie.getValue();
	}

	/**
	 * Save the given data to the given SharedPreferences object, for the given url.
	 */
	public static void saveData(SharedPreferences sp, String url, String data) {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sp);

		BasicClientCookie2 dataStoreCookie = new BasicClientCookie2(url, data);
		Date nowDate = new Date();

		// Set expiryDate +1 Minutes
		int cacheTime = Integer.parseInt(PropertyProvider
				.getProperty("cacheTime")) * 1000;
		nowDate.setTime(nowDate.getTime() + cacheTime);
		dataStoreCookie.setDomain(PropertyProvider.getProperty("domain"));
		dataStoreCookie.setExpiryDate(nowDate);

		persistentCookieStore.addCookie(dataStoreCookie);
	}
	
	/**
	 * Save the given data with an expiry date to the given SharedPreferences object, for the given url.
	 */
	public static void saveData(SharedPreferences sp, String url, String data, Date expiryDate) {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sp);

		BasicClientCookie2 dataStoreCookie = new BasicClientCookie2(url, data);
		dataStoreCookie.setDomain(PropertyProvider.getProperty("domain"));
		dataStoreCookie.setExpiryDate(expiryDate);

		persistentCookieStore.addCookie(dataStoreCookie);
	}
}
