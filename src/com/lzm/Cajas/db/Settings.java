package com.lzm.Cajas.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 27/07/2014.
 */
public class Settings {
    public long id = 0;
    public String fecha;
    public String floraBase;
    public String tropicosBase;

    SettingsDbHelper settingsDbHelper;

    public Settings(Context context) {
        settingsDbHelper = new SettingsDbHelper(context);
    }

    //getters
    public long getId() {
        return id;
    }

    public String getFloraBase() {
        return floraBase;
    }

    public String getTropicosBase() {
        return tropicosBase;
    }

    public String getFecha() {
        return fecha;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }

    public void setFloraBase(String floraBase) {
        this.floraBase = floraBase;
    }

    public void setTropicosBase(String tropicosBase) {
        this.tropicosBase = tropicosBase;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void save() {
        if (this.id == 0) {
            this.id = this.settingsDbHelper.createSettings(this);
        } else {
            this.settingsDbHelper.updateSettings(this);
        }
    }

    public static Settings get(Context context, long id) {
        SettingsDbHelper e = new SettingsDbHelper(context);
        return e.getSettings(id);
    }

    public static Settings getSettings(Context context) {
        SettingsDbHelper e = new SettingsDbHelper(context);
        return e.getFirstSettings();
    }

    public static int count(Context context) {
        SettingsDbHelper e = new SettingsDbHelper(context);
        return e.countAllSettingses();
    }

//    public static int countByNombre(Context context, String settings) {
//        SettingsDbHelper e = new SettingsDbHelper(context);
//        return e.countSettingsesByNombre(settings);
//    }

    public static ArrayList<Settings> list(Context context) {
        SettingsDbHelper e = new SettingsDbHelper(context);
        return e.getAllSettingses();
    }

//    public static ArrayList<Settings> listSettingses(Context context) {
//        SettingsDbHelper e = new SettingsDbHelper(context);
//        return e.getOnlySettingses();
//    }

//    public static List<Settings> findAllByNombre(Context context, String settings) {
//        SettingsDbHelper e = new SettingsDbHelper(context);
//        return e.getAllSettingsesByNombre(settings);
//    }

    public static void empty(Context context) {
        SettingsDbHelper e = new SettingsDbHelper(context);
        e.deleteAllSettingses();
    }
}
