package com.lzm.Cajas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by DELL on 26/07/2014.
 */
public class EspecieDbHelper extends DbHelper {

    // Logcat tag
    private static final String LOG = "EspecieDbHelper";

    // ESPECIE Table - column names
    private static final String KEY_NOMBRE_COMUN = "nombre_comun";
    private static final String KEY_NOMBRE_COMUN_NORM = "nombre_comun_norm";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_NOMBRE_NORM = "nombre_norm";
    private static final String KEY_GENERO_ID = "genero_id";
    private static final String KEY_COLOR1_ID = "color1_id";
    private static final String KEY_COLOR2_ID = "color2_id";
    private static final String KEY_FORMA_VIDA1_ID = "forma_vida1_id";
    private static final String KEY_FORMA_VIDA2_ID = "forma_vida2_id";
    private static final String KEY_ID_TROPICOS = "id_tropicos";
    private static final String KEY_ES_MIA = "es_mia";

    public static final String[] KEYS_ESPECIE = {KEY_NOMBRE_COMUN, KEY_NOMBRE_COMUN_NORM, KEY_NOMBRE, KEY_NOMBRE_NORM,
            KEY_GENERO_ID, KEY_COLOR1_ID, KEY_COLOR2_ID, KEY_FORMA_VIDA1_ID, KEY_FORMA_VIDA2_ID, KEY_ID_TROPICOS, KEY_ES_MIA};

