package com.lzm.Cajas.image;

import android.graphics.Bitmap;

/**
 * Created by Svt on 8/31/2014.
 */
public class EspecieUi {

    public String id;
    public String nombre;
    public int resId;
    public String tropicosId;
    public Bitmap foto;
    public long idEspecie;

    public EspecieUi(String nombre, Bitmap foto) {
        this.nombre = nombre;
        this.foto = foto;
    }

    public EspecieUi(String nombre, int resId) {
        this.nombre = nombre;
        this.resId = resId;
    }

    public EspecieUi(String nombre, int resId, String tropicosId) {
        this.nombre = nombre;
        this.resId = resId;
        this.tropicosId = tropicosId;
    }

    public EspecieUi(long idEspecie, String nombre, int resId, String tropicosId) {
        this.idEspecie = idEspecie;
        this.nombre = nombre;
        this.resId = resId;
        this.tropicosId = tropicosId;
    }
}
