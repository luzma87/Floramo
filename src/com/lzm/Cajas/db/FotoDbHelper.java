package com.lzm.Cajas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 26/07/2014.
 */
public class FotoDbHelper extends DbHelper {

    // Logcat tag
    private static final String LOG = "FotoDbHelper";

    //  FOTO - column names
    private static final String KEY_ESPECIE_ID = "especie_id";
    private static final String KEY_KEYWORDS = "keywords";
    private static final String KEY_PATH = "path";
    public static final String KEY_RUTA_ID = "ruta_id";
    private static final String KEY_LONGITUD = "longitud";
    private static final String KEY_LATITUD = "latitud";
    private static final String KEY_ALTITUD = "altitud";
    private static final String KEY_LUGAR_ID = "lugar_id";
    private static final String KEY_COORDENADA_ID = "coordenada_id";

    public static final String[] KEYS_FOTO = {KEY_ESPECIE_ID, KEY_KEYWORDS, KEY_PATH,
            KEY_LATITUD, KEY_LONGITUD, KEY_ALTITUD, KEY_LUGAR_ID, KEY_COORDENADA_ID, KEY_RUTA_ID};

    public FotoDbHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOTO);

        // create new tables
        onCreate(db);
    }

    public long createFoto(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(foto, true);

        // insert row
        long res = db.insert(TABLE_FOTO, null, values);
        db.close();
        return res;
    }

    public Foto getFoto(long foto_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FOTO + " WHERE "
                + KEY_ID + " = " + foto_id;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Foto ft = null;
        if (c != null) {
            c.moveToFirst();
            ft = setDatos(c);
        }
        db.close();
        return ft;
    }

    public int countAllFotos() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_FOTO;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public int countFotosByEspecie(Especie especie) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_FOTO +
                " WHERE " + KEY_ESPECIE_ID + " = '" + especie.id + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public Foto getFotoByEspecie(Especie especie) {
        SQLiteDatabase db = this.getReadableDatabase();
        Foto foto = null;
//        String selectQuery = "SELECT * FROM " + TABLE_FOTO + " tf, " + TABLE_ESPECIE + " te " +
//                "WHERE tf." + KEY_ESPECIE_ID + "=te." + KEY_ID +
//                " AND te." + KEY_ID + "='" + especie.id + "' ";

        String selectQuery = "SELECT * FROM " + TABLE_FOTO +
                " WHERE " + KEY_ESPECIE_ID + " = " + especie.id +
                " LIMIT 1";

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                foto = setDatos(c);

                // adding to foto list
            } while (c.moveToNext());
        }
        db.close();
        return foto;
    }

    public List<Foto> getAllFotos() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Foto> fotos = new ArrayList<Foto>();
        String selectQuery = "SELECT  * FROM " + TABLE_FOTO;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Foto f = setDatos(c);

                // adding to tags list
                fotos.add(f);
            } while (c.moveToNext());
        }
        db.close();
        return fotos;
    }

    public List<Foto> getAllFotosByEspecie(Especie especie) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Foto> fotos = new ArrayList<Foto>();

//        String selectQuery = "SELECT * FROM " + TABLE_FOTO + " tf, " + TABLE_ESPECIE + " te " +
//                "WHERE tf." + KEY_ESPECIE_ID + "=te." + KEY_ID +
//                " AND te." + KEY_ID + "='" + especie.id + "' ";

        String selectQuery = "SELECT * FROM " + TABLE_FOTO +
                " WHERE " + KEY_ESPECIE_ID + " = " + especie.id;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Foto f = setDatos(c);

                // adding to foto list
                fotos.add(f);
            } while (c.moveToNext());
        }
        db.close();
        return fotos;
    }

    public List<Foto> getAllFotosByKeyword(String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Foto> fotos = new ArrayList<Foto>();

        String selectQuery = "SELECT * FROM " + TABLE_FOTO +
                " WHERE " + KEY_KEYWORDS + " LIKE '%" + keyword + "%'";

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Foto f = setDatos(c);

                // adding to foto list
                fotos.add(f);
            } while (c.moveToNext());
        }
        db.close();
        return fotos;
    }

    public int updateFoto(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(foto);

        // updating row
        int res = db.update(TABLE_FOTO, values, KEY_ID + " = ?",
                new String[]{String.valueOf(foto.getId())});
        db.close();
        return res;
    }

    public void deleteFoto(Foto foto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOTO, KEY_ID + " = ?",
                new String[]{String.valueOf(foto.id)});
        db.close();
    }

    public void deleteAllFotos() {
        String sql = "DELETE FROM " + TABLE_FOTO;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    private Foto setDatos(Cursor c) {
        Foto f = new Foto(this.context);
        f.setId(c.getLong((c.getColumnIndex(KEY_ID))));
        f.setFecha(c.getString(c.getColumnIndex(KEY_FECHA)));
        f.setEspecie_id(c.getLong(c.getColumnIndex(KEY_ESPECIE_ID)));
        f.setLatitud(c.getDouble(c.getColumnIndex(KEY_LATITUD)));
        f.setLongitud(c.getDouble(c.getColumnIndex(KEY_LONGITUD)));
        f.setAltitud(c.getDouble(c.getColumnIndex(KEY_ALTITUD)));
        f.setKeywords((c.getString(c.getColumnIndex(KEY_KEYWORDS))));
        f.setPath((c.getString(c.getColumnIndex(KEY_PATH))));
        f.setCoordenada_id((c.getLong(c.getColumnIndex(KEY_COORDENADA_ID))));
        f.setLugar_id((c.getLong(c.getColumnIndex(KEY_LUGAR_ID))));
        f.setRuta_id((c.getLong(c.getColumnIndex(KEY_RUTA_ID))));
        return f;
    }

    private ContentValues setValues(Foto foto, boolean fecha) {
        ContentValues values = new ContentValues();
        if (fecha) {
            values.put(KEY_FECHA, getDateTime());
        }
        if (foto.especie_id != null) {
            values.put(KEY_ESPECIE_ID, foto.especie_id);
        }
        if (foto.coordenada_id != null) {
            values.put(KEY_COORDENADA_ID, foto.coordenada_id);
        }
        if (foto.lugar_id != null) {
            values.put(KEY_LUGAR_ID, foto.lugar_id);
        }
        if (foto.ruta_id != null) {
            values.put(KEY_RUTA_ID, foto.ruta_id);
        }
        values.put(KEY_LATITUD, foto.latitud);
        values.put(KEY_LONGITUD, foto.longitud);
        values.put(KEY_ALTITUD, foto.altitud);
        values.put(KEY_KEYWORDS, foto.getKeywords());
        values.put(KEY_PATH, foto.getPath());

        return values;
    }

    private ContentValues setValues(Foto foto) {
        return setValues(foto, false);
    }

    public List<Foto> getAllFotosByRuta(Ruta ruta) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Foto> fotos = new ArrayList<Foto>();

//        String selectQuery = "SELECT * FROM " + TABLE_FOTO + " tf, " + TABLE_ESPECIE + " te " +
//                "WHERE tf." + KEY_ESPECIE_ID + "=te." + KEY_ID +
//                " AND te." + KEY_ID + "='" + especie.id + "' ";

        String selectQuery = "SELECT * FROM " + TABLE_FOTO +
                " WHERE " + KEY_RUTA_ID + " = " + ruta.id;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Foto f = setDatos(c);

                // adding to foto list
                fotos.add(f);
            } while (c.moveToNext());
        }
        db.close();
        return fotos;
    }

}
