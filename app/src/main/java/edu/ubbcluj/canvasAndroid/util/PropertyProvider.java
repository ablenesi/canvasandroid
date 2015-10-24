package edu.ubbcluj.canvasAndroid.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertyProvider {
	private static ResourceBundle bundle = 
			ResourceBundle.getBundle("edu.ubbcluj.canvasAndroid.res.config");
	
	public static String getProperty(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";
		}
	}
}
