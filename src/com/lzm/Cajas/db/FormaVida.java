package com.lzm.Cajas.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 27/07/2014.
 */
public class FormaVida {
    public long id = 0;
    public String fecha;
    public String nombre;

    FormaVidaDbHelper formaVidaDbHelper;

    public FormaVida(Context context) {
        formaVidaDbHelper = new FormaVidaDbHelper(context);
    }

    public FormaVida(Context context, String nombre) {
        this.nombre = nombre;

        formaVidaDbHelper = new FormaVidaDbHelper(context);
    }

    public FormaVida(Context context, long id, String nombre) {
        this.id = id;
        this.nombre = nombre;

        formaVidaDbHelper = new FormaVidaDbHelper(context);
    }

    //getters
    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String toString() {
        return "**" + nombre;
    }

    public void save() {
        if (this.id == 0) {
            this.id = this.formaVidaDbHelper.createFormaVida(this);
        } else {
            this.formaVidaDbHelper.updateFormaVida(this);
        }
    }

    public static FormaVida get(Context context, long id) {
        FormaVidaDbHelper e = new FormaVidaDbHelper(context);
        return e.getFormaVida(id);
    }

    public static int count(Context context) {
        FormaVidaDbHelper e = new FormaVidaDbHelper(context);
        return e.countAllFormasVida();
    }

    public static int countByNombre(Context context, String formavida) {
        FormaVidaDbHelper e = new FormaVidaDbHelper(context);
        return e.countFormasVidaByNombre(formavida);
    }

    public static ArrayList<FormaVida> list(Context context) {
        FormaVidaDbHelper e = new FormaVidaDbHelper(context);
        return e.getAllFormasVida();
    }

    public static ArrayList<FormaVida> listFormasVida(Context context) {
        FormaVidaDbHelper e = new FormaVidaDbHelper(context);
        return e.getOnlyFormasVida();
    }

    public static List<FormaVida> findAllByNombre(Context context, String formavida) {
        FormaVidaDbHelper e = new FormaVidaDbHelper(context);
        return e.getAllFormasVidaByNombre(formavida);
    }

    public static void empty(Context context) {
        FormaVidaDbHelper e = new FormaVidaDbHelper(context);
        e.deleteAllFormasVida();
    }
}
