package com.lzm.Cajas;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.*;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;
import com.lzm.Cajas.db.*;
import com.lzm.Cajas.image.*;
import com.lzm.Cajas.listeners.FieldListener;
import com.lzm.Cajas.utils.*;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapActivity extends Activity implements Button.OnClickListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
    /*DRAWER*/
    private DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mOptionsArray;

    public int activeFragment = 0;
    public int posSearch = 0;
    public final int INICIO_POS = 0;
    public final int MAP_POS = 1;
    public final int ENCICLOPEDIA_POS = 2;
    public final int CAPTURA_POS = 3;
    public final int RUTAS_POS = 4;
    public final int TROPICOS_POS = 5;
    //    public final int NOTEPAD_POS = 6;
//    public final int NOTA_POS = 7;
    public final int TOOLS_POS = 6;
    public final int SETTINGS_POS = 7;

    public final int BUSQUEDA_POS = 18;

    public final int ABOUT_CAJAS_PARAMO_POS = 100;
    public final int ENCICLOPEDIA2_POS = 200;
    public final int SHOW_RUTA_POS = 300;
    public final int SHOW_ESPECIE_POS = 400;
    public final int BUGS_POS = 500;
    public final int COMMENTS_POS = 600;

    public String paramFrag = null;

    private static final int CAMERA_REQUEST = 1337;
    /*Interfaz*/
    private Button[] botones;
    boolean continente = true;
    List<SearchResult> result;
    Activity activity;
    /*Mapa*/
    private static GoogleMap map;
    private Polyline polyLine;
    private PolylineOptions rectOptions = new PolylineOptions().color(Color.RED);
    private static LatLng location;
    LocationClient locationClient;
    Marker lastPosition;
    HashMap<Marker, Foto> data;
    HashMap<Marker, EspecieUi> dataEspecies;
    Marker selected;
    int tipoMapa = 0;

    /*Fin mapa*/

    Foto imageToUpload;
    List<Especie> especiesBusqueda;
    List<Especie> especies;
    AlertDialog dialog;
    public int screenHeight;
    public int screenWidth;
    public static final String PREFS_NAME = "IkiamSettings";
    public String userId;
    public String name;
    public String type;
    public String errorMessage;
    public String ruta_remote_id;
    public String old_id;

    /*Service */
    Messenger mService = null;
    boolean mIsBound;
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    Boolean status = false;
    Boolean attached = false;
    Ruta ruta;

    /*Images*/
    private ImageTableObserver camera;
    int lastestImageIndex = 0;
    ImageItem imageItem;
    List<Bitmap> imagenes;
    int lastIndex = -1;
    int lastSize = 0;
    List<Foto> fotos;
    /*Fin imagenes*/

    List<FieldListener> listeners = new ArrayList<FieldListener>();

    public int enciclopediaListHeight = 0;
    public boolean enciclopediaPause = false;

    public Foto fotoSinCoords;

    public void setRuta_remote_id(String id) {
        fireEvent("ruta_remote_id", id);
        old_id = ruta_remote_id;
        this.ruta_remote_id = id;
    }

    public void setUserId(String id) {
        fireEvent("userId", id);
        this.userId = id;
    }

    public void setType(String type) {
        fireEvent("type", type);
        this.type = type;
    }

    public void setErrorMessage(String msg) {
        // System.out.println("::: SET ERROR MESSAGE::: " + msg);
        fireEvent("errorMessage", msg);
        this.errorMessage = msg;
    }

    public void addListener(FieldListener l) {
        if (l != null) listeners.add(l);
    }

    public void fireEvent(String fieldName, String newValue) {
        for (FieldListener l : listeners) {
            l.fieldValueChanged(fieldName, newValue);
        }
    }

    public void showToast(final String msg) {
        final Activity a = this;
        if (a != null) {
            a.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(a, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DbHelper helper = new DbHelper(this);
        helper.getWritableDatabase();

        fotoSinCoords = null;
        imageToUpload = null;

        /*preferencias
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        userId = settings.getString("user", "-1");
        name = settings.getString("name", "-1");
        type = settings.getString("type", "-1");
        email = settings.getString("email", "-1");
        esCientifico = settings.getString("esCientifico", "-1");
        titulo = settings.getString("titulo", "-1");
        */
        //System.out.println("variables name "+userId+"  name "+name);
        setContentView(R.layout.activity_map);

        activeFragment = MAP_POS;
        this.activity = this;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        /*CORE*/
        locationClient = new LocationClient(this, this, this);
        locationClient.connect();
        setUpMapIfNeeded();
        data = new HashMap<Marker, Foto>();
        dataEspecies = new HashMap<Marker, EspecieUi>();
        result = new ArrayList<SearchResult>();

        botones = new Button[4];

        botones[0] = (Button) this.findViewById(R.id.btnService);
        botones[1] = (Button) this.findViewById(R.id.btnEspecies);
        botones[2] = (Button) this.findViewById(R.id.btnTipo);
        botones[3] = (Button) this.findViewById(R.id.btnLimpiar);

        for (int i = 0; i < botones.length; i++) {
            botones[i].setOnClickListener(this);
        }
        restoreMe(savedInstanceState);

        /*fin*/

        /*DRAWER*/
        mTitle = mDrawerTitle = getTitle();
        fotos = new ArrayList<Foto>();
        mOptionsArray = getResources().getStringArray(R.array.options_array);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        mDrawerList = (ListView) findViewById(R.id.left_drawer2);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mOptionsArray));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
                getActionBar().setTitle(R.string.menu_title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        /*FinDrawer*/

//        Utils.openFragment(this, new InicioFragment(), getString(R.string.inicio_title), null);
        selectItem(INICIO_POS);
    }

    private void restoreMe(Bundle state) {
        if (state != null) {
                /*Implementar el restor*/
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        System.out.println("---LUZMA--- ON Restore State active: " + activeFragment + "   saved: "
//                + (savedInstanceState == null ? "NONE" : savedInstanceState.getSerializable("activeFragment")));
//        if (savedInstanceState != null) {
        selectItem(Integer.parseInt(savedInstanceState.getSerializable("activeFragment").toString()));
//        }
    }

    @Override
    public void onResume() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        super.onSaveInstanceState(savedInstanceState);
//        System.out.println("---LUZMA--- ON Save State 1 active: " + activeFragment + "   saved: "
//                + (savedInstanceState == null ? "NONE" : savedInstanceState.getSerializable("activeFragment")));
        savedInstanceState.putSerializable("activeFragment", activeFragment);
//        System.out.println("---LUZMA--- ON Save State 2 active: " + activeFragment + "   saved: "
//                + (savedInstanceState == null ? "NONE" : savedInstanceState.getSerializable("activeFragment")));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == botones[0].getId()) {
            //service de rutas
            if (servicesConnected()) {
                if (!status) {
                    map.clear();
                    ruta = new Ruta(this, getString(R.string.ruta_nombre));
                    ruta.save();
                    this.startService(new Intent(this, SvtService.class));
                    doBindService();
                    //  sendMessageToService((int)ruta.id);
                    Location mCurrentLocation;
                    mCurrentLocation = locationClient.getLastLocation();
                    location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 19);
                    map.animateCamera(update);
                    polyLine = map.addPolyline(rectOptions);
                    botones[0].setText(getString(R.string.ruta_parar));
                    camera = new ImageTableObserver(new Handler(), this);
                    this.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, camera);
                    status = true;
                } else {
                    doUnbindService();
                    this.stopService(new Intent(this, SvtService.class));
                    botones[0].setText(getString(R.string.ruta_nueva));
                    ruta = null;
                    status = false;
                }
            }
        }

        if (v.getId() == botones[1].getId()) {
            //especies
            especies = Especie.list(this);
            location = new LatLng(-2.84360424, -79.2282486);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 11);
            map.animateCamera(update);
            int[] res = Utils.getSize(screenWidth);
            for (Especie especie : especies) {
                ExecutorService queue = Executors.newSingleThreadExecutor();
                queue.execute(new EspecieLoader(this, especie, res[0], res[1]));
            }
        }
        if (v.getId() == botones[2].getId()) {
            //tipo
            tipoMapa++;
            switch (tipoMapa) {
                case 0:
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case 1:
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case 2:
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case 3:
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                default:
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    tipoMapa = 0;
                    break;
            }
        }
        if (v.getId() == botones[3].getId()) {
            if (especies != null)
                especies.clear();
            if (dataEspecies != null)
                dataEspecies.clear();
            if (data != null)
                data.clear();
            map.clear();
        }
    }

    public void setPingEspecie(final EspecieUi ui, final LatLng posicion, final Bitmap imagen) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                int[] res = Utils.getSize(screenWidth);
                int w = res[0] + res[2];
                int h = res[1] + res[3];

