package com.lzm.Cajas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 27/07/2014.
 */
public class LugarDbHelper extends DbHelper {

    private static final String LOG = "LugarDbHelper";

    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_NOMBRE_NORM = "nombre_norm";

    public static final String[] KEYS_LUGAR = {KEY_NOMBRE, KEY_NOMBRE_NORM};

    public LugarDbHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUGAR);

        // create new tables
        onCreate(db);
    }

    public long createLugar(Lugar lugar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(lugar, true);

        // insert row
        long res = db.insert(TABLE_LUGAR, null, values);
        db.close();
        return res;
    }

    public Lugar getLugar(long lugar_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_LUGAR + " WHERE "
                + KEY_ID + " = " + lugar_id;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Lugar cl = null;
        if (c.getCount() > 0) {
            c.moveToFirst();
            cl = setDatos(c);
        }
        db.close();
        return cl;
    }

    public ArrayList<Lugar> getAllLugares() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Lugar> lugares = new ArrayList<Lugar>();
        String selectQuery = "SELECT  * FROM " + TABLE_LUGAR;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Lugar cl = setDatos(c);

                // adding to tags list
                lugares.add(cl);
            } while (c.moveToNext());
        }
        db.close();
        return lugares;
    }

    public List<Lugar> getAllLugaresByNombre(String lugar) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Lugar> lugares = new ArrayList<Lugar>();
        String selectQuery = "SELECT  * FROM " + TABLE_LUGAR +
                " WHERE " + KEY_NOMBRE + " = '" + lugar + "'";

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Lugar cl = setDatos(c);

                // adding to tags list
                lugares.add(cl);
            } while (c.moveToNext());
        }
        db.close();
        return lugares;
    }

    public int countAllLugares() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_LUGAR;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public int countLugaresByNombre(String lugar) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_LUGAR +
                " WHERE " + KEY_NOMBRE + " = '" + lugar + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        db.close();
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public int updateLugar(Lugar lugar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(lugar);

        // updating row
        int res = db.update(TABLE_LUGAR, values, KEY_ID + " = ?",
                new String[]{String.valueOf(lugar.getId())});
        db.close();
        return res;
    }

    public void deleteLugar(Lugar lugar) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LUGAR, KEY_ID + " = ?",
                new String[]{String.valueOf(lugar.id)});
        db.close();
    }

    public void deleteAllLugares() {
        String sql = "DELETE FROM " + TABLE_LUGAR;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    private Lugar setDatos(Cursor c) {
        Lugar cl = new Lugar(this.context);
        cl.setId(c.getLong((c.getColumnIndex(KEY_ID))));
        cl.setFecha(c.getString(c.getColumnIndex(KEY_FECHA)));
        cl.setNombre(c.getString(c.getColumnIndex(KEY_NOMBRE)));
        return cl;
    }

    private ContentValues setValues(Lugar lugar, boolean fecha) {
        ContentValues values = new ContentValues();
        if (fecha) {
            values.put(KEY_FECHA, getDateTime());
        }
        values.put(KEY_NOMBRE, lugar.nombre);
        values.put(KEY_NOMBRE_NORM, lugar.nombreNorm);
        return values;
    }

    private ContentValues setValues(Lugar lugar) {
        return setValues(lugar, false);
    }
}

