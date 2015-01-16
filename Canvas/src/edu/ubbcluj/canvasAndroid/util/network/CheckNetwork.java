package edu.ubbcluj.canvasAndroid.util.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class CheckNetwork {
	
	public static ConnectivityManager cm = null;
	
	public static boolean isNetworkOnline(Activity activity) {
	    boolean status=false;
	    try{
	    	if (activity != null) {
	    		cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    	} else {
	    		if (cm == null) {
	    			return true;
	    		}
	    	}
	        NetworkInfo netInfo = cm.getNetworkInfo(0);
	        if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
	            status= true;
	        }else {
	            netInfo = cm.getNetworkInfo(1);
	            if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
	                status= true;
	        }
	    }catch(Exception e){
	        e.printStackTrace();  
	        return false;
	    }
	    return status;

	    }  
}