//                System.out.println("w " + w + " h " + h);
                Bitmap bmp = Bitmap.createBitmap(w, h, conf);
                Canvas canvas1 = new Canvas(bmp);
                Paint color = new Paint();
                color.setTextSize(10);
                color.setColor(Color.BLACK);//modify canvas
                canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.pin3), 0, 0, color);
                canvas1.drawBitmap(imagen, 5, 4, color);
                Marker marker = map.addMarker(new MarkerOptions().position(posicion)
                        .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                        .title(ui.nombre));
                //EspecieUi especieUi = new EspecieUi(title, nombreEspecie, fotoDialog, likes, desc);
                dataEspecies.put(marker, ui);
            }
        });
    }

    /*Google services*/
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
//            Log.d("Location Updates",
//                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error code
            return false;
        }
    }

    /*location services*/
    @Override
    public void onConnected(Bundle dataBundle) {
        Location mCurrentLocation;
        mCurrentLocation = locationClient.getLastLocation();
        map.getMyLocation();
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        // Toast.makeText(this, "Disconnected. Please re-connect.",
        //Toast.LENGTH_SHORT).show();
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
//        System.out.println("error connection failed");
    }

    /*MAPS*/
    private void updatePolyLine(LatLng latLng) {
        List<LatLng> points = polyLine.getPoints();
        points.add(latLng);
        polyLine.setPoints(points);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng l : points) {
            builder.include(l);
        }
        LatLngBounds bounds = builder.build();
        int padding = (100);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);
    }

    public void setPing(final String title, final int likes, final double latitud, final double longitud, final Bitmap foto, final Bitmap fotoDialog, final String url, final String descripcion) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                final LatLng pos = new LatLng(latitud, longitud);
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap bmp = Bitmap.createBitmap(170, 126, conf);
                Canvas canvas1 = new Canvas(bmp);
                Paint color = new Paint();
                color.setTextSize(35);
                color.setColor(Color.BLACK);//modify canvas
                canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.pin3), 0, 0, color);
                canvas1.drawBitmap(foto, 5, 4, color);
                Marker marker = map.addMarker(new MarkerOptions().position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                        .anchor(0.5f, 1).title(title));
                //AtraccionUi atraccion = new AtraccionUi(title, fotoDialog, likes, url, descripcion);
                //atracciones.put(marker, atraccion);
            }
        });
    }

    public void setUpMapIfNeeded() {
        //System.out.println("setUpMap if needed" +map);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapF)).getMap();
        //System.out.println("setUpMap if needed despues" +map);
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapF)).getMap();
            // Check if we were successful in obtaining the map.
            if (map != null)
                setUpMap();
        } else {
            setUpMap();
        }
    }

    private void setUpMap() {
        //locationClient.getLastLocation();
        location = new LatLng(-1.6477220517969353, -78.46435546875);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 7);
        map.setMyLocationEnabled(true);
        map.animateCamera(update);
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //ExecutorService queue = Executors.newSingleThreadExecutor();
                //queue.execute(new EspecieLoader(this,especie));
            }
        });
        final Context context = this;
        final Settings sett = Settings.getSettings(context);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (dataEspecies.get(marker) != null) {
                    marker.showInfoWindow();
                    //System.out.println("especie " + marker + "  " + selected + "  " + (selected != marker));
                    if (selected == null) {
                        selected = marker;
                    } else {
                        if (selected.getId().equals(marker.getId())) {
                            View myView;
                            selected = null;
                            LayoutInflater inflater = activity.getLayoutInflater();
                            final EspecieUi current = dataEspecies.get(marker);
                            myView = inflater.inflate(R.layout.dialog, null);
                            ImageView img = (ImageView) myView.findViewById(R.id.image);
                            ExecutorService queue = Executors.newSingleThreadExecutor();
                            queue.execute(new EspecieDialogImageLoader((MapActivity) activity, current.resId, img));
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(current.nombre);
                            builder.setView(myView);
                            builder.setPositiveButton(R.string.especie_info_tropicos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    String url = sett.tropicosBase + current.tropicosId;
                                    try {
                                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(myIntent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            builder.setNegativeButton(R.string.dialog_btn_cerrar, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setNeutralButton(R.string.dlg_ver, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Fragment fragment = new EspecieInfoFragment();
                                    Bundle args = new Bundle();
                                    args.putLong("especie", current.idEspecie);
                                    String nombre = current.nombre;
                                    Utils.openFragment((MapActivity) context, fragment, nombre, args);
                                }
                            });
                            dialog = builder.create();
                            dialog.show();
                        } else {
                            selected = marker;
                        }
                    }
                }
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (dataEspecies.get(marker) != null) {
                    marker.showInfoWindow();
                    //System.out.println("especie " + marker + "  " + selected + "  " + (selected != marker));
                    if (selected == null) {
                        selected = marker;
                    } else {
                        if (selected.getId().equals(marker.getId())) {
                            View myView;
                            selected = null;
                            LayoutInflater inflater = activity.getLayoutInflater();
                            final EspecieUi current = dataEspecies.get(marker);
                            myView = inflater.inflate(R.layout.dialog, null);
                            ImageView img = (ImageView) myView.findViewById(R.id.image);
                            ExecutorService queue = Executors.newSingleThreadExecutor();
                            queue.execute(new EspecieDialogImageLoader((MapActivity) activity, current.resId, img));
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(current.nombre);
                            builder.setView(myView);
                            builder.setPositiveButton(R.string.especie_info_tropicos, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    String url = sett.tropicosBase + current.tropicosId;
                                    try {
                                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(myIntent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            builder.setNegativeButton(R.string.dialog_btn_cerrar, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            dialog = builder.create();
                            dialog.show();
                        } else {
                            selected = marker;
                        }
                    }
                    return true;
                }
                return true;
            }
        });
    }

    public void showError(final ProgressDialog pd, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                pd.dismiss();
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    public void setImgDialog(final Bitmap imagen, final ImageView img) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                img.setImageBitmap(imagen);
            }
        });
    }

    public void setImgDialogLoader(final Bitmap imagen, final ImageView img, final ProgressDialog loader) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                img.setImageBitmap(imagen);
                loader.dismiss();
            }
        });
    }

    private void updateFotoSinCoords(LatLng pos) {
     /*   Coordenada coord = new Coordenada(this);
        markerSubir.hideInfoWindow();
        coord.latitud = pos.latitude;
        coord.longitud = pos.longitude;
        coord.altitud = 0;
        coord.save();
        fotoSinCoords.setCoordenada_id(coord);
        fotoSinCoords.save();
        Toast.makeText(this, getString(R.string.map_foto_ubicada), Toast.LENGTH_LONG).show();
        if (markerSubir.isInfoWindowShown())
            markerSubir.hideInfoWindow();
        markerSubir.remove();
        markerSubir = null;
        fotoSinCoords = null;*/
    }

    /*DRAWER*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.search_btn_label).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.search_btn_label:
                Fragment fragment = new BusquedaFragment();
                Bundle args = new Bundle();
                Utils.openFragment(this, fragment, getString(R.string.busqueda_title), args);
                activeFragment = BUSQUEDA_POS;
                return true;
            case R.id.menu_help:
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.help_layout, null);

                final AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(v)
                        .setTitle(getString(R.string.help_help));
                builder.setPositiveButton(R.string.dialog_btn_cerrar, null);

                final AlertDialog d = builder.create();
                final TextView txt = (TextView) v.findViewById(R.id.help_container);

                switch (activeFragment) {
                    case INICIO_POS:
                        txt.setText(getString(R.string.help_inicio));
                        break;
                    case MAP_POS:
                        txt.setText(getString(R.string.help_map));
                        break;
                    case CAPTURA_POS:
                        txt.setText(getString(R.string.help_captura));
                        break;
                    case ENCICLOPEDIA_POS:
                        txt.setText(getString(R.string.help_enciclopedia));
                        break;
                    case RUTAS_POS:
                        txt.setText(getString(R.string.help_rutas));
                        break;
                    case TROPICOS_POS:
                        txt.setText(getString(R.string.help_tropicos));
                        break;
//                    case NOTEPAD_POS:
//                    case NOTA_POS:
//                        txt.setText(getString(R.string.help_notepad));
//                        break;
                    case SETTINGS_POS:
                        txt.setText(getString(R.string.help_configuracion));
                        break;
                    case BUSQUEDA_POS:
                        txt.setText(getString(R.string.help_busqueda));
                        break;
                    case TOOLS_POS:
                        txt.setText(getString(R.string.help_tools));
                        break;
                }

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button cerrar = d.getButton(AlertDialog.BUTTON_POSITIVE);
                        cerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                d.dismiss();
                            }
                        });
                    }
                });
                d.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // System.out.println("entro?");
            selectItem(position);
        }
    }

    public void selectItem(int position) {
        selectItem(position, true);
    }

    public void selectItem(int position, boolean drawer) {
        // update the main content by replacing fragments
        String title = "";
        Utils.hideSoftKeyboard(this);
        Fragment fragment = null;
        Bundle args = null;
        Boolean skyp = false;

        int overrideDrawer = -1;

        switch (position) {
            case INICIO_POS:
                fragment = new InicioFragment();
//                this.addListener((FieldListener) fragment);
                title = getString(R.string.inicio_title);
                activeFragment = INICIO_POS;
                break;
            case MAP_POS:
                if (activeFragment == MAP_POS)
                    skyp = true;
                fragment = null;
                title = getString(R.string.map_title);
                activeFragment = MAP_POS;
                break;
            case CAPTURA_POS:
                fragment = new CapturaFragment();
//                this.addListener((FieldListener) fragment);
                title = getString(R.string.captura_title);
                activeFragment = CAPTURA_POS;
                break;
            case ENCICLOPEDIA_POS:
                fragment = new EnciclopediaGridFragment();
                title = getString(R.string.encyclopedia_title);
                activeFragment = ENCICLOPEDIA_POS;
                break;
            case RUTAS_POS:
                fragment = new RutasFragment();
                title = getString(R.string.ruta_plural);
                activeFragment = RUTAS_POS;
                break;
            case TROPICOS_POS:
                fragment = new TropicosFragment();
                title = getString(R.string.especie_info_tropicos);
                activeFragment = TROPICOS_POS;
                break;
//            case NOTEPAD_POS:
//                fragment = new NotepadFragment();
//                title = getString(R.string.notepad_title);
//                activeFragment = NOTEPAD_POS;
//                break;
//            case NOTA_POS:
//                fragment = new NotaCreateFrgment();
//                args = new Bundle();
//                args.putLong("nota", -1);
//                title = getString(R.string.nota_create_title);
//                activeFragment = NOTA_POS;
//                break;
            case TOOLS_POS:
                fragment = new ToolsFragment();
                title = getString(R.string.tools_title);
                activeFragment = TOOLS_POS;
                break;
            case SETTINGS_POS:
                fragment = new SettingsFragment();
//                this.addListener((FieldListener) fragment);
                title = getString(R.string.settings_title);
                activeFragment = SETTINGS_POS;
                break;

            case ABOUT_CAJAS_PARAMO_POS:
                if (paramFrag != null) {
                    fragment = new AboutFragment();
                    if (Integer.parseInt(paramFrag) == AboutFragment.CAJAS) {
                        title = getString(R.string.inicio_about_cajas);
                    } else if (Integer.parseInt(paramFrag) == AboutFragment.PARAMO) {
                        title = getString(R.string.inicio_about_paramo);
                    }
                    args = new Bundle();
                    args.putInt("tipo", Integer.parseInt(paramFrag));
                    activeFragment = ABOUT_CAJAS_PARAMO_POS;
                    overrideDrawer = SETTINGS_POS;
                }
                break;
            case ENCICLOPEDIA2_POS:
                fragment = new EnciclopediaListFragment();
                title = getString(R.string.encyclopedia_title);
                activeFragment = ENCICLOPEDIA2_POS;
                overrideDrawer = ENCICLOPEDIA_POS;
                break;
            case SHOW_RUTA_POS:
                fragment = new RutaFragment();
                title = getString(R.string.rutas_title);
                activeFragment = SHOW_RUTA_POS;
                overrideDrawer = RUTAS_POS;
                break;
            case SHOW_ESPECIE_POS:
                if (paramFrag != null) {
                    fragment = new EspecieInfoFragment();
                    title = getString(R.string.especie_info_title);
                    args = new Bundle();
                    args.putLong("especie", Long.parseLong(paramFrag));
                    activeFragment = SHOW_ESPECIE_POS;
                    overrideDrawer = ENCICLOPEDIA_POS;
                }
                break;
            case BUGS_POS:
                fragment = new SettingsBugsFragment();
                title = getString(R.string.bugs_title);
                activeFragment = BUGS_POS;
                overrideDrawer = SETTINGS_POS;
                break;
            case COMMENTS_POS:
                fragment = new SettingsCommentsFragment();
                title = getString(R.string.comments_activity_title);
                activeFragment = COMMENTS_POS;
                overrideDrawer = SETTINGS_POS;
                break;

            default:
                fragment = null;
                break;
        }
        if (!skyp)
            Utils.openFragment(this, fragment, title, args);

        if (drawer) {
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        if (overrideDrawer > -1) {
            mDrawerList.setItemChecked(overrideDrawer, true);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public void showMap() {
        selectItem(MAP_POS);
    }

    public double pendiente(LatLng p1, LatLng p2) {
        double x1 = (p1.longitude + 180) * 360;
        double y1 = (p1.latitude + 90) * 180;
        double x2 = (p2.longitude + 180) * 360;
        double y2 = (p2.latitude + 90) * 180;
        double dX = x2 - x1;
        double dY = y2 - y1;
        return dY / dX;
    }

    public double distancia(LatLng p1, LatLng p2) {
        double x1 = (p1.longitude + 180) * 360;
        double y1 = (p1.latitude + 90) * 180;
        double x2 = (p2.longitude + 180) * 360;
        double y2 = (p2.latitude + 90) * 180;
        double dX = x2 - x1;
        double dY = y2 - y1;
        return Math.sqrt((dX * dX) + (dY * dY));
    }

    public int pointSort(LatLng p1, LatLng p2, LatLng upper) {
        // Exclude the 'upper' point from the sort (which should come first).
        if (p1 == upper) return -1;
        if (p2 == upper) return 1;

        // Find the slopes of 'p1' and 'p2' when a line is
        // drawn from those points through the 'upper' point.
        double m1 = pendiente(p1, upper);
        double m2 = pendiente(p2, upper);

        // 'p1' and 'p2' are on the same line towards 'upper'.
        if (m1 == m2) {
            // The point closest to 'upper' will come first.
//            return p1.distance(upper) < p2.distance(upper) ? -1 : 1;
            return (distancia(p1, upper) < distancia(p2, upper)) ? -1 : 1;
        }

        // If 'p1' is to the right of 'upper' and 'p2' is the the left.
        if (m1 <= 0 && m2 > 0) return -1;

        // If 'p1' is to the left of 'upper' and 'p2' is the the right.
        if (m1 > 0 && m2 <= 0) return 1;

        // It seems that both slopes are either positive, or negative.
        return m1 > m2 ? -1 : 1;
    }


    /*Service de rutas*/
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SvtService.MSG_SET_INT_VALUE:
//                    System.out.println("Int Message: " + msg.arg1);
                    break;
                case SvtService.MSG_SET_STRING_VALUE:
                    String str1 = msg.getData().getString("str1");
