package edu.ubbcluj.canvasAndroid.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie2;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import edu.ubbcluj.canvasAndroid.persistence.model.SerializableCookie;

public class PersistentCookieStore implements CookieStore, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String cookie_store_name = "CanvasData"; 
	private static final String cookie_prefix = "canvasCookie_";
	private final SharedPreferences cookiePreferences;
	private final List<String> persistentCookieNames;
	
	public PersistentCookieStore(SharedPreferences settings) {
		cookiePreferences = settings;
		persistentCookieNames = new ArrayList<String>();
		
		String storedCookieNames = cookiePreferences.getString(cookie_store_name, null);
		if (storedCookieNames != null) {
		    String[] cookieNames = TextUtils.split(storedCookieNames, ",");
			
		    for (String name : cookieNames) {
			    persistentCookieNames.add(name);
			}
		    
			//Clear out expired cookies
		    clearExpired(new Date());		    
		}		 
	}

	/**
	 * Save a new cookie to SavedPreferences Data encoded using Base64.
	 */
	@Override
	public synchronized void addCookie(Cookie cookie) {
		final SharedPreferences.Editor editor = cookiePreferences.edit();
		String cookieName = cookie.getName();
		
		//Add cookieName to persistentCookieNames
		persistentCookieNames.add(cookieName);
		
		try {
			editor.putString(cookie_store_name,TextUtils.join(",",persistentCookieNames));
			editor.putString(cookie_prefix + cookieName,encodeCookie(new SerializableCookie(cookie)));
		} catch (Exception error) {
			Log.e("CookieStore", error.toString());
		}
		editor.commit();
	}

	@Override
	public synchronized List<Cookie> getCookies() {
		//Clear out expired cookies
	    clearExpired(new Date());	
	    
	    List<Cookie> decodedCookies = new ArrayList<Cookie>();
		String storedCookieNames = cookiePreferences.getString(cookie_store_name, null);
		
		if (storedCookieNames != null) {
		    String[] cookieNames = TextUtils.split(storedCookieNames, ",");	    
			
		    for (String name : cookieNames) {
			    String encodedCookie = cookiePreferences.getString(cookie_prefix + name, null);
			    if (encodedCookie != null) {
			        Cookie decodedCookie = decodeCookie(encodedCookie);
			        if (decodedCookie != null) {
			            decodedCookies.add(decodedCookie);
			        }
			    }
			}
		}
		
		return decodedCookies;
	}
	
	/**
	 * Get a cookie by key.
	 */
	public synchronized Cookie getCookie(String key) {
		//Clear out expired cookies
	    clearExpired(new Date());	
	    
		String encodedCookie = cookiePreferences.getString(cookie_prefix + key, null);
		 
		 if (encodedCookie != null) {
	        Cookie decodedCookie = decodeCookie(encodedCookie);
	        if (decodedCookie != null) {
	            return decodedCookie;
	        }			 
		 }  
	
		 return new BasicClientCookie2("Epmty","");
	}
	
	/**
	 * Clear the expired cookies.
	 */
	@Override
	public synchronized boolean clearExpired(Date date) {
        boolean clearedAny = false;
        final SharedPreferences.Editor editor = cookiePreferences.edit();
		String storedCookieNames = cookiePreferences.getString(cookie_store_name, null);

		if (storedCookieNames != null) {
			String[] cookieNames = TextUtils.split(storedCookieNames, ",");
		    for (String name : cookieNames) {
			    String encodedCookie = cookiePreferences.getString(cookie_prefix + name, null);
			    if (encodedCookie != null) {
			        Cookie decodedCookie = decodeCookie(encodedCookie);
			        if (decodedCookie != null) {
			        	if (decodedCookie.isExpired(date)) {
			        		editor.remove(cookie_prefix + name);
			        		persistentCookieNames.remove(name);
	
			        		clearedAny = true;
			        	}
			        }
			    }
		    }    
		}
		
		if (clearedAny) {
			editor.putString(cookie_store_name,TextUtils.join(",",persistentCookieNames));
		}
		
		editor.commit();
		
		return clearedAny;
	}	
	
	/**
	 * Clear all cookies.
	 */
	@Override
	public synchronized void clear() {
		final SharedPreferences.Editor editor = cookiePreferences.edit();
		for (String cookieName:persistentCookieNames) {
			editor.remove(cookie_prefix + cookieName);
		}
		
		editor.remove(cookie_store_name);
		editor.commit();
		
		persistentCookieNames.clear();
	}	
	
	public void removeFromCookiePreferences(String key) {
		final SharedPreferences.Editor editor = cookiePreferences.edit();
		editor.remove(cookie_prefix + key);
		editor.commit();
	}
	
	public void clearSharedPreferences() {
		final SharedPreferences.Editor editor = cookiePreferences.edit();
		editor.clear();
		editor.commit();		
	}
	
	/**
	 * Check if a cookie exists.
	 */
	public synchronized boolean existCookie(String key) {
		//Clear out expired cookies
	    clearExpired(new Date());			
	    return (persistentCookieNames.contains(key));
	}
	
	/**
	 * Encode the serializable cookie.
	 */
    protected String encodeCookie(SerializableCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (Exception e) {
            return null;
        }

        return Base64.encodeToString(os.toByteArray(),Base64.DEFAULT);
    }
    
    /**
     * Decode the serializable cookie.
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes =  Base64.decode(cookieString,Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableCookie) objectInputStream.readObject()).getCookie();
        } catch (Exception exception) {
            Log.d("CookieStore", "Failed to decode the cookie", exception);
        }

        return cookie;
    }
}