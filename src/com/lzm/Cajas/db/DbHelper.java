package com.lzm.Cajas.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by DELL on 26/07/2014.
 */
public class DbHelper extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 11;

    // Database Name
//    private static String DB_PATH = "/data/data/com.tmm.android.chuck/databases/";
    public static String DB_PATH = Environment.getExternalStorageDirectory().getPath() + "/Cajas/db/";
    //    public static String DB_PATH = "";
    private static final String DATABASE_NAME = "cajasDb.db";

    // Table Names
    protected static final String TABLE_COLOR = "colores";
    protected static final String TABLE_LUGAR = "lugares";
    protected static final String TABLE_FOTO = "fotos";
    protected static final String TABLE_FAMILIA = "familias";
    protected static final String TABLE_GENERO = "generos";
    protected static final String TABLE_ESPECIE = "especies";
    protected static final String TABLE_SETTINGS = "settings";
    protected static final String TABLE_FORMA_VIDA = "formas_vida";
    protected static final String TABLE_NOTA = "notas";
    protected static final String TABLE_RUTA = "rutas";
    protected static final String TABLE_COORDENADA = "coordenadas";


    // Common column names
    protected static final String KEY_ID = "id";
    protected static final String KEY_FECHA = "fecha";
    private static final String[] KEYS_COMMON = {KEY_ID, KEY_FECHA};

    protected Context context;

    public DbHelper(Context context) {
        super(context, DB_PATH + DATABASE_NAME, null, DATABASE_VERSION);
        new File(DB_PATH).mkdirs();
//        System.out.println("DB HELPER " + DB_PATH + DATABASE_NAME);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> AQUI <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Log.e("DBHELPER", "DBHELPER ON CREATE");

        db.execSQL(createTableSql(TABLE_COLOR, KEYS_COMMON, ColorDbHelper.KEYS_COLOR));
        db.execSQL(createTableSql(TABLE_ESPECIE, KEYS_COMMON, EspecieDbHelper.KEYS_ESPECIE));
        db.execSQL(createTableSql(TABLE_FAMILIA, KEYS_COMMON, FamiliaDbHelper.KEYS_FAMILIA));
        db.execSQL(createTableSql(TABLE_FOTO, KEYS_COMMON, FotoDbHelper.KEYS_FOTO));
        db.execSQL(createTableSql(TABLE_GENERO, KEYS_COMMON, GeneroDbHelper.KEYS_GENERO));
        db.execSQL(createTableSql(TABLE_LUGAR, KEYS_COMMON, LugarDbHelper.KEYS_LUGAR));
        db.execSQL(createTableSql(TABLE_SETTINGS, KEYS_COMMON, SettingsDbHelper.KEYS_SETTINGS));
        db.execSQL(createTableSql(TABLE_FORMA_VIDA, KEYS_COMMON, FormaVidaDbHelper.KEYS_FORMA_VIDA));
        db.execSQL(createTableSql(TABLE_NOTA, KEYS_COMMON, NotaDbHelper.KEYS_NOTA));
        db.execSQL(createTableSql(TABLE_RUTA, KEYS_COMMON, RutaDbHelper.KEYS_RUTA));
        db.execSQL(createTableSql(TABLE_COORDENADA, KEYS_COMMON, CoordenadaDbHelper.KEYS_COORDENADA));

        checkDb(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }

    /**
     * @param tableName:   el nombre de la tabla
     * @param common:      los campos comunes a las tablas: en pos 0 el id i en pos 1 la fecha
     * @param columnNames: los campos
     * @return el sql
     */
    public static String createTableSql(String tableName, String[] common, String[] columnNames) {
        String sql = "CREATE TABLE " + tableName + " (" +
                common[0] + " INTEGER PRIMARY KEY," +
                common[1] + " DATETIME";
        for (String c : columnNames) {
            if (c.startsWith("lat") || c.startsWith("long")) {
                sql += ", " + c + " REAL";
            } else {
                sql += ", " + c + " TEXT";
            }
        }
        sql += ")";
        //System.out.println("crear sql create " + sql);
        return sql;
    }

    /**
     * get datetime
     */
    protected static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void checkDb(SQLiteDatabase db) {
        db.execSQL("INSERT INTO settings (id, floraBase, tropicosBase) VALUES (\"2\", \"http://www.mobot.org/MOBOT/paramo/search_paramo.asp?searchFor=\", \"http://www.tropicos.org/Name/\");");

        db.execSQL("INSERT INTO lugares (id, nombre, nombre_norm) VALUES (\"1\", \"Páramo del Cajas, Ecuador\", \"Paramo del Cajas, Ecuador\");");

        db.execSQL("INSERT INTO colores(id, nombre) VALUES (\"3\", \"white\");");
        db.execSQL("INSERT INTO colores(id, nombre) VALUES (\"12\", \"green\");");
        db.execSQL("INSERT INTO colores(id, nombre) VALUES (\"27\", \"pink\");");
        db.execSQL("INSERT INTO colores(id, nombre) VALUES (\"36\", \"yellow\");");
        db.execSQL("INSERT INTO colores(id, nombre) VALUES (\"37\", \"orange\");");
        db.execSQL("INSERT INTO colores(id, nombre) VALUES (\"46\", \"purple\");");
        db.execSQL("INSERT INTO colores(id, nombre) VALUES (\"51\", \"brown\");");
        db.execSQL("INSERT INTO colores(id, nombre) VALUES (\"70\", \"red\");");
        db.execSQL("INSERT INTO colores(id, nombre) VALUES (\"126\", \"blue\");");


        db.execSQL("INSERT INTO formas_vida(id, nombre) VALUES (\"4\", \"cushion\");");
        db.execSQL("INSERT INTO formas_vida(id, nombre) VALUES (\"9\", \"shrub\");");
        db.execSQL("INSERT INTO formas_vida(id, nombre) VALUES (\"13\", \"herb\");");
        db.execSQL("INSERT INTO formas_vida(id, nombre) VALUES (\"52\", \"grass\");");
        db.execSQL("INSERT INTO formas_vida(id, nombre) VALUES (\"57\", \"rosette\");");
        db.execSQL("INSERT INTO formas_vida(id, nombre) VALUES (\"175\", \"acquatic\");");
        db.execSQL("INSERT INTO formas_vida(id, nombre) VALUES (\"235\", \"liana\");");
        db.execSQL("INSERT INTO formas_vida(id, nombre) VALUES (\"270\", \"tree\");");


        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"5\", \"Rubiaceae\", \"Rubiaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"14\", \"Apiaceae\", \"Apiaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"21\", \"Asteraceae\", \"Asteraceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"28\", \"Scrophulariaceae u Orobanchaceae\", \"Scrophulariaceae u Orobanchaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"32\", \"Orobanchaceae\", \"Orobanchaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"38\", \"Berberidaceae\", \"Berberidaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"42\", \"Alstroemeriaceae\", \"Alstroemeriaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"47\", \"Melastomataceae\", \"Melastomataceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"53\", \"Poaceae o Gramineae.\", \"Poaceae o Gramineae.\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"58\", \"Portulacaceae\", \"Portulacaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"62\", \"Calceolariaceae\", \"Calceolariaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"66\", \"Brassicaceae\", \"Brassicaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"74\", \"Caryophyllaceae\", \"Caryophyllaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"81\", \"Lamiaceae\", \"Lamiaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"90\", \"Ericaceae\", \"Ericaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"100\", \"Ephedraceae\", \"Ephedraceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"104\", \"Orchidaceae\", \"Orchidaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"108\", \"Equisetaceae\", \"Equisetaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"115\", \"Escalloniaceae\", \"Escalloniaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"119\", \"Onagraceae\", \"Onagraceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"127\", \"Gentianaceae\", \"Gentianaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"142\", \"Geraniaceae\", \"Geraniaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"146\", \"Rosaceae\", \"Rosaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"164\", \"Lycopodiaceae\", \"Lycopodiaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"168\", \"Hypericaceae\", \"Hypericaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"176\", \"Isoëtaceae\", \"Isoetaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"180\", \"Pteridaceae\", \"Pteridaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"192\", \"Fabaceae\", \"Fabaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"196\", \"Campanulaceae\", \"Campanulaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"205\", \"Polygalaceae\", \"Polygalaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"209\", \"Haloragaceae\", \"Haloragaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"213\", \"Poaceae o Gramineae\", \"Poaceae o Gramineae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"217\", \"Malvaceae\", \"Malvaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"224\", \"Plantaginaceae\", \"Plantaginaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"228\", \"Oxalidaceae\", \"Oxalidaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"236\", \"Passifloraceae\", \"Passifloraceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"248\", \"Piperaceae\", \"Piperaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"258\", \"Lentibulariaceae\", \"Lentibulariaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"274\", \"Potamogetonaceae\", \"Potamogetonaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"281\", \"Bromeliaceae\", \"Bromeliaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"285\", \"Ranunculaceae\", \"Ranunculaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"291\", \"Grossulariaceae\", \"Grossulariaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"298\", \"Cyperaceae\", \"Cyperaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"305\", \"Iridaceae\", \"Iridaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"315\", \"Valerianaceae\", \"Valerianaceae\");");
        db.execSQL("INSERT INTO familias (id, nombre, nombre_norm) VALUES (\"329\", \"Violaceae\", \"Violaceae\");");


        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"6\", \"Arcytophyllum\", \"Arcytophyllum\", \"5\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"15\", \"Arracacia\", \"Arracacia\", \"14\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"18\", \"Azorella\", \"Azorella\", \"14\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"22\", \"Baccharis\", \"Baccharis\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"29\", \"Bartsia\", \"Bartsia\", \"28\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"33\", \"Bartsia\", \"Bartsia\", \"32\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"39\", \"Berberis\", \"Berberis\", \"38\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"43\", \"Bomarea\", \"Bomarea\", \"42\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"48\", \"Brachyotum\", \"Brachyotum\", \"47\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"54\", \"Calamagrostis\", \"Calamagrostis\", \"53\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"59\", \"Calandrinia\", \"Calandrinia\", \"58\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"63\", \"Calceolaria\", \"Calceolaria\", \"62\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"67\", \"Cardamine\", \"Cardamine\", \"66\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"71\", \"Castilleja\", \"Castilleja\", \"32\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"75\", \"Cerastium\", \"Cerastium\", \"74\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"78\", \"Chuquiraga\", \"Chuquiraga\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"82\", \"Clinopodium\", \"Clinopodium\", \"81\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"85\", \"Diplostephium\", \"Diplostephium\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"91\", \"Disterigma\", \"Disterigma\", \"90\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"94\", \"Dorobaea\", \"Dorobaea\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"97\", \"Draba\", \"Draba\", \"66\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"101\", \"Ephedra\", \"Ephedra\", \"100\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"105\", \"Epidendrum\", \"Epidendrum\", \"104\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"109\", \"Equisetum\", \"Equisetum\", \"108\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"112\", \"Eryngium\", \"Eryngium\", \"14\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"116\", \"Escalonia\", \"Escalonia\", \"115\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"120\", \"Fuchsia\", \"Fuchsia\", \"119\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"123\", \"Galium\", \"Galium\", \"5\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"128\", \"Gentiana\", \"Gentiana\", \"127\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"131\", \"Gentianella\", \"Gentianella\", \"127\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"143\", \"Geranium\", \"Geranium\", \"142\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"147\", \"Geum\", \"Geum\", \"146\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"150\", \"Gynoxys\", \"Gynoxys\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"155\", \"Halenia\", \"Halenia\", \"127\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"158\", \"Hesperomeles\", \"Hesperomeles\", \"146\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"161\", \"Hieracium\", \"Hieracium\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"165\", \"Huperzia\", \"Huperzia\", \"164\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"169\", \"Hypericum\", \"Hypericum\", \"168\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"172\", \"Hypochaeris\", \"Hypochaeris\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"177\", \"Isoëtes\", \"Isoetes\", \"176\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"181\", \"Jamesonia\", \"Jamesonia\", \"180\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"184\", \"Lachemilla\", \"Lachemilla\", \"146\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"189\", \"Loricaria\", \"Loricaria\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"193\", \"Lupinus\", \"Lupinus\", \"192\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"197\", \"Lysipomia\", \"Lysipomia\", \"196\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"200\", \"Miconia\", \"Miconia\", \"47\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"206\", \"Monnina\", \"Monnina\", \"205\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"210\", \"Myriophyllum\", \"Myriophyllum\", \"209\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"214\", \"Neurolepis\", \"Neurolepis\", \"213\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"218\", \"Nototriche\", \"Nototriche\", \"217\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"221\", \"Oritrophium\", \"Oritrophium\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"225\", \"Ourisia\", \"Ourisia\", \"224\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"229\", \"Oxalis\", \"Oxalis\", \"228\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"232\", \"Paspalum\", \"Paspalum\", \"213\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"237\", \"Passiflora\", \"Passiflora\", \"236\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"240\", \"Pedicularis\", \"Pedicularis\", \"32\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"243\", \"Pentacalia\", \"Pentacalia\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"249\", \"Peperomia\", \"Peperomia\", \"248\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"252\", \"Perezia\", \"Perezia\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"255\", \"Pernettya\", \"Pernettya\", \"90\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"259\", \"Pinguicula\", \"Pinguicula\", \"258\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"262\", \"Plantago\", \"Plantago\", \"224\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"267\", \"Pleurothallis\", \"Pleurothallis\", \"104\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"271\", \"Polylepis\", \"Polylepis\", \"146\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"275\", \"Potamogeton\", \"Potamogeton\", \"274\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"278\", \"Potentilla\", \"Potentilla\", \"146\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"282\", \"Puya\", \"Puya\", \"281\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"286\", \"Ranunculus\", \"Ranunculus\", \"285\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"292\", \"Ribes\", \"Ribes\", \"291\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"295\", \"Rubus\", \"Rubus\", \"146\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"299\", \"Scirpus\", \"Scirpus\", \"298\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"302\", \"Senecio\", \"Senecio\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"306\", \"Sisyrinchium\", \"Sisyrinchium\", \"305\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"309\", \"Stachys\", \"Stachys\", \"81\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"312\", \"Vaccinium\", \"Vaccinium\", \"90\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"316\", \"Valeriana\", \"Valeriana\", \"315\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"330\", \"Viola\", \"Viola\", \"329\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"333\", \"Werneria\", \"Werneria\", \"21\");");
        db.execSQL("INSERT INTO generos (id, nombre, nombre_norm, familia_id) VALUES (\"340\", \"Xenophyllum\", \"Xenophyllum\", \"21\");");


        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"19\", \"pedunculata\", \"pedunculata\", \"18\", \"12\", \"null\", \"4\", \"13\", \"1703586\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"25\", \"genistelloides\", \"genistelloides\", \"22\", \"3\", \"null\", \"9\", \"null\", \"2722899\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"40\", \"lutea\", \"lutea\", \"39\", \"36\", \"37\", \"9\", \"null\", \"3500102\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"64\", \"rosmarinifolia\", \"rosmarinifolia\", \"63\", \"36\", \"null\", \"13\", \"null\", \"29200665\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"68\", \"jamesonii\", \"jamesonii\", \"67\", \"27\", \"null\", \"13\", \"null\", \"4101583\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"83\", \"nubigenum\", \"nubigenum\", \"82\", \"46\", \"null\", \"57\", \"13\", \"17605046\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"72\", \"fissifolia\", \"fissifolia\", \"71\", \"70\", \"12\", \"13\", \"null\", \"29202221\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"121\", \"vulcanica\", \"vulcanica\", \"120\", \"27\", \"null\", \"9\", \"null\", \"23200095\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"95\", \"pimpinellifolia\", \"pimpinellifolia\", \"94\", \"36\", \"null\", \"13\", \"57\", \"2733220\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"102\", \"americana\", \"americana\", \"101\", \"12\", \"70\", \"13\", \"null\", \"14300006\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"110\", \"bogotense\", \"bogotense\", \"109\", \"12\", \"null\", \"13\", \"null\", \"26605510\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"144\", \"multipartitum\", \"multipartitum\", \"143\", \"27\", \"3\", \"57\", \"13\", \"13900186\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"124\", \"hypocarpium\", \"hypocarpium\", \"123\", \"37\", \"null\", \"13\", \"null\", \"27909757\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"148\", \"peruvianum\", \"peruvianum\", \"147\", \"36\", \"null\", \"13\", \"57\", \"27800530\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"159\", \"obtusifolia\", \"obtusifolia\", \"158\", \"3\", \"27\", \"9\", \"null\", \"27801590\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"178\", \"novo-granadensis\", \"novo-granadensis\", \"177\", \"12\", \"null\", \"175\", \"13\", \"26619689\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"170\", \"aciculare\", \"aciculare\", \"169\", \"36\", \"null\", \"9\", \"null\", \"7800273\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"185\", \"hispidula\", \"hispidula\", \"184\", \"27\", \"null\", \"13\", \"null\", \"27806115\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"194\", \"microphyllus\", \"microphyllus\", \"193\", \"46\", \"null\", \"9\", \"null\", \"13037051\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"187\", \"orbiculata\", \"orbiculata\", \"184\", \"12\", \"27\", \"57\", \"null\", \"27801673\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"230\", \"phaeotricha\", \"phaeotricha\", \"229\", \"36\", \"null\", \"13\", \"null\", \"23700365\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"215\", \"villosa\", \"villosa\", \"214\", \"51\", \"null\", \"52\", \"13\", \"50077093\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"222\", \"crocifolium\", \"crocifolium\", \"221\", \"3\", \"36\", \"13\", \"57\", \"2738138\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"140\", \"rapunculoides\", \"rapunculoides\", \"131\", \"46\", \"null\", \"13\", \"null\", \"13800128\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"241\", \"incurva\", \"incurva\", \"240\", \"27\", \"null\", \"13\", \"null\", \"29206498\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"244\", \"arbutifolia\", \"arbutifolia\", \"243\", \"36\", \"null\", \"9\", \"null\", \"2733350\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"250\", \"graveolens\", \"graveolens\", \"249\", \"51\", \"12\", \"13\", \"null\", \"25004174\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"7\", \"filiforme\", \"filiforme\", \"6\", \"3\", \"null\", \"4\", \"null\", \"27904079\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"23\", \"caespitosa\", \"caespitosa\", \"22\", \"3\", \"null\", \"9\", \"null\", \"2722892\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"10\", \"vernicosum\", \"vernicosum\", \"6\", \"3\", \"null\", \"9\", \"null\", \"27909983\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"16\", \"elata\", \"elata\", \"15\", \"12\", \"null\", \"13\", \"null\", \"1700589\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"30\", \"laticrenata\", \"laticrenata\", \"29\", \"12\", \"27\", \"13\", \"null\", \"29200505\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"34\", \"pedicularioides\", \"pedicularioides\", \"33\", \"27\", \"null\", \"13\", \"null\", \"29200512\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"44\", \"glaucescens\", \"glaucescens\", \"43\", \"27\", \"null\", \"13\", \"null\", \"1200216\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"265\", \"rigida\", \"rigida\", \"262\", \"51\", \"12\", \"13\", \"null\", \"25200233\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"256\", \"prostrata\", \"prostrata\", \"255\", \"3\", \"27\", \"9\", \"null\", \"50066293\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"263\", \"australis\", \"australis\", \"262\", \"51\", \"12\", \"13\", \"null\", \"25200077\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"268\", \"coriacardia\", \"coriacardia\", \"267\", \"51\", \"null\", \"13\", \"null\", \"23508782\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"279\", \"dombeyi\", \"dombeyi\", \"278\", \"36\", \"null\", \"13\", \"null\", \"27800748\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"283\", \"clava-herculis\", \"clava-herculis\", \"282\", \"126\", \"null\", \"57\", \"null\", \"4302804\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"300\", \"rigidus\", \"rigidus\", \"299\", \"51\", \"null\", \"175\", \"null\", \"9902187\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"296\", \"coriaceus\", \"coriaceus\", \"295\", \"27\", \"null\", \"9\", \"null\", \"27800796\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"310\", \"elliptica\", \"elliptica\", \"309\", \"27\", \"46\", \"57\", \"null\", \"17601420\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"323\", \"plantaginea\", \"plantaginea\", \"316\", \"3\", \"null\", \"13\", \"57\", \"33500246\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"319\", \"henricii\", \"henricii\", \"316\", \"3\", \"null\", \"57\", \"13\", \"33500711\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"327\", \"secunda\", \"secunda\", \"316\", \"3\", \"null\", \"13\", \"57\", \"33500758\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"336\", \"pumila\", \"pumila\", \"333\", \"36\", \"null\", \"13\", \"57\", \"2709070\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"341\", \"humile\", \"humile\", \"340\", \"3\", \"36\", \"57\", \"13\", \"50101822\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"173\", \"sessiliflora\", \"sessiliflora\", \"172\", \"3\", \"36\", \"57\", \"null\", \"2702714\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"132\", \"cerastioides\", \"cerastioides\", \"131\", \"46\", \"null\", \"13\", \"null\", \"13800126\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"134\", \"hirculus\", \"hirculus\", \"131\", \"37\", \"null\", \"13\", \"null\", \"13801755\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"138\", \"longibarbata\", \"longibarbata\", \"131\", \"12\", \"null\", \"13\", \"null\", \"13801791\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"136\", \"hyssopofila\", \"hyssopofila\", \"131\", \"37\", \"null\", \"13\", \"null\", \"13801759\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"151\", \"cuicochensis\", \"cuicochensis\", \"150\", \"36\", \"null\", \"9\", \"null\", \"2743344\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"153\", \"miniphylla\", \"miniphylla\", \"150\", \"36\", \"null\", \"9\", \"null\", \"2733857\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"156\", \"serpyllifolia\", \"serpyllifolia\", \"155\", \"12\", \"null\", \"13\", \"null\", \"13803175\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"162\", \"frigidum\", \"frigidum\", \"161\", \"36\", \"null\", \"13\", \"null\", \"2737073\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"166\", \"crassa\", \"crassa\", \"165\", \"27\", \"70\", \"13\", \"null\", \"26608478\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"182\", \"goudotii\", \"goudotii\", \"181\", \"12\", \"null\", \"13\", \"null\", \"26606391\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"190\", \"thuyoides\", \"thuyoides\", \"189\", \"3\", \"51\", \"9\", \"null\", \"2727069\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"198\", \"vitreola\", \"vitreola\", \"197\", \"3\", \"null\", \"4\", \"null\", \"5502024\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"201\", \"chionophila\", \"chionophila\", \"200\", \"3\", \"null\", \"13\", \"4\", \"20301006\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"98\", \"steyermarkii\", \"steyermarkii\", \"97\", \"27\", \"null\", \"13\", \"null\", \"4104861\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"49\", \"jamesonii\", \"jamesonii\", \"48\", \"46\", \"null\", \"9\", \"null\", \"20300412\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"88\", \"glandulosum\", \"glandulosum\", \"85\", \"36\", \"null\", \"9\", \"null\", \"2705432\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"92\", \"empetrifolium\", \"empetrifolium\", \"91\", \"27\", \"3\", \"13\", \"null\", \"12300816\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"55\", \"intermedia\", \"intermedia\", \"54\", \"51\", \"null\", \"52\", \"13\", \"25512876\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"60\", \"acaulis\", \"acaulis\", \"59\", \"27\", \"46\", \"57\", \"null\", \"26200245\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"76\", \"floccosum\", \"floccosum\", \"75\", \"3\", \"null\", \"13\", \"null\", \"6300620\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"79\", \"jussieui\", \"jussieui\", \"78\", \"37\", \"null\", \"9\", \"null\", \"2728050\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"86\", \"ericoides\", \"ericoides\", \"85\", \"36\", \"null\", \"9\", \"null\", \"2733144\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"106\", \"tenuicaule\", \"tenuicaule\", \"105\", \"51\", \"null\", \"13\", \"null\", \"23509220\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"117\", \"myrtilloides\", \"myrtilloides\", \"116\", \"12\", \"36\", \"9\", \"null\", \"29100693\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"113\", \"humile\", \"humile\", \"112\", \"46\", \"12\", \"57\", \"null\", \"1701117\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"129\", \"sedifolia\", \"sedifolia\", \"128\", \"126\", \"null\", \"13\", \"null\", \"13800257\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"203\", \"salicifolia\", \"salicifolia\", \"200\", \"3\", \"null\", \"9\", \"null\", \"20302462\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"207\", \"crassifolia\", \"crassifolia\", \"206\", \"46\", \"null\", \"9\", \"null\", \"25900130\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"211\", \"quitense\", \"quitense\", \"210\", \"51\", \"12\", \"175\", \"null\", \"15000089\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"219\", \"hartwegii\", \"hartwegii\", \"218\", \"46\", \"null\", \"4\", \"null\", \"19600568\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"226\", \"chamaedrifolia\", \"chamaedrifolia\", \"225\", \"27\", \"null\", \"57\", \"13\", \"29202277\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"233\", \"bonplandianum\", \"bonplandianum\", \"232\", \"51\", \"null\", \"52\", \"13\", \"25513101\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"238\", \"cumbalensis\", \"cumbalensis\", \"237\", \"27\", \"46\", \"235\", \"null\", \"24200022\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"253\", \"pungens\", \"pungens\", \"252\", \"46\", \"null\", \"57\", \"13\", \"2707362\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"260\", \"calyptrata\", \"calyptrata\", \"259\", \"46\", \"null\", \"57\", \"13\", \"18300017\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"246\", \"vacciniodes\", \"vacciniodes\", \"243\", \"36\", \"null\", \"9\", \"null\", \"2733432\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"272\", \"reticulata\", \"reticulata\", \"271\", \"12\", \"null\", \"270\", \"null\", \"27800836\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"276\", \"paramoanus\", \"paramoanus\", \"275\", \"51\", \"null\", \"175\", \"null\", \"26300337\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"287\", \"flagelliformis\", \"flagelliformis\", \"286\", \"36\", \"null\", \"175\", \"null\", \"27100571\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"313\", \"floribundum\", \"floribundum\", \"312\", \"27\", \"3\", \"9\", \"null\", \"12300488\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"317\", \"cernua\", \"cernua\", \"316\", \"3\", \"null\", \"13\", \"57\", \"33500749\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"293\", \"lehmannii\", \"lehmannii\", \"292\", \"27\", \"70\", \"9\", \"null\", \"29100299\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"289\", \"praemorsus\", \"praemorsus\", \"286\", \"36\", \"null\", \"13\", \"null\", \"27101034\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"303\", \"chionogeton\", \"chionogeton\", \"302\", \"36\", \"null\", \"13\", \"null\", \"2743430\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"307\", \"palustre\", \"palustre\", \"306\", \"36\", \"null\", \"13\", \"null\", \"16600587\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"334\", \"nubigena\", \"nubigena\", \"333\", \"3\", \"null\", \"4\", \"57\", \"2711495\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"321\", \"microphylla\", \"microphylla\", \"316\", \"3\", \"null\", \"9\", \"null\", \"33500229\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"331\", \"pygmea\", \"pygmea\", \"330\", \"46\", \"3\", \"4\", \"null\", \"33800244\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"325\", \"rigida\", \"rigida\", \"316\", \"3\", \"null\", \"4\", \"57\", \"33500386\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"338\", \"pygmaea\", \"pygmaea\", \"333\", \"3\", \"36\", \"4\", \"57\", \"2723559\");");
        db.execSQL("INSERT INTO especies (id, nombre, nombre_norm, genero_id, color1_id, color2_id, forma_vida1_id, forma_vida2_id, id_tropicos) VALUES (\"343\", \"roseum\", \"roseum\", \"340\", \"27\", \"36\", \"4\", \"null\", \"50101826\");");


        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"141\", \"0.0\", \"0.0\", \"140\", \"1\", \"genti_rapu.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"251\", \"0.0\", \"0.0\", \"250\", \"1\", \"pepe_grav.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"8\", \"-2.783\", \"-79.2239167\", \"7\", \"1\", \"arcyt_fili.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"20\", \"-2.7822223\", \"-79.2238889\", \"19\", \"1\", \"azor_pedu.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"17\", \"-2.8439722\", \"-79.1437194\", \"16\", \"1\", \"arrac_elat.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"26\", \"-2.7858333\", \"-79.205\", \"25\", \"1\", \"bacch_geni.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"24\", \"-2.7822223\", \"-79.2233334\", \"23\", \"1\", \"bacch_caes.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"41\", \"-2.8333333\", \"-79.3333333\", \"40\", \"1\", \"berb_lute.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"31\", \"-2.7794445\", \"-79.2216667\", \"30\", \"1\", \"bart_lati.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"35\", \"-2.783\", \"-79.2239167\", \"34\", \"1\", \"barts_pedi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"45\", \"-2.783\", \"-79.2239167\", \"44\", \"1\", \"bomarea_glaucesc_copy.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"50\", \"-2.783\", \"-79.2239167\", \"49\", \"1\", \"brac_jame.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"61\", \"-2.7775\", \"-79.2416666\", \"60\", \"1\", \"call_acau.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"56\", \"-2.186\", \"-79.2667\", \"55\", \"1\", \"cala_inte.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"65\", \"-2.7794445\", \"-79.2216667\", \"64\", \"1\", \"calce_rosm.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"69\", \"-2.7822223\", \"-79.2238889\", \"68\", \"1\", \"card_jame.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"80\", \"-2.95\", \"-79.3\", \"79\", \"1\", \"chuqu_juss.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"77\", \"-2.75\", \"-79.25\", \"76\", \"1\", \"cerast_floc.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"84\", \"-2.7858333\", \"-79.205\", \"83\", \"1\", \"clino_nubi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"89\", \"-2.91666\", \"-79.21666\", \"88\", \"1\", \"dipl_glan.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"87\", \"-2.783\", \"-79.2239167\", \"86\", \"1\", \"diplos_eric.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"99\", \"-2.7816667\", \"-79.2377777\", \"98\", \"1\", \"drab_stey.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"93\", \"-2.7833333\", \"-79.2\", \"92\", \"1\", \"dist_empe.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"96\", \"-2.7822223\", \"-79.2238889\", \"95\", \"1\", \"dorob_pimp.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"107\", \"-2.7858333\", \"-79.205\", \"106\", \"1\", \"epid_tenui.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"103\", \"-2.79888\", \"-79.30472\", \"102\", \"1\", \"ephe_ame.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"114\", \"-2.93333\", \"-79.16666\", \"113\", \"1\", \"erin_humi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"111\", \"-2.81111\", \"-79.24361\", \"110\", \"1\", \"equise_bog.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"122\", \"-2.7833333\", \"-79.2\", \"121\", \"1\", \"fuch_loxe.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"118\", \"-2.8333333\", \"-79.2166667\", \"117\", \"1\", \"esca_myrt.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"133\", \"-2.8\", \"-79.13333\", \"132\", \"1\", \"genti_cera.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"125\", \"-2.7822223\", \"-79.2233334\", \"124\", \"1\", \"gali_hypo.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"130\", \"-2.81666\", \"-79.16666\", \"129\", \"1\", \"gent_sedi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"137\", \"-2.8025\", \"-79.21833\", \"136\", \"1\", \"genti_hyss.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"135\", \"-2.783\", \"-79.2239167\", \"134\", \"1\", \"genti_hirc.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"145\", \"-2.8025\", \"-79.21833\", \"144\", \"1\", \"gera_mult.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"139\", \"-2.95\", \"-79.3\", \"138\", \"1\", \"gent_long.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"149\", \"-2.7\", \"-79.26666\", \"148\", \"1\", \"geum_per.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"154\", \"-2.783\", \"-79.2239167\", \"153\", \"1\", \"gynox_mini.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"157\", \"-2.8\", \"-79.13333\", \"156\", \"1\", \"hale_serp.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"160\", \"-2.8025\", \"-79.21833\", \"159\", \"1\", \"hesper_obt.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"174\", \"-2.8025\", \"-79.21833\", \"173\", \"1\", \"hypoc_sess.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"163\", \"-2.7822223\", \"-79.2238889\", \"162\", \"1\", \"hiera_frig.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"167\", \"-2.90166\", \"-79.26111\", \"166\", \"1\", \"huper_crass.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"171\", \"-2.7822223\", \"-79.2238889\", \"170\", \"1\", \"hyper_acic.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"179\", \"-2.783\", \"-79.2239167\", \"178\", \"1\", \"isoet_novo.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"183\", \"-2.91666\", \"-79.21666\", \"182\", \"1\", \"james_goud.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"186\", \"-2.76666\", \"-79.21666\", \"185\", \"1\", \"lach_hisp.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"191\", \"-2.93333\", \"-79.26666\", \"190\", \"1\", \"loric_thuy.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"188\", \"-2.8\", \"-79.28333\", \"187\", \"1\", \"lache_orbi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"195\", \"-2.66666\", \"-79.38333\", \"194\", \"1\", \"lupi_micr.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"204\", \"-2.783\", \"-79.2239167\", \"203\", \"1\", \"micon_sali.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"199\", \"-2.7822223\", \"-79.2233334\", \"198\", \"1\", \"lysip_vit.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"202\", \"-2.783\", \"-79.2239167\", \"201\", \"1\", \"micon_chio.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"208\", \"-2.7\", \"-79.26666\", \"207\", \"1\", \"monn_cras.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"216\", \"-1.75\", \"-78.45\", \"215\", \"1\", \"neur_vill.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"212\", \"-2.8439722\", \"-79.1437194\", \"211\", \"1\", \"myrio_quit.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"220\", \"-2.7775\", \"-79.2416666\", \"219\", \"1\", \"noto_hart.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"227\", \"-2.66666\", \"-79.25\", \"226\", \"1\", \"ouri_cham.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"223\", \"-2.783\", \"-79.2239167\", \"222\", \"1\", \"oritr_croci.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"231\", \"-2.8\", \"-79.13333\", \"230\", \"1\", \"oxal_phae.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"254\", \"-2.78583\", \"-79.205\", \"253\", \"1\", \"pere_pung.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"239\", \"-2.75\", \"-79.1666667\", \"238\", \"1\", \"passi_cumb.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"242\", \"-2.7822223\", \"-79.2233334\", \"241\", \"1\", \"pedi_inc.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"245\", \"-2.8025\", \"-79.21833\", \"244\", \"1\", \"penta_arbu.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"247\", \"-2.783\", \"-79.2239167\", \"246\", \"1\", \"penta_vacc.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"257\", \"-2.7905555\", \"-79.2011111\", \"256\", \"1\", \"pern_pros.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"261\", \"-2.7822223\", \"-79.2233334\", \"260\", \"1\", \"pingu_caly.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"266\", \"-2.7858333\", \"-79.205\", \"265\", \"1\", \"plant_rigi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"264\", \"-2.7925\", \"-79.2008333\", \"263\", \"1\", \"plant_aust.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"269\", \"-2.7858333\", \"-79.205\", \"268\", \"1\", \"pleur_cori.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"273\", \"-2.7822223\", \"-79.2238889\", \"272\", \"1\", \"poly_reti.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"280\", \"-2.7925\", \"-79.2008333\", \"279\", \"1\", \"poten_domb.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"277\", \"-2.81111\", \"-79.24361\", \"276\", \"1\", \"potam_para.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"284\", \"-2.7858333\", \"-79.205\", \"283\", \"1\", \"puya_clav.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"288\", \"-2.7822223\", \"-79.2233334\", \"287\", \"1\", \"ranu_flag.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"290\", \"-2.7833\", \"-79.33\", \"289\", \"1\", \"ranuc_prae.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"294\", \"-0.78333\", \"-79.23333\", \"293\", \"1\", \"ribe_lehm.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"301\", \"-2.7858333\", \"-79.205\", \"300\", \"1\", \"scir_rigi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"297\", \"-2.7858333\", \"-79.205\", \"296\", \"1\", \"rubu_cori.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"308\", \"-2.7925\", \"-79.2008333\", \"307\", \"1\", \"sisyr_palu.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"304\", \"-2.783\", \"-79.2239167\", \"303\", \"1\", \"senec_chio.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"11\", \"-2.7849444\", \"-79.2055\", \"10\", \"1\", \"arcyt_vern.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"73\", \"-2.7775\", \"-79.2416666\", \"72\", \"1\", \"casti_fiss.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"152\", \"-2.9\", \"-79.28333\", \"151\", \"1\", \"gynox_cuic.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"234\", \"-2.7849444\", \"-79.2055\", \"233\", \"1\", \"pasp_humb.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"311\", \"-2.7822223\", \"-79.2238889\", \"310\", \"1\", \"stac_elli.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"332\", \"-2.783\", \"-79.2239167\", \"331\", \"1\", \"viola_pyg.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"314\", \"-2.9077778\", \"-79.2836111\", \"313\", \"1\", \"vacc_flor.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"335\", \"-2.93333\", \"-79.3\", \"334\", \"1\", \"wern_nubi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"318\", \"-2.7858333\", \"-79.205\", \"317\", \"1\", \"valer_cer.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"320\", \"-2.7694445\", \"-79.2447222\", \"319\", \"1\", \"valer_henr.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"337\", \"-2.7925\", \"-79.2008333\", \"336\", \"1\", \"werne_pumi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"339\", \"-2.7775\", \"-79.2416666\", \"338\", \"1\", \"wern_pygm.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"342\", \"-2.7858333\", \"-79.205\", \"341\", \"1\", \"xenop_hum.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"344\", \"-2.7775\", \"-79.2416666\", \"343\", \"1\", \"xenop_rose.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"322\", \"-2.7833\", \"-79.33\", \"321\", \"1\", \"valer_micr.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"324\", \"-2.7822223\", \"-79.2238889\", \"323\", \"1\", \"valer_plantaginea_copy.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"326\", \"-2.83333\", \"-79.2\", \"325\", \"1\", \"valer_rigi.jpg\");");
        db.execSQL("INSERT INTO fotos (id, latitud, longitud, especie_id, lugar_id, path) VALUES (\"328\", \"-2.7775\", \"-79.2416666\", \"327\", \"1\", \"valer_secu.jpg\");");

    }

//    public void export() {
//        File f = new File(context.getFilesDir().getParent() + "/databases/" + DATABASE_NAME);
//        FileInputStream fis = null;
//        FileOutputStream fos = null;
//
//        try {
//            fis = new FileInputStream(f);
//            fos = new FileOutputStream("/mnt/sdcard/db_dump_" + DATABASE_NAME + ".db");
////            Environment.getExternalStorageDirectory().getPath();
//            while (true) {
//                int i = fis.read();
//                if (i != -1) {
//                    fos.write(i);
//                } else {
//                    break;
//                }
//            }
//            fos.flush();
//            Toast.makeText(context, "DB dump OK", Toast.LENGTH_LONG).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, "DB dump ERROR", Toast.LENGTH_LONG).show();
//        } finally {
//            try {
//                fos.close();
//                fis.close();
//            } catch (IOException ioe) {
//            }
//        }
//    }

    protected void logQuery(String log, String query) {
//        Log.e(log, query);
    }
}
