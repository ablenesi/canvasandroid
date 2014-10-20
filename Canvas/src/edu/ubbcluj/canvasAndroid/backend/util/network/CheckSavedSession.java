package edu.ubbcluj.canvasAndroid.backend.util.network;

import android.content.Context;
import android.os.AsyncTask;
import edu.ubbcluj.canvasAndroid.LoginActivity;
import edu.ubbcluj.canvasAndroid.backend.util.PersistentCookieStore;
import edu.ubbcluj.canvasAndroid.backend.util.model.SingletonCookie;

public class CheckSavedSession extends AsyncTask<LoginActivity, Void, Void> {

	private PersistentCookieStore cookieStore;
	private LoginActivity activity;
	private boolean cookieIsAvailable = false;
	
	@Override
	protected Void doInBackground(LoginActivity... activities) {
		
		for (LoginActivity activity :  activities) {
			this.activity = activity;
			cookieStore = new PersistentCookieStore(activity.getSharedPreferences("CanvasAndroid", Context.MODE_PRIVATE));
			
			if (cookieStore.getCookies().size() != 0) {
				SingletonCookie.getInstance().setCookieStore(cookieStore);
				cookieIsAvailable = true;
				
				return null;
			}
			
			return null;
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if (cookieIsAvailable) {
			activity.redirect();
		}
	}

	
}
