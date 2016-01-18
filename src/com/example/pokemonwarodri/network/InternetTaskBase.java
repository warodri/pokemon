package com.example.pokemonwarodri.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.w3c.dom.Document;

import android.util.Log;

/**
 * Created by Walter on 10/12/2014.
 */
public class InternetTaskBase {

	public static final String TAG = "Internet";

	public String readIS(InputStream is) {
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			return total.toString();
		} catch (Exception e) {
			Log.wtf(TAG, e);
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Handler for XML download callbacks
	 */
	public interface XmlResponseHandler {
		public void onXmlReceived(Document xmlDoc);
	}
	
}