//                    textStrValue.setText("Str Message: " + str1);
//                    System.out.println("Str  Message: " + msg.arg1);
                    break;
                case SvtService.MSG_SET_COORDS:
                    Double latitud = msg.getData().getDouble("latitud");
                    Double longitud = msg.getData().getDouble("logitud");
//                    System.out.println("Str  Message recibed: " + latitud+"  "+longitud);
                    LatLng latlong = new LatLng(latitud, longitud);
                    if (lastPosition == null)
                        lastPosition = map.addMarker(new MarkerOptions().position(latlong).title("Última posición registrada"));
                    else
                        lastPosition.setPosition(latlong);
                    updatePolyLine(latlong);
                    if (lastestImageIndex != 0) {
                        //System.out.println("Tomo foto " + lastestImageIndex);
                        imageItem = getLatestItem();
                        getFoto();
                        if (imagenes.size() > lastSize) {
//                            map.addMarker(new MarkerOptions().position(latlong).title("Sydney").snippet("Population: 4,627,300").icon(BitmapDescriptorFactory.fromBitmap(imagenes.get(lastIndex))));
                            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                            Foto foto = new Foto(activity);
                            Coordenada cord = new Coordenada(activity, latitud, longitud);
                            cord.save();
                            foto.setCoordenada(cord);
                            foto.setRuta(ruta);
                            foto.path = imageItem.imagePath;
                            foto.save();
                            fotos.add(foto);
                            int[] res = Utils.getSize(screenWidth);
                            int w = res[0] + res[2];
                            int h = res[1] + res[3];
                            Bitmap bmp = Bitmap.createBitmap(w, h, conf);
                            Canvas canvas1 = new Canvas(bmp);
                            Paint color = new Paint();
                            color.setTextSize(35);
                            color.setColor(Color.BLACK);//modify canvas
                            canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                                    R.drawable.pin3), 0, 0, color);
                            canvas1.drawBitmap(imagenes.get(lastIndex), 5, 4, color);

                            Marker marker = map.addMarker(new MarkerOptions().position(latlong)
                                    .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                                    .anchor(0.5f, 1).title("Nueva fotografía"));

                            data.put(marker, foto);

                            lastSize = imagenes.size();
                            //queue.execute(new ImageUploader(activity, queue, imageItem, 0));
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);

            try {
                Message msg = Message.obtain(null, SvtService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
                attached = true;
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
            }
            if (ruta != null) {
                try {
                    Message msg = Message.obtain(null, SvtService.MSG_SET_INT_VALUE);
                    msg.replyTo = mMessenger;
                    msg.arg1 = (int) ruta.id;
                    mService.send(msg);
                    //System.out.println("Mando mensaje de ruta");
                    attached = true;
                } catch (RemoteException e) {
                    // In this case the service has crashed before we could even do anything with it
                }
            }

        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mService = null;
            // System.out.println("Disconnected.");
        }
    };

    /*IMAGES*/
    public void setImageIndex(int index) {
        //System.out.println("set image index " + index);
        if (imagenes == null) {
            imagenes = new ArrayList<Bitmap>();
        }
        if (index >= lastestImageIndex) {
            this.lastestImageIndex = index;
        } else {
            /*Borro una foto*/
            this.lastestImageIndex = 0;
        }
        //System.out.println("set image index fin "+this.lastestImageIndex);
    }

    /*Fotos*/
    public void getFoto() {
        if (imageItem != null) {
            // System.out.println("path "+imageItem.imagePath);
            //System.out.println("images " + imagenes);
            int[] res = Utils.getSize(screenWidth);
            Bitmap b = ImageUtils.decodeBitmapPath(imageItem.imagePath, res[0], res[1]);
            //System.out.println("width " + b.getWidth() + "  " + b.getHeight());
            imagenes.add(b);
            lastIndex++;
            lastestImageIndex = 0;
        }

    }

    public Bitmap getFotoDialog(Foto image, int width, int heigth) {
        if (image != null) {
            // System.out.println("path "+imageItem.imagePath);
            //System.out.println("images " + image.imagePath+"  "+width+"  "+heigth);
            Bitmap b = ImageUtils.decodeFile(image.path, width, heigth);
            return b;

        }
        return null;

    }

    public Bitmap getFotoDialogString(InputStream io, int width, int heigth) {
        if (io != null) {
            // System.out.println("path "+imageItem.imagePath);
            //System.out.println("images " + image.imagePath+"  "+width+"  "+heigth);
            Bitmap b = ImageUtils.decodeBitmap(io, width, heigth);
            return b;

        }
        return null;

    }

    public ImageItem getLatestItem() {
        // set vars
        if (lastestImageIndex > 0) {
            ImageItem item = null;
            String columns[] = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.MINI_THUMB_MAGIC};

            Uri image = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, lastestImageIndex);
            Cursor cursor = this.managedQuery(image, columns, null, null, null);

            // check if cursus has rows, if not break and exit loop
            if (cursor.moveToFirst()) {
                //System.out.println("tiene rows "+cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC)));
                item = new ImageItem();
                item.prefs = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
                item.imageId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                item.imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                item.imageName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                item.imageType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                item.imageSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            }
            //cursor.close();

            // System.out.println("salio");
            return item;
        }

        return null;

    }


    void doBindService() {
        this.bindService(new Intent(this, SvtService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, SvtService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            this.unbindService(mConnection);
            mIsBound = false;
        }
    }

    /*Funcion para ver rutas desde el list*/
    public void openRutaFragment(Ruta ruta) {
        this.ruta = ruta;
        this.old_id = null;
        Fragment fragment = new RutaFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            RelativeLayout mainLayout = (RelativeLayout) this.findViewById(R.id.rl2);
            mainLayout.setVisibility(LinearLayout.GONE);
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack("")
                    .commit();
        }
        setTitle(ruta.descripcion);
    }

    public void showRuta(List<Coordenada> cords, List<Foto> fotos, Fragment fragment) {
        map.clear();
        data.clear();
        location = new LatLng(cords.get(0).getLatitud(), cords.get(0).getLongitud());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 19);
        map.animateCamera(update);
        polyLine = map.addPolyline(rectOptions);
        int[] res = Utils.getSize(screenWidth);
        int w = res[0] + res[2];
        int h = res[1] + res[3];
        for (int i = 0; i < fotos.size(); i++) {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bmp = Bitmap.createBitmap(w, h, conf);
            ;
            Canvas canvas1 = new Canvas(bmp);
            Paint color = new Paint();
            color.setTextSize(35);
            color.setColor(Color.BLACK);//modify canvas
            canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.pin3), 0, 0, color);
            Bitmap b = ImageUtils.decodeBitmapPath(fotos.get(i).path, res[0], res[1]);
            ;
            canvas1.drawBitmap(b, 5, 4, color);
            Coordenada co = fotos.get(i).getCoordenada(this);
            location = new LatLng(co.getLatitud(), co.getLongitud());
            Marker marker = map.addMarker(new MarkerOptions().position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                    .anchor(0.5f, 1).title(getString(R.string.ruta_nueva_foto)));
            data.put(marker, fotos.get(i));
        }
        for (int i = 0; i < cords.size(); i++) {
            updatePolyLine(new LatLng(cords.get(i).getLatitud(), cords.get(i).getLongitud()));
            if (i == cords.size() - 1) {
                map.addMarker(new MarkerOptions().position(new LatLng(cords.get(i).getLatitud(), cords.get(i).getLongitud())).title("Última posición registrada"));
            }
        }
        this.onDestroy();
        showMap();

    }


    public void showSearchResults(final List<SearchResult> results, final ProgressDialog dialog) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                result = results;
                ListFragment fragment = new BusquedaTropicosResult();
                Utils.openFragment((MapActivity) activity, fragment, getString(R.string.busqueda_title));
            }
        });

    }
}