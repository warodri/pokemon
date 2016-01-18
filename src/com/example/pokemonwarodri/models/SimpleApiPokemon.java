package com.example.pokemonwarodri.models;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * This is a simple base model for creating other accesses to
 * our API from Internet. You can use different Frameworks for 
 * a better Internet connection or even do more complex stuff by 
 * extending this SimpleApiPokemon class.
 * 
 * @author Walter A Rodriguez
 *
 */
public abstract class SimpleApiPokemon {

	/**
	 * Asynchronous callback response
	 */
	private PokemonData mCallback;
	
	/**
	 * Contructor
	 * @param callback : set the callback
	 */
	public SimpleApiPokemon(PokemonData callback) {
		mCallback = callback;
	}

	/**
	 * Implement your own idea to get the info from
	 * Internet for our API.
	 * 
	 * @param context : your Activity context
	 */
	public void getPokemonList(Context context) {
	}

	/**
	 * Implement your own idea to get details from
	 * Internet for our API.
	 * 
	 * @param context : your Activity context
	 * @param uri: uri to get some Pokemon details
	 */
	public void getPokemonDetail(Context context, String uri) {
	}
	
	public void getImageInfo(Context context, String uri) {
		
	}
	
	/**
	 * Implement your own idea to get an image for downloading
	 * images from Pokemon's API
	 * 
	 * @param context : your Activity context
	 * @param uri : uri for the image to download
	 */
	public void getPokemonImage(Context context, String uri) {
	}
	
	/**
	 * Get the callback already set in the constructor.
	 * @return
	 */
	public PokemonData getCallback() {
		return mCallback;
	}

	/**
	 * Interface for asynchronous callback.
	 * @author Walter A Rodriguez
	 *
	 */
	public interface PokemonData {
		public void onPokemonListReceived(Hashtable<String, Pokemon> data);
		public void onPokemonDetailReceived(PokemonDetail data);
		public void onPokemonImageInfoReceived(String imageUri);
		public void onPokemonImageReceived(Bitmap image);
	}
	
	
	
}
