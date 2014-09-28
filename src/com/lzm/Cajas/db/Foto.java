package com.lzm.Cajas.db;

import android.content.Context;

import java.util.List;

/**
 * Created by DELL on 26/07/2014.
 */
public class Foto {
    public long id = 0;
    public String fecha;
    public String keywords;

    public Long especie_id;

    public Long color1_id;
    public Long color2_id;

    public double latitud;
    public double longitud;
    public double altitud = 0;

    public Long coordenada_id;

    public Long lugar_id;

    public String path;

    Context context;

    FotoDbHelper fotoDbHelper;

    public Foto(Context context) {
        fotoDbHelper = new FotoDbHelper(context);
    }

    public Foto(Context context, Especie especie, String comentarios, String keywords) {
        this.keywords = keywords;
        this.especie_id = especie.id;
        this.context = context;
        fotoDbHelper = new FotoDbHelper(context);
    }

    public Foto(Context context, Especie especie, String comentarios) {
        this.especie_id = especie.id;
        this.context = context;
        fotoDbHelper = new FotoDbHelper(context);
    }

    public Foto(Context context, Especie especie) {
        this.especie_id = especie.id;
        this.context = context;
        fotoDbHelper = new FotoDbHelper(context);
    }

    //getters
    public Especie getEspecie(Context context) {
        if (especie_id != null) {
            return Especie.get(context, especie_id);
        } else {
            return null;
        }
    }

    public Lugar getLugar(Context context) {
        if (lugar_id != null) {
            return Lugar.get(context, lugar_id);
        } else {
            return null;
        }
    }

    public Long getLugar_id() {
        return lugar_id;
    }

    public Long getEspecie_id() {
        return especie_id;
    }

    public long getId() {
        return id;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getFecha() {
        return fecha;
    }

    public String getPath() {
        return path;
    }

    public Long getCoordenada_id() {
        return coordenada_id;
    }

    public void setCoordenada_id(Long coordenada_id) {
        this.coordenada_id = coordenada_id;
    }

    public Coordenada getCoordenada() {
        if (especie_id != null) {
            return Coordenada.get(context, coordenada_id);
        } else {
            return null;
        }
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada_id = coordenada.id;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getAltitud() {
        return altitud;
    }

    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }


    //setter
    public void setEspecie(Especie especie) {
        this.especie_id = especie.id;
    }

    public void setEspecie_id(Long especie_id) {
        this.especie_id = especie_id;
    }

    public void setLugar(Lugar lugar) {
        this.lugar_id = lugar.id;
    }

    public void setLugar_id(Long lugar_id) {
        this.lugar_id = lugar_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void save() {
        if (this.id == 0) {
            this.id = this.fotoDbHelper.createFoto(this);
        } else {
            this.fotoDbHelper.updateFoto(this);
        }
    }

    public void delete() {
        this.fotoDbHelper.deleteFoto(this);
    }

    public static Foto get(Context context, long id) {
        FotoDbHelper e = new FotoDbHelper(context);
        return e.getFoto(id);
    }

    public static List<Foto> list(Context context) {
        FotoDbHelper e = new FotoDbHelper(context);
        return e.getAllFotos();
    }

    public static int count(Context context) {
        FotoDbHelper e = new FotoDbHelper(context);
        return e.countAllFotos();
    }

    public static int countByEspecie(Context context, Especie especie) {
        FotoDbHelper e = new FotoDbHelper(context);
        return e.countFotosByEspecie(especie);
    }

    public static Foto findByEspecie(Context context, Especie especie) {
        FotoDbHelper e = new FotoDbHelper(context);
        return e.getFotoByEspecie(especie);
    }

    public static List<Foto> findAllByEspecie(Context context, Especie especie) {
        FotoDbHelper e = new FotoDbHelper(context);
        return e.getAllFotosByEspecie(especie);
    }

    public static List<Foto> findAllByKeyword(Context context, String keyword) {
        FotoDbHelper e = new FotoDbHelper(context);
        return e.getAllFotosByKeyword(keyword);
    }

    public static void empty(Context context) {
        FotoDbHelper e = new FotoDbHelper(context);
        e.deleteAllFotos();
    }

}
