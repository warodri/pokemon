package com.example.pokemonwarodridemo;

import java.util.Enumeration;
import java.util.Hashtable;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokemonwarodri.models.ApiAccessManager;
import com.example.pokemonwarodri.models.Pokemon;
import com.example.pokemonwarodri.models.PokemonDetail;

public class MainActivity extends ListActivity {

	private final String TAG = "MainActivity";
	
	/**
	 * This is just this application's context.
	 * We use a Context to do stuff outside this Activity-
	 */
	private Context mContext = this;
	
	/**
	 * We use this manager to get info from our Pokemon API.
	 */
	private ApiAccessManager mApiAccessManager;
	
	/**
	 * Android progress dialog to show a Please Wait...
	 */
	private ProgressDialog mProgress;
	
	/**
	 * Pagination: define how many items you want to 
	 * show on your list.
	 */
	private final int TOTAL_ITEMS_TO_SHOW = 10;
	
	/**
	 * Keep track of the last page
	 */
	private int mLastPageIndex = 0;
	private int mTotalElements = 0;
	
	/**
	 * Screen elements
	 */
	private LinearLayout mLayoutButtons;
	private TextView mPageInfo;
	
	/**
	 * Hold your pokemon info here.
	 * PLEASE NOTE: I know this is not 100% useful here in this example
	 * but I wanted to show how I store data in case we need it.
	 */
	private  Hashtable<String, Pokemon> mData;

	/**
	 * List's items on screen
	 */
	private ArrayAdapter<String> mAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initValues();
		downloadPokemonList();
	}
	
	
	/**
	 * We capture any click on the list item
	 */
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id) {
	    
		// Get the key for our Hashtable and ...
		String key = mAdapter.getItem(position);
		Log.i(TAG, "Selected pokemon: " + key);
		
		// ... get the info. Download all details
		Pokemon selected = mData.get(key);
		String uri = selected.resource_uri;
		uri = uri.replace("api/v1/", "");
		   
		// Download now!
		downloadPokemonDetail(uri);
	    
	}

	
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
			mProgress.hide();
			if (data == null) {
				Toast.makeText(mContext, "Ups! We have an error receiving your list. "
						+ "Please check your Internet connection!", Toast.LENGTH_LONG).show();
			} else if (data.size() == 0) {
				Toast.makeText(mContext, "No Pokemons to show!", Toast.LENGTH_LONG).show();
			} else {
				mData = data;
				mTotalElements = mData.size();
				showTheList();
			}
		}

		@Override
		public void onPokemonDetailReceived(PokemonDetail data) {
			mProgress.hide();
			if (data == null) {
				Toast.makeText(mContext, "Ups! We have an error receiving details. "
						+ "Please check your Internet connection!", Toast.LENGTH_LONG).show();
			} else {
				showDetail(data);
			}
		}

		@Override
		public void onPokemonImageReceived(Bitmap image) {
			// nothing here
		}

		@Override
		public void onPokemonImageInfoReceived(String imageUri) {
			// nothing here
		}
	};
	
	
	/**
	 * Init some variables.
	 */
	private void initValues() {
		
		// init screen elements
		mLayoutButtons = (LinearLayout) findViewById(R.id.layoutButtons);
		mPageInfo = (TextView) findViewById(R.id.labelPageInfo);
		
		// init our API access manager. Use a simple one.
		mApiAccessManager = new ApiAccessManager(mCallBack);
		
		// init Please Wait dialog
		mProgress = new ProgressDialog(mContext);
		mProgress.setTitle(getString(R.string.app_name));
		mProgress.setMessage(getString(R.string.please_wait));
		
	}

	
	
	/**
	 * Do the download job for this Activity
	 */
	private void downloadPokemonList() {
		mProgress.show();
		mApiAccessManager.getPokemonList(mContext);
	}

	
	private void downloadPokemonDetail(String uri) {
		mProgress.show();
		mApiAccessManager.getPokemonDetail(mContext, uri);
	}
	
	/**
	 * Get the info and show the list.
	 */
	private void showTheList() {

		String[] itemName = new String[TOTAL_ITEMS_TO_SHOW];
		
		int firstIndex = mLastPageIndex * TOTAL_ITEMS_TO_SHOW;
		int counter = 0;
		int pos = 0;
		
		// include in the list only elements for current page.
		Enumeration<String> enume = mData.keys();
		while(enume.hasMoreElements()) {
			String key = enume.nextElement();
			if (counter >=  firstIndex) {
				itemName[pos] = key;
				pos ++;
				if (pos >= itemName.length) break;
			}
			counter ++;
		}
		
		// put elements on the list
		mAdapter = new ArrayAdapter<String>(this, R.layout.mylist, R.id.Itemname, itemName);
		this.setListAdapter(mAdapter);
		
		// Show pagination elements
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showPagination(mLastPageIndex);
				mAdapter.notifyDataSetChanged();
			}
		});
		
	}
	
	/**
	 * Show all pagination components.
	 * @param startFrom : page to start from if button is pressed.
	 */
	private void showPagination(int startFrom) {
		
		mLayoutButtons.removeAllViews();
		
		int totalPages = mTotalElements / TOTAL_ITEMS_TO_SHOW;
		int prevElement = startFrom - 1;
		int nextElement = startFrom + 1;
		int lastElement = totalPages - 1;
		
		Button first = getFooterButton("<<", 0);
		Button prev = getFooterButton("<", prevElement);
		Button next = getFooterButton(">", nextElement);
		Button last = getFooterButton(">>", lastElement);
		
		if (prevElement >= 0) mLayoutButtons.addView(first);
		if (prevElement >= 0) mLayoutButtons.addView(prev);
		if (nextElement <= lastElement) mLayoutButtons.addView(next);
		if (startFrom < (totalPages - 1)) mLayoutButtons.addView(last);
		
		int pageShow = startFrom + 1;
		mPageInfo.setText("Page " + pageShow + " of " + totalPages);
	}
	
	/**
	 * Creates a button for our fotter's pagination
	 * @param text : text to show on this button.
	 * @param pageIndex : next page to show if user presses this button.
	 * @return : Button object ready to add to the layout.
	 */
	private Button getFooterButton(String text, final int pageIndex) {
		Button b = new Button(mContext);
		b.setText(text);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mLastPageIndex = pageIndex;
						showTheList();
					}
				});
			}
		});
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		b.setLayoutParams(lp);
		return b;
	}

	/**
	 * Invoke Deatils Activity to show the image and some more info.
	 * 
	 * @param data : received data from JSON response.
	 */
	private void showDetail(PokemonDetail data) {
		
		Intent i = new Intent(this, DetailActivity.class);
		i.putExtra("name", data.name);
		i.putExtra("male_female_ratio", data.male_female_ratio);
		i.putExtra("national_id", data.national_id);
		i.putExtra("sprite_uri", data.sprite_uri);
		
		
		startActivity(i);
		
	}
	
}
