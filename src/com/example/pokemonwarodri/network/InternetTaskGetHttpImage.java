package com.example.pokemonwarodri.network;

import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.pokemonwarodri.utils.StaticData;

/**
 * This class will execute in the background a GET request
 * then it will inform back using a handler.
 */
public class InternetTaskGetHttpImage extends AsyncTask<String, Void, Bitmap> {

	private static String TAG = "InternetTaskGetHttp";
	private Context mContext;
    InternetManagerHandler mHandler;

    public InternetTaskGetHttpImage(Context context, InternetManagerHandler handler) {
        mContext = context;
    	mHandler = handler;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
    	
    	if (params.length == 0) {
    		return null;
    	}
    	
		Bitmap retVal = null;
    	String url = params[0];
        
    	if (checkInternet()) {

    	    try {
    	        URL imageUrl = new URL(url);
    	        retVal = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
    	    }
    	    catch (Exception e) {
    	    	e.printStackTrace();
    	    }
    		
    	}
        
        return retVal;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
    	
    	if (mHandler != null) {
            InternetResponseModel model = new InternetResponseModel();
            model.resultOk = true;
            model.strResult = "";
            model.bitmapResult = image;
            mHandler.onInternetResponse(model);
        }
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
