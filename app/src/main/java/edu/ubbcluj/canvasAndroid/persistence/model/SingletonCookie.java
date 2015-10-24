package edu.ubbcluj.canvasAndroid.persistence.model;

import edu.ubbcluj.canvasAndroid.persistence.PersistentCookieStore;

public class SingletonCookie {

	private PersistentCookieStore cookieStore;
	private static SingletonCookie sCookie;
	
	private SingletonCookie() {}

	public static synchronized SingletonCookie getInstance() {
		
		if (sCookie == null) {
			sCookie = new SingletonCookie();
		}
		
		return sCookie;
	}
	
	public synchronized PersistentCookieStore getCookieStore() {
		return cookieStore;
	}

	public synchronized void setCookieStore(PersistentCookieStore cookieStore) {
		this.cookieStore = cookieStore;
	};
	
	public synchronized void deleteCookieStore() {
		if (cookieStore != null)
			cookieStore.clear();
	}
}
