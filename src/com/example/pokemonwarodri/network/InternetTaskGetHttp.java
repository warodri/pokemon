package com.example.pokemonwarodri.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pokemonwarodri.utils.StaticData;

/**
 * This class will execute in the background a GET request
 * then it will inform back using a handler.
 */
public class InternetTaskGetHttp extends AsyncTask<String, Void, String> {

	private static String TAG = "InternetTaskGetHttp";
	private Context mContext;
    InternetManagerHandler mHandler;

    public InternetTaskGetHttp(Context context, InternetManagerHandler handler) {
        mContext = context;
    	mHandler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
    	
        String response = null;
        
    	if (checkInternet()) {

        	InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            
            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    response = convertStreamToString(inputStream);
                }
                
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
    		
    	}
        
        return response;
    }

    @Override
    protected void onPostExecute(String data) {
    	
    	if (mHandler != null) {
            InternetResponseModel model = new InternetResponseModel();
            model.resultOk = true;
            model.strResult = data;
            mHandler.onInternetResponse(model);
        }
    }
    
    
    private String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    
	/**
	 * It's a good practice to check if this device has 
	 * an active Internet connection before trying to go
	 * and get info from any remote server.
	 */
	private boolean checkInternet() {
		return StaticData.hasActiveInternetConnection(mContext);
	}

    

}
