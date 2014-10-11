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
public class FormaVidaDbHelper extends DbHelper {

    private static final String LOG = "FormaVidaDbHelper";

    public static final String KEY_NOMBRE = "nombre";

    public static final String[] KEYS_FORMA_VIDA = {KEY_NOMBRE};

    public FormaVidaDbHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMA_VIDA);

        // create new tables
        onCreate(db);
    }

    public long createFormaVida(FormaVida formavida) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(formavida, true);

        // insert row
        long res = db.insert(TABLE_FORMA_VIDA, null, values);
        db.close();
        return res;
    }

    public FormaVida getFormaVida(long formavida_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_FORMA_VIDA + " WHERE "
                + KEY_ID + " = " + formavida_id;

        Cursor c = db.rawQuery(selectQuery, null);
        FormaVida cl = null;
        if (c.getCount() > 0) {
            c.moveToFirst();
            cl = setDatos(c);
        }
        db.close();
        return cl;
    }

    public ArrayList<FormaVida> getAllFormasVida() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FormaVida> formavidaes = new ArrayList<FormaVida>();
        String selectQuery = "SELECT  * FROM " + TABLE_FORMA_VIDA;

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                FormaVida cl = setDatos(c);

                // adding to tags list
                formavidaes.add(cl);
            } while (c.moveToNext());
        }
        db.close();
        return formavidaes;
    }

    public ArrayList<FormaVida> getOnlyFormasVida() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FormaVida> formavidaes = new ArrayList<FormaVida>();
        String selectQuery = "SELECT  * FROM " + TABLE_FORMA_VIDA +
                " WHERE " + KEY_NOMBRE + " <> 'none'";

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                FormaVida cl = setDatos(c);

                // adding to tags list
                formavidaes.add(cl);
            } while (c.moveToNext());
        }
        db.close();
        return formavidaes;
    }

    public List<FormaVida> getAllFormasVidaByNombre(String formavida) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<FormaVida> formavidaes = new ArrayList<FormaVida>();
        String selectQuery = "SELECT  * FROM " + TABLE_FORMA_VIDA +
                " WHERE " + KEY_NOMBRE + " = '" + formavida + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                FormaVida cl = setDatos(c);

                // adding to tags list
                formavidaes.add(cl);
            } while (c.moveToNext());
        }
        db.close();
        return formavidaes;
    }

    public int countAllFormasVida() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_FORMA_VIDA;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public int countFormasVidaByNombre(String formavida) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_FORMA_VIDA +
                " WHERE " + KEY_NOMBRE + " = '" + formavida + "'";
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

    public int updateFormaVida(FormaVida formavida) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(formavida);

        // updating row
        int res = db.update(TABLE_FORMA_VIDA, values, KEY_ID + " = ?",
                new String[]{String.valueOf(formavida.getId())});
        db.close();
        return res;
    }

    public void deleteFormaVida(FormaVida formavida) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FORMA_VIDA, KEY_ID + " = ?",
                new String[]{String.valueOf(formavida.id)});
        db.close();
    }

    public void deleteAllFormasVida() {
        String sql = "DELETE FROM " + TABLE_FORMA_VIDA;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    private FormaVida setDatos(Cursor c) {
        FormaVida cl = new FormaVida(this.context);
        cl.setId(c.getLong((c.getColumnIndex(KEY_ID))));
        cl.setFecha(c.getString(c.getColumnIndex(KEY_FECHA)));
        cl.setNombre(c.getString(c.getColumnIndex(KEY_NOMBRE)));
        return cl;
    }

    private ContentValues setValues(FormaVida formavida, boolean fecha) {
        ContentValues values = new ContentValues();
        if (fecha) {
            values.put(KEY_FECHA, getDateTime());
        }
        values.put(KEY_NOMBRE, formavida.nombre);
        return values;
    }

    private ContentValues setValues(FormaVida formavida) {
        return setValues(formavida, false);
    }
}

