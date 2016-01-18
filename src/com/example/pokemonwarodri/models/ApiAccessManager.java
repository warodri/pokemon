package com.example.pokemonwarodri.models;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.pokemonwarodri.network.InternetManagerHandler;
import com.example.pokemonwarodri.network.InternetResponseModel;
import com.example.pokemonwarodri.network.InternetTaskGetHttp;
import com.example.pokemonwarodri.network.InternetTaskGetHttpImage;
import com.example.pokemonwarodri.utils.StaticData;

/**
 * ApiAccessManager will get the info from our API using
 * a simple HttpURLConnection object from Android Framework.
 * 
 * @author Walter A Rodriguez
 *
 */
public class ApiAccessManager extends SimpleApiPokemon {

	private static final String TAG = "ApiAccessManager";
	
	private final String POKEMON = "pokemon";
	private final String NAME = "name";
	private final String RESOURCE_URI = "resource_uri";
	private final String SPRITES = "sprites";
	
	/**
	 * Using this manager, we can implement different methods for grabbing
	 * data from our API without changing any code from our Activity.
	 */
	public ApiAccessManager(PokemonData callback) {
		super(callback);
	}
	
	/**
	 * My implementation for getting the info from our API
	 */
	@Override
	public void getPokemonList(Context context) {
		
		super.getPokemonList(context);
		
		try {
			// URL for our Pokemon list
			String url = StaticData.SERVER_BASE + StaticData.POKEMON_LIST;
			Log.i(TAG, "Pokemon List URL: " + url);
			
			// Download from Internet
			new InternetTaskGetHttp(context, new InternetManagerHandler() {
				
				@Override
				public void onInternetResponse(InternetResponseModel model) {
					Log.i(TAG, "Response: " + model.strResult);
					// Get the parsed list of Pokemon
					Hashtable<String, Pokemon> result = getParsedList(model.strResult);
					// Respond the parsed list
					if (getCallback() != null) getCallback().onPokemonListReceived(result);
				}
			}).execute(url);
			
			
		} catch(Exception e) {
			e.printStackTrace();
			// Respond null because of an error
			if (getCallback() != null) getCallback().onPokemonListReceived(null);
		}

	}
	
	
	/**
	 * My implementation for getting Pokemon details from our API
	 */
	@Override
	public void getPokemonDetail(Context context, String uri) {
		
		super.getPokemonDetail(context, uri);
		
		try {
			// URL for our Pokemon list
			String url = StaticData.SERVER_BASE + uri;
			Log.i(TAG, "Pokemon Detail URL: " + url);
			
			// Download from Internet
			new InternetTaskGetHttp(context, new InternetManagerHandler() {
				
				@Override
				public void onInternetResponse(InternetResponseModel model) {
					Log.i(TAG, "Response: " + model.strResult);
					// Get the parsed list of Pokemon
					PokemonDetail result = getParsedDetails(model.strResult);
					// Respond the parsed list
					if (getCallback() != null) getCallback().onPokemonDetailReceived(result);
				}
			}).execute(url);
			
			
		} catch(Exception e) {
			e.printStackTrace();
			// Respond null because of an error
			if (getCallback() != null) getCallback().onPokemonDetailReceived(null);
		}
		
	}
	
	/**
	 * Will parse a raw JSON code recovered for our Pokemon list.
	 * @param rawJsonText : raw text received.
	 * @return : Hashtable with all the elements. Null if error or empty if none recovered.
	 */
	private Hashtable<String, Pokemon> getParsedList(String rawJsonText) {
		
		Hashtable<String, Pokemon> ht = new Hashtable<String, Pokemon>();
		
		try {
			
			JSONObject obj = new JSONObject(rawJsonText);
			JSONArray arr = obj.getJSONArray(POKEMON);
			
			for (int i=0; i < arr.length(); i++) {
				
				JSONObject pokemon = arr.getJSONObject(i);
				String name = pokemon.getString(NAME);
				String resource_uri = pokemon.getString(RESOURCE_URI);
				
				Pokemon p = new Pokemon();
				p.name = name;
				p.resource_uri = resource_uri;
				
				ht.put(name, p);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return ht;
		
	}
	
	/**
	 * Parse a raw JSON with all the selected Pokemon details.
	 * @param rawJsonText : raw text received.
	 * @return : PokemonDetail object with all the needed information.
	 */
	private PokemonDetail getParsedDetails(String rawJsonText) {
		
		PokemonDetail data = null;
		
		try {
			
			JSONObject obj = new JSONObject(rawJsonText);
			JSONArray arr = obj.getJSONArray(SPRITES);
			
			String name = obj.getString(NAME);
			String male_female_ratio = obj.getString("male_female_ratio");
			String national_id = obj.getString("national_id");
			String resource_uri = "";
			
			if (arr.length() > 0) {
				// we only need the first image
				JSONObject pokemon = arr.getJSONObject(0);
				resource_uri = pokemon.getString("resource_uri");
			}
			
			data = new PokemonDetail();
			
			// populate response...
			data.name = name;
			data.male_female_ratio = male_female_ratio;
			data.national_id = national_id;
			data.sprite_uri = resource_uri;
			data.image_uri = ""; // not yet
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return data;
		
	}

	/**
	 * My implementation for getting Pokemon image info from our API
	 */
	@Override
	public void getImageInfo(Context context, String uri) {
		
		super.getImageInfo(context, uri);
		
		String url = StaticData.SERVER_API + uri;
		Log.i(TAG, "Sprite info URL: " + url);
		
		// Download image...
		new InternetTaskGetHttp(context, new InternetManagerHandler() {
			
			@Override
			public void onInternetResponse(InternetResponseModel model) {
				
				String rawJson = model.strResult;
				
				if (rawJson == null || rawJson.trim().length() == 0) {
					if (getCallback() != null) getCallback().onPokemonImageInfoReceived(null);
					return;
				}
				
				try {
					JSONObject obj = new JSONObject(rawJson);
					String image = obj.getString("image");
					if (getCallback() != null) getCallback().onPokemonImageInfoReceived(image);
					
				} catch(Exception e) {
					e.printStackTrace();
					if (getCallback() != null) getCallback().onPokemonImageInfoReceived(null);
				}
			}
			
		}).execute(url);
	}
	
	/**
	 * My implementation for getting Pokemon image from our API
	 */
	@Override
	public void getPokemonImage(Context context, String uri) {
		
		super.getPokemonImage(context, uri);
		
		try {
			// URL for our Pokemon list
			String url = StaticData.SERVER_API + uri;
			Log.i(TAG, "Sprite image URL: " + url);
			
			// Download from Internet
			new InternetTaskGetHttpImage(context, new InternetManagerHandler() {
				
				@Override
				public void onInternetResponse(InternetResponseModel model) {
					Bitmap image = model.bitmapResult;
					if (getCallback() != null) getCallback().onPokemonImageReceived(image);
				}
			}).execute(url);
			
			
		} catch(Exception e) {
			e.printStackTrace();
			// Respond null because of an error
			if (getCallback() != null) getCallback().onPokemonImageReceived(null);
		}
		
	}
}
