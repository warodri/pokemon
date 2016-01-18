package com.example.pokemonwarodri.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * We place here all the static varialbes used in this app.
 * @author Walter A. Rodriguez
 *
 */
public class StaticData {

	/**
	 * Tag to show on logs.
	 */
	private static final String LOG_TAG = "StaticData";

	/**
	 * Base Server URL.
	 */
	public static final String SERVER_API = "http://pokeapi.co";
	
	/**
	 * Base Server URL for API requests.
	 */
	public static final String SERVER_BASE = SERVER_API + "/api/v1/";

	
	/**
	 * Get Pokemon list
	 */
	public static final String POKEMON_LIST = "pokedex/1/";

	/**
	 * Checks if a valid Internet connection is established for this device.
	 * @param context : app context.
	 * @return : true / false if Internet is available.
	 */
	public static boolean hasActiveInternetConnection(Context context) {
	    if (isNetworkAvailable(context)) {
	        try {
	            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
	            urlc.setRequestProperty("User-Agent", "Test");
	            urlc.setRequestProperty("Connection", "close");
	            urlc.setConnectTimeout(1500); 
	            urlc.connect();
	            return (urlc.getResponseCode() == 200);
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "Error checking internet connection", e);
	        }
	    } else {
	        Log.d(LOG_TAG, "No network available!");
	    }
	    return false;
	}
	
	/**
	 * Checks if we have a network connection on this device.
	 * @param context : app context.
	 * @return true / false if we are connected to mobile network (well, it's part of the internet)
	 */
	private static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	         = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
}
