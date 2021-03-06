package com.lzm.Cajas.db;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by DELL on 27/07/2014.
 */
public class Nota {
    public long id = 0;
    public String fecha;
    public String titulo;
    public String contenido;

    NotaDbHelper notaDbHelper;

    public Nota(Context context) {
        notaDbHelper = new NotaDbHelper(context);
    }

    public Nota(Context context, String titulo) {
        this.titulo = titulo;

        notaDbHelper = new NotaDbHelper(context);
    }

    public Nota(Context context, long id, String titulo) {
        this.id = id;
        this.titulo = titulo;

        notaDbHelper = new NotaDbHelper(context);
    }

    //getters
    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getContenido() {
        return contenido;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String toString() {
        return titulo;
    }

    public void save() {
        if (this.id == 0) {
            this.id = this.notaDbHelper.createNota(this);
        } else {
            this.notaDbHelper.updateNota(this);
        }
    }

    public static Nota get(Context context, long id) {
        NotaDbHelper e = new NotaDbHelper(context);
        return e.getNota(id);
    }

    public static int count(Context context) {
        NotaDbHelper e = new NotaDbHelper(context);
        return e.countAllNotas();
    }

    public static ArrayList<Nota> list(Context context) {
        NotaDbHelper e = new NotaDbHelper(context);
        return e.getAllNotas();
    }

    public static void delete(Context context, Nota nota) {
        NotaDbHelper e = new NotaDbHelper(context);
        e.deleteNota(nota);
    }

    public static void empty(Context context) {
        NotaDbHelper e = new NotaDbHelper(context);
        e.deleteAllNotaes();
    }
}
