package edu.ubbcluj.canvasAndroid.backend.util;

import java.util.Date;

import org.apache.http.impl.cookie.BasicClientCookie2;

import android.content.SharedPreferences;

public class CookieHandler {
	public static boolean checkData(SharedPreferences sp, String url) {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sp);

		return persistentCookieStore.existCookie(url);
	}

	public static String getData(SharedPreferences sp, String url) {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sp);
		BasicClientCookie2 dataStoreCookie = (BasicClientCookie2) persistentCookieStore
				.getCookie(url);

		return dataStoreCookie.getValue();
	}

	public static void saveData(SharedPreferences sp, String url, String data) {
		// ez a cookie elmentese
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
	
	public static void saveData(SharedPreferences sp, String url, String data, Date expiryDate) {
		PersistentCookieStore persistentCookieStore = new PersistentCookieStore(
				sp);

		BasicClientCookie2 dataStoreCookie = new BasicClientCookie2(url, data);
		dataStoreCookie.setDomain(PropertyProvider.getProperty("domain"));
		dataStoreCookie.setExpiryDate(expiryDate);

		persistentCookieStore.addCookie(dataStoreCookie);
	}
}
