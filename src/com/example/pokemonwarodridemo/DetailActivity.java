package com.example.pokemonwarodridemo;

import java.util.Hashtable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonwarodri.models.ApiAccessManager;
import com.example.pokemonwarodri.models.Pokemon;
import com.example.pokemonwarodri.models.PokemonDetail;

public class DetailActivity extends Activity {

	private final String TAG = "DetailActivity";
	
	/**
	 * This is just this application's context.
	 * We use a Context to do stuff outside this Activity-
	 */
	private Context mContext = this;

	/**
	 * Android progress dialog to show a Please Wait...
	 */
	private ProgressDialog mProgress;
	
	/**
	 * We use this manager to get info from our Pokemon API.
	 */
	private ApiAccessManager mApiAccessManager;
	
	/**
	 * Pokemon info fields.
	 */
	private String mName;
	private String mMale_female_ratio;
	private String mNational_id;
	private String mSprite_uri;
	private String mImage_uri;

	
	/**
	 * Handle response from getting info from our API.
	 * 1) Handle when the list is downloaded.
	 * 2) Handle when details are downloaded.
	 * 3) Handle when an image is downloaded.
	 * 4) Handle when image info is received.
	 */
	private ApiAccessManager.PokemonData mCallBack = new ApiAccessManager.PokemonData() {

		@Override
		public void onPokemonListReceived(Hashtable<String, Pokemon> data) {
			// nothing here
		}

		@Override
		public void onPokemonDetailReceived(PokemonDetail data) {
			// nothing here
		}

		@Override
		public void onPokemonImageReceived(Bitmap image) {
			// Pokemon image is here
			final Bitmap finalBitmap = image;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showTextOnScreen();
					showImageOnScreen(finalBitmap);
				}
			});
		}

		@Override
		public void onPokemonImageInfoReceived(String imageUri) {
			if (imageUri == null || imageUri.trim().length() == 0) {
				Toast.makeText(mContext, "Error recovering Sprite image info!", Toast.LENGTH_LONG).show();
				return;
			}
			mImage_uri = imageUri;
			downloadImage();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		mName = getIntent().hasExtra("name") ? getIntent().getStringExtra("name") : "No name defined.";
		mMale_female_ratio = getIntent().hasExtra("male_female_ratio") ? getIntent().getStringExtra("male_female_ratio") : "No male_female_ratio defined.";
		mNational_id = getIntent().hasExtra("national_id") ? getIntent().getStringExtra("national_id") : "No national_id defined.";
		mSprite_uri = getIntent().hasExtra("sprite_uri") ? getIntent().getStringExtra("sprite_uri") : null;
		
		initValues();
		downloadSpriteImagePath();
	}
	
	private void initValues() {
		
		// init our API access manager. Use a simple one.
		mApiAccessManager = new ApiAccessManager(mCallBack);
		
		// init Please Wait dialog
		mProgress = new ProgressDialog(mContext);
		mProgress.setTitle(getString(R.string.app_name));
		mProgress.setMessage(getString(R.string.please_wait));
				
	}
	
	/**
	 * Get this selected Pokemon image path to download
	 */
	private void downloadSpriteImagePath() {
		
		if (mSprite_uri == null || mSprite_uri.trim().length() == 0) {
			Toast.makeText(mContext, "No URL specified for your image!", Toast.LENGTH_LONG).show();
			return;
		}
		mApiAccessManager.getImageInfo(mContext, mSprite_uri);
	}
	
	/**
	 * Download Pokemon image
	 */
	private void downloadImage() {
	
		if (mImage_uri == null || mImage_uri.trim().length() == 0) {
			Toast.makeText(mContext, "No URL specified for your image!", Toast.LENGTH_LONG).show();
			return;
		}
		Log.i(TAG, "Sprite image URI: " + mImage_uri);
		mApiAccessManager.getPokemonImage(mContext, mImage_uri);
		
	}
	
	/**
	 * This is the selected Pokemon image
	 * @param image
	 */
	private void showImageOnScreen(Bitmap image) {
		ImageView iv = (ImageView) findViewById(R.id.image);
		iv.setImageBitmap(image);
	}
	
	/**
	 * This is the selected Pokemon info
	 */
	private void showTextOnScreen() {
		TextView tv = (TextView) findViewById(R.id.labelPokemonInfo);
		tv.setText("Name: " + mName + "\n" + 
				"National Pokedex number: " + mNational_id + "\n" + 
				"Male / Femal ratio: " + mMale_female_ratio);
	}
	
}
