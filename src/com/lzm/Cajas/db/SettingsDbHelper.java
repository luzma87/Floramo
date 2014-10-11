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
public class SettingsDbHelper extends DbHelper {

    private static final String LOG = "SettingsDbHelper";

    private static final String KEY_TROPICOS = "tropicosBase";
    private static final String KEY_FLORA = "floraBase";

    public static final String[] KEYS_SETTINGS = {KEY_TROPICOS, KEY_FLORA};

    public SettingsDbHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);

        // create new tables
        onCreate(db);
    }

    public long createSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(settings, true);

        // insert row
        long res = db.insert(TABLE_SETTINGS, null, values);
        db.close();
        return res;
    }

    public Settings getSettings(long settings_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS + " WHERE "
                + KEY_ID + " = " + settings_id;

        Cursor c = db.rawQuery(selectQuery, null);
        Settings cl = null;
        if (c.getCount() > 0) {
            c.moveToFirst();
            cl = setDatos(c);
        }
        db.close();
        return cl;
    }

    public Settings getFirstSettings() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS + " LIMIT 1";

        Cursor c = db.rawQuery(selectQuery, null);
        Settings cl = null;
        if (c.getCount() > 0) {
            c.moveToFirst();
            cl = setDatos(c);
        }
        db.close();
        return cl;
    }

    public ArrayList<Settings> getAllSettingses() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Settings> settingses = new ArrayList<Settings>();
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Settings cl = setDatos(c);

                // adding to tags list
                settingses.add(cl);
            } while (c.moveToNext());
        }
        db.close();
        return settingses;
    }

    public int countAllSettingses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_SETTINGS;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public int updateSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(settings);

        // updating row
        int res = db.update(TABLE_SETTINGS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(settings.getId())});
        db.close();
        return res;
    }

    public void deleteSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SETTINGS, KEY_ID + " = ?",
                new String[]{String.valueOf(settings.id)});
        db.close();
    }

    public void deleteAllSettingses() {
        String sql = "DELETE FROM " + TABLE_SETTINGS;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    private Settings setDatos(Cursor c) {
        Settings cl = new Settings(this.context);
        cl.setId(c.getLong((c.getColumnIndex(KEY_ID))));
        cl.setFecha(c.getString(c.getColumnIndex(KEY_FECHA)));
        cl.setTropicosBase(c.getString(c.getColumnIndex(KEY_TROPICOS)));
        cl.setFloraBase(c.getString(c.getColumnIndex(KEY_FLORA)));
        return cl;
    }

    private ContentValues setValues(Settings settings, boolean fecha) {
        ContentValues values = new ContentValues();
        if (fecha) {
            values.put(KEY_FECHA, getDateTime());
        }
        values.put(KEY_TROPICOS, settings.tropicosBase);
        values.put(KEY_FLORA, settings.floraBase);
        return values;
    }

    private ContentValues setValues(Settings settings) {
        return setValues(settings, false);
    }
}

