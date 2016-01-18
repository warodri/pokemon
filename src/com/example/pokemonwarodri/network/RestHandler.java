package com.example.pokemonwarodri.network;

/**
 * Callback de las operaciones REST.
 */
public interface RestHandler {
    public void onResponse(Exception e, Object response);
}