    public EspecieDbHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESPECIE);

        // create new tables
        onCreate(db);
    }

    public long createEspecie(Especie especie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(especie, true);

        // insert row
        long res = db.insert(TABLE_ESPECIE, null, values);
        db.close();
        return res;
    }

    public Especie getEspecie(long especie_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ESPECIE + " WHERE "
                + KEY_ID + " = " + especie_id;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Especie es = null;
        if (c.getCount() > 0) {
            c.moveToFirst();
            es = setDatos(c);
        }
        db.close();
        return es;
    }

    public Especie getDatosEspecie(long especie_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " +
                "    e.id id," +
                "    e.nombre especie," +
                "    e.id_tropicos tropicos," +
                "    e.es_mia es_mia," +
                "    g.nombre genero," +
                "    f.nombre familia," +
                "    c1.nombre color1," +
                "    c2.nombre color2," +
                "    f1.nombre forma_vida1," +
                "    f2.nombre forma_vida2" +
                " FROM especies e" +
                " INNER JOIN generos g on e.genero_id = g.id" +
                " INNER JOIN familias f on g.familia_id = f.id" +
                " INNER JOIN colores c1 on e.color1_id = c1.id" +
                " OUTER LEFT JOIN colores c2 on e.color2_id = c2.id" +
                " INNER JOIN formas_vida f1 on e.forma_vida1_id = f1.id" +
                " OUTER LEFT JOIN formas_vida f2 on e.forma_vida2_id = f2.id" +
                " WHERE e.id = \"" + especie_id + "\"";

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Especie es = new Especie(context);
        if (c.getCount() > 0) {
            c.moveToFirst();
            es.setId(c.getLong((c.getColumnIndex(KEY_ID))));
            es.nombre = c.getString(c.getColumnIndex("especie"));
            es.idTropicos = c.getLong(c.getColumnIndex("tropicos"));
            es.esMia = c.getInt(c.getColumnIndex("es_mia"));
            es.genero = c.getString(c.getColumnIndex("genero"));
            es.familia = c.getString(c.getColumnIndex("familia"));
            es.color1 = c.getString(c.getColumnIndex("color1"));
            es.color2 = c.getString(c.getColumnIndex("color2"));
            es.formaVida1 = c.getString(c.getColumnIndex("forma_vida1"));
            es.formaVida2 = c.getString(c.getColumnIndex("forma_vida2"));
        }
        db.close();
        return es;
    }

    public List<Especie> getAllEspecies() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Especie> todos = new ArrayList<Especie>();
        String selectQuery = "SELECT  * FROM " + TABLE_ESPECIE;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Especie es = setDatos(c);
                // adding to especie list
                todos.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return todos;
    }

    public List<Especie> getAllEspeciesByGenero(Genero genero) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Especie> todos = new ArrayList<Especie>();
        String selectQuery = "SELECT  * FROM " + TABLE_ESPECIE +
                " WHERE " + KEY_GENERO_ID + " = " + genero.id;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Especie es = setDatos(c);
                // adding to especie list
                todos.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return todos;
    }

    public List<Especie> getAllEspeciesByNombre(String especie) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Especie> todos = new ArrayList<Especie>();
        String selectQuery = "SELECT  * FROM " + TABLE_ESPECIE +
                " WHERE " + KEY_NOMBRE + " = '" + especie + "'";

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Especie es = setDatos(c);
                // adding to especie list
                todos.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return todos;
    }

    public List<Especie> getAllEspeciesByNombreComunLike(String especie) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Especie> todos = new ArrayList<Especie>();
        String selectQuery = "SELECT  * FROM " + TABLE_ESPECIE +
                " WHERE LOWER(" + KEY_NOMBRE_COMUN_NORM + ") LIKE '%" + especie.toLowerCase() + "%'";

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Especie es = setDatos(c);
                // adding to especie list
                todos.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return todos;
    }

    public List<Especie> getAllEspeciesByNombreLike(String especie) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Especie> todos = new ArrayList<Especie>();
        String selectQuery = "SELECT  * FROM " + TABLE_ESPECIE +
                " WHERE LOWER(" + KEY_NOMBRE_NORM + ") LIKE '%" + especie.toLowerCase() + "%'";

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Especie es = setDatos(c);
                // adding to especie list
                todos.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return todos;
    }

    public List<Especie> getAllEspeciesByNombreCientifico(String nombreCientifico) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Especie> todos = new ArrayList<Especie>();
        String selectQuery = "SELECT  * FROM " + TABLE_ESPECIE + " te, " + TABLE_GENERO + " tg" +
                " WHERE te." + KEY_GENERO_ID + " = tg." + KEY_ID +
                " AND tg." + KEY_NOMBRE + "||' '||te." + KEY_NOMBRE + " = '" + nombreCientifico + "'";

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Especie es = setDatos(c);
                // adding to especie list
                todos.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return todos;
    }

    public List<Especie> getAllEspeciesByGeneroAndNombreLike(Genero genero, String especie) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Especie> todos = new ArrayList<Especie>();
        String selectQuery = "SELECT  * FROM " + TABLE_ESPECIE +
                " WHERE LOWER(" + KEY_NOMBRE_NORM + ") LIKE '%" + especie.toLowerCase() + "%'" +
                " AND " + KEY_GENERO_ID + " = " + genero.id;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Especie es = setDatos(c);
                // adding to especie list
                todos.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return todos;
    }

    public int countAllEspecies() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_ESPECIE;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public int countEspeciesByGenero(Genero genero) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_ESPECIE +
                " WHERE " + KEY_GENERO_ID + " = '" + genero.id + "'";
        logQuery(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public int countEspeciesByNombre(String especie) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_ESPECIE +
                " WHERE " + KEY_NOMBRE + " = '" + especie + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public int countEspeciesByNombreCientifico(String nombreCientifico) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) FROM " + TABLE_ESPECIE + " te, " + TABLE_GENERO + " tg" +
                " WHERE te." + KEY_GENERO_ID + " = tg." + KEY_ID +
                " AND tg." + KEY_NOMBRE + "||' '||te." + KEY_NOMBRE + " = '" + nombreCientifico + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public int countFotosByColor(Color color) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  count(*) count FROM " + TABLE_ESPECIE +
                " WHERE " + KEY_COLOR1_ID + " = '" + color.id + "'" +
                " OR " + KEY_COLOR2_ID + " = '" + color.id + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            int count = c.getInt(c.getColumnIndex("count"));
            db.close();
            return count;
        }
        db.close();
        return 0;
    }

    public List<Especie> getAllEspeciesByColor(Color color) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Especie> todos = new ArrayList<Especie>();
        String selectQuery = "SELECT  * FROM " + TABLE_ESPECIE +
                " WHERE " + KEY_COLOR1_ID + " = " + color.id +
                " OR " + KEY_COLOR2_ID + " = " + color.id;

        logQuery(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Especie es = setDatos(c);
                // adding to especie list
                todos.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return todos;
    }

    public long updateEspecie(Especie especie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setValues(especie);

        // updating row
        long res = db.update(TABLE_ESPECIE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(especie.getId())});
        db.close();
        return res;
    }

    public void deleteEspecie(Especie especie, boolean should_delete_all_fotos) {
        SQLiteDatabase db = this.getWritableDatabase();
        // before deleting tag
        // check if fotos under this especie should also be deleted
        if (should_delete_all_fotos) {
            // get all fotos under this especie
            List<Foto> allEspecieFotos = Foto.list(this.context);

            // delete all fotos
            for (Foto foto : allEspecieFotos) {
                // delete foto
                foto.delete();
            }
        }

        // now delete the tag
        db.delete(TABLE_ESPECIE, KEY_ID + " = ?",
                new String[]{String.valueOf(especie.getId())});
        db.close();
    }

    public List<Especie> getBusqueda(Vector<String> keywords, String color, String nc) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Especie> todos = new ArrayList<Especie>();
        String sql;

        String select = "SELECT  e.* ";
        String from = " FROM " + TABLE_ESPECIE + " e";
        String joins = "";
        String where = "";
        String groupBy = " GROUP BY e." + KEY_ID;

        if (!color.equals("")) {
            joins += " LEFT JOIN " + TABLE_COLOR + " c1 ON e." + KEY_COLOR1_ID + " = c1." + KEY_ID;
            joins += " LEFT JOIN " + TABLE_COLOR + " c2 ON e." + KEY_COLOR2_ID + " = c2." + KEY_ID;

            if (where.equals("")) {
                where += " WHERE ";
            } else {
                where += " AND ";
            }
            where += " (c1." + ColorDbHelper.KEY_NOMBRE + " = '" + color + "'";
            where += " OR c2." + ColorDbHelper.KEY_NOMBRE + " = '" + color + "')";
        }

        if (!nc.equals("")) {
            if (where.equals("")) {
                where += " WHERE ";
            } else {
                where += " AND ";
            }
            where += "LOWER(e." + KEY_NOMBRE_COMUN_NORM + ") LIKE '%" + Normalizer.normalize(nc, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase() + "%' ";
        }

        sql = select + from + joins + where + groupBy;

        logQuery(LOG, sql);

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Especie es = setDatos(c);
//                System.out.println("adding to especie list: " + es.id);
                todos.add(es);
            } while (c.moveToNext());
        }
        db.close();
        return todos;
    }

    public void deleteAllEspecies() {
        String sql = "DELETE FROM " + TABLE_ESPECIE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    private Especie setDatos(Cursor c) {
        Especie es = new Especie(this.context);
        es.setId(c.getLong((c.getColumnIndex(KEY_ID))));
        es.setNombreComun((c.getString(c.getColumnIndex(KEY_NOMBRE_COMUN))));
        es.setNombre(c.getString(c.getColumnIndex(KEY_NOMBRE)));
        es.setFecha(c.getString(c.getColumnIndex(KEY_FECHA)));
        es.setGenero_id(c.getLong(c.getColumnIndex(KEY_GENERO_ID)));
        es.setColor1_id(c.getLong(c.getColumnIndex(KEY_COLOR1_ID)));
        es.setColor2_id(c.getLong(c.getColumnIndex(KEY_COLOR2_ID)));
        es.setFormaVida1_id(c.getLong(c.getColumnIndex(KEY_FORMA_VIDA1_ID)));
        es.setFormaVida2_id(c.getLong(c.getColumnIndex(KEY_FORMA_VIDA2_ID)));
        es.setIdTropicos(c.getLong(c.getColumnIndex(KEY_ID_TROPICOS)));
        es.setEsMia(c.getInt(c.getColumnIndex(KEY_ES_MIA)));
        return es;
    }

    private ContentValues setValues(Especie especie, boolean fecha) {
        ContentValues values = new ContentValues();
        if (fecha) {
            values.put(KEY_FECHA, getDateTime());
        }
        values.put(KEY_NOMBRE_COMUN, especie.nombreComun);
        values.put(KEY_NOMBRE_COMUN_NORM, especie.nombreComunNorm);
        values.put(KEY_NOMBRE, especie.nombre);
        values.put(KEY_NOMBRE_NORM, especie.nombreNorm);
        values.put(KEY_ID_TROPICOS, especie.idTropicos);
        values.put(KEY_ES_MIA, especie.esMia);
        if (especie.genero_id != null) {
            values.put(KEY_GENERO_ID, especie.genero_id);
        }
        if (especie.color1_id != null) {
            values.put(KEY_COLOR1_ID, especie.color1_id);
        }
        if (especie.color2_id != null) {
            values.put(KEY_COLOR2_ID, especie.color2_id);
        }
        if (especie.formaVida1_id != null) {
            values.put(KEY_FORMA_VIDA1_ID, especie.formaVida1_id);
        }
        if (especie.formaVida2_id != null) {
            values.put(KEY_FORMA_VIDA2_ID, especie.formaVida2_id);
        }
        return values;
    }

    private ContentValues setValues(Especie especie) {
        return setValues(especie, false);
    }
}
