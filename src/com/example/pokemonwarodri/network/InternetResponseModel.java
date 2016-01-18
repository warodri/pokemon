package com.example.pokemonwarodri.network;

import android.graphics.Bitmap;

/**
 * Modelo de datos de una respuesta
 * de Internet (algunos campos pueden
 * permancer null porque no aplica)
 */
public class InternetResponseModel {
    public boolean resultOk = true;
    public String strResult = null;
    public Bitmap bitmapResult = null;
}
