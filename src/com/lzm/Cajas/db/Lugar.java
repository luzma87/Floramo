package com.lzm.Cajas.db;

import android.content.Context;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 27/07/2014.
 */
public class Lugar {
    public long id = 0;
    public String fecha;
    public String nombre;
    public String nombreNorm;
    public String path;

    LugarDbHelper lugarDbHelper;

    public Lugar(Context context) {
        lugarDbHelper = new LugarDbHelper(context);
    }

    public Lugar(Context context, String nombre) {
        this.nombre = nombre;
        this.nombreNorm = Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        lugarDbHelper = new LugarDbHelper(context);
    }

    public Lugar(Context context, long id, String nombre) {
        this.id = id;
        this.nombre = nombre;

        lugarDbHelper = new LugarDbHelper(context);
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nombreNorm = Normalizer.normalize(nombre, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String toString() {
        return "**" + nombre;
    }

    public void save() {
        if (this.id == 0) {
            this.id = this.lugarDbHelper.createLugar(this);
        } else {
            this.lugarDbHelper.updateLugar(this);
        }
    }

    public static Lugar get(Context context, long id) {
        LugarDbHelper e = new LugarDbHelper(context);
        return e.getLugar(id);
    }

    public static int count(Context context) {
        LugarDbHelper e = new LugarDbHelper(context);
        return e.countAllLugares();
    }

    public static int countByNombre(Context context, String lugar) {
        LugarDbHelper e = new LugarDbHelper(context);
        return e.countLugaresByNombre(lugar);
    }

    public static ArrayList<Lugar> list(Context context) {
        LugarDbHelper e = new LugarDbHelper(context);
        return e.getAllLugares();
    }

    public static List<Lugar> findAllByNombre(Context context, String lugar) {
        LugarDbHelper e = new LugarDbHelper(context);
        return e.getAllLugaresByNombre(lugar);
    }

    public static void empty(Context context) {
        LugarDbHelper e = new LugarDbHelper(context);
        e.deleteAllLugares();
    }
}
