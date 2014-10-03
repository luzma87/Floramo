package com.lzm.Cajas;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lzm.Cajas.adapters.*;
import com.lzm.Cajas.capturaAutocomplete.CustomAutoCompleteView;
import com.lzm.Cajas.db.*;
import com.lzm.Cajas.image.ImageUtils;
import com.lzm.Cajas.listeners.*;
import com.lzm.Cajas.utils.GeoDegree;
import com.lzm.Cajas.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luz on 25/07/14.
 * <p/>
 * Based on the Example of loading an image into an image view using the image picker.
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 * http://www.airpair.com/android/android-image-picker-select-gallery-images
 * <p/>
 * image rotation
 * http://stackoverflow.com/questions/20478765/how-to-get-the-correct-orientation-of-the-image-selected-from-the-default-image/20480741#20480741
 * answered Dec 9 '13 at 21:04 by ramaral
 * <p/>
 * image resize
 * http://stackoverflow.com/questions/477572/strange-out-of-memory-issue-while-loading-an-image-to-a-bitmap-object
 * edited Jan 12 '12 at 12:33 by Fedor
 * and
 * edited Mar 20 at 6:18 by Thomas Vervest
 */
public class CapturaFragment extends Fragment implements Button.OnClickListener, View.OnTouchListener {

//    private ImageButton[] botones;
//    private ToggleButton[] toggles;
//    String[] keys;
//    String[] statusString;

    private ImageButton btnCamara;
    private ImageButton btnGaleria;
    private ImageButton btnSave;

    private ImageView selectedImage;

    private TextView lblInfo;

//    private EditText textoComentarios;

    //    public CustomAutoCompleteView autocompleteNombreComun;
    public CustomAutoCompleteView autocompleteFamilia;
    public CustomAutoCompleteView autocompleteGenero;
    public CustomAutoCompleteView autocompleteEspecie;

    private Spinner spinnerColor1;
    private Spinner spinnerColor2;
    private Spinner spinnerLugar;
    private Spinner spinnerFormaVida1;
    private Spinner spinnerFormaVida2;

    public CapturaNombreComunArrayAdapter nombreComunArrayAdapter;
    public CapturaNombreFamiliaArrayAdapter nombreFamiliaArrayAdapter;
    public CapturaNombreGeneroArrayAdapter nombreGeneroArrayAdapter;
    public CapturaNombreEspecieArrayAdapter nombreEspecieArrayAdapter;

    private static final int GALLERY_REQUEST = 999;
    private static final int CAMERA_REQUEST = 1337;

    private static int screenWidth;
    private static int screenHeight;

    private String fotoPath;
    private Double fotoLat;
    private Double fotoLong;
    private Double fotoAlt;

    boolean hayFoto = false;
    boolean deMapa = false;

    MapActivity context;
    private String pathFolder;
    private Bitmap bitmap;
    MapActivity activity;
    Foto fotoSubir;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
//        pathFolder = getArguments().getString("pathFolder");

        pathFolder = Utils.getFolder(context);

        View view = inflater.inflate(R.layout.captura_layout, container, false);
        View scrollview = view.findViewById(R.id.captura_cientifico_scroll_view);
//        view.setOnTouchListener(this);
        scrollview.setOnTouchListener(this);

        activity = (MapActivity) getActivity();
        screenHeight = activity.screenHeight;
        screenWidth = activity.screenWidth;
        selectedImage = (ImageView) view.findViewById(R.id.captura_chosen_image_view);
        lblInfo = (TextView) view.findViewById(R.id.captura_info_label);
//        textoComentarios = (EditText) view.findViewById(R.id.captura_comentarios_txt);

        initSpinners(view);
        initAutocompletes(view);
        initButtons(view);
//        initToggles(view);
        return view;
    }

    @Override
    public void onClick(View v) {

        Utils.hideSoftKeyboard(this.getActivity());

        if (v.getId() == btnGaleria.getId()) { // galeria
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, GALLERY_REQUEST);
            } else {
                alerta(getString(R.string.gallery_app_not_available));
            }
        } else if (v.getId() == btnCamara.getId()) { // camara
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            } else {
                alerta(getString(R.string.camera_app_not_available));
            }
        } else if (v.getId() == btnSave.getId()) { // save
            if (hayFoto) {
                FormaVida formaVida1 = (FormaVida) spinnerFormaVida1.getSelectedItem();
                FormaVida formaVida2 = (FormaVida) spinnerFormaVida2.getSelectedItem();
                Color color1 = (Color) spinnerColor1.getSelectedItem();
                Color color2 = (Color) spinnerColor2.getSelectedItem();
                Lugar lugar = (Lugar) spinnerLugar.getSelectedItem();
//                String nombreComun = autocompleteNombreComun.getText().toString().trim();
                String nombreFamilia = autocompleteFamilia.getText().toString().trim();
                String nombreGenero = autocompleteGenero.getText().toString().trim();
                String nombreEspecie = autocompleteEspecie.getText().toString().trim();

                boolean ok = true;
               /* if (nombreComun.equals("")) {
                    ok = false;
                    alerta(getString(R.string.captura_error_nombre_comun));
                } else*/
                if (nombreFamilia.equals("")) {
                    ok = false;
                    alerta(getString(R.string.captura_error_nombre_familia));
                } else if (nombreGenero.equals("")) {
                    ok = false;
                    alerta(getString(R.string.captura_error_nombre_genero));
                } else if (nombreEspecie.equals("")) {
                    ok = false;
                    alerta(getString(R.string.captura_error_nombre_especie));
                }

                if (ok) {
//                    String comentarios = textoComentarios.getText().toString().trim();
//                    String keywords = "";
//                    int i = 0;
//                    boolean checked = false;
//                    for (ToggleButton toggle : toggles) {
//                        if (toggle.isChecked()) {
//                            checked = true;
//                            if (!keywords.equals("")) {
//                                keywords += ", ";
//                            }
//                            keywords += keys[i];
////                        System.out.println("i=" + i + "   " + keys[i] + "     " + keywords);
//                        }
//                        i++;
//                    }
//                    if (!checked) {
//                        alerta(getString(R.string.captura_error_keywords));
//                    } else {
                    Familia familia = null;
                    Genero genero = null;
                    Especie especie = null;
                    if (!nombreFamilia.equals("")) {
                        familia = Familia.getByNombreOrCreate(context, nombreFamilia);
                    }
                    if (!nombreGenero.equals("")) {
                        genero = Genero.getByNombreOrCreate(context, nombreGenero);
                        if (familia != null) {
                            genero.setFamilia(familia);
                            genero.save();
                        }
                    }
                    if (!nombreEspecie.equals("")) {
                        especie = Especie.getByNombreOrCreate(context, nombreEspecie);
                        if (genero != null) {
                            especie.setGenero(genero);
                        }
                        if (color1 != null) {
                            especie.setColor1(color1);
                        }
                        if (color2 != null && !color2.nombre.equals("none")) {
                            especie.setColor2(color2);
                        }
                        if (formaVida1 != null) {
                            especie.setFormaVida1(formaVida1);
                        }
                        if (formaVida2 != null && !formaVida2.nombre.equals("none")) {
                            especie.setFormaVida2(formaVida2);
                        }
//                            if (!nombreComun.equals("")) {
//                                especie.setNombreComun(nombreComun);
//                            }
                        especie.save();

                    }

                    if (!deMapa) {
                        fotoSubir = new Foto(context);
                    }

                    if (especie != null) {
                        fotoSubir.setEspecie(especie);
                    }
//                        fotoSubir.setKeywords(keywords);

                    if (fotoLat != null && fotoLong != null) {
//                        System.out.println("COORDENADA::: " + fotoLat + "," + fotoLong);
                        fotoSubir.latitud = fotoLat;
                        fotoSubir.longitud = fotoLong;
                        fotoSubir.altitud = fotoAlt;
                    }
                    if (lugar != null) {
                        fotoSubir.setLugar(lugar);
                    }
                    fotoSubir.save();

                    String nuevoNombre;
                    if (genero != null && especie != null) {
                        nuevoNombre = genero.nombre + "_" + especie.nombre + "_" + fotoSubir.id;
                        nuevoNombre = nuevoNombre.replaceAll("[^a-zA-Z_0-9]", "_");
                    } else {
                        nuevoNombre = "na_na_" + fotoSubir.id;
                    }
                    nuevoNombre += ".jpg";

//                File file = new File(pathFolder, fotoPath);
//                fotoPath = file.getName();
                    File file = new File(pathFolder, nuevoNombre);
////                if (file.exists()) {
////                    file.delete();
////                }
                    try {
                        if (!file.exists()) {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                            out.flush();
                            out.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //System.out.println("Path folder: " + pathFolder);
                    //System.out.println("Photo path: " + fotoPath);
//                foto.setPath(pathFolder + "/" + fotoPath);
                    fotoSubir.setPath(pathFolder + "/" + nuevoNombre);
                    fotoSubir.save();
//                String msg = "Foto guardada";
                    //System.out.println("foto: " + foto.id + "entry: " + entry.id + "  especie: " + especie.id + "   " + especie.nombre + "  (" + genero.nombre + " " + genero.id + ")" + "  (" + familia.nombre + " " + familia.id + ")");

                    alerta(getString(R.string.captura_success));
                    //                System.out.println("Save: <" + keywords + "> <" + comentarios + ">");
//                    }
                }
            } else {
                alerta(getString(R.string.captura_error_seleccion));
            }
        } else {
            if (hayFoto) {
//                updateStatus(v);
            } else {
                ToggleButton toggle = (ToggleButton) v;
                toggle.setChecked(false);
                alerta(getString(R.string.captura_error_seleccion));
            }
        }
    }

    private void initButtons(View view) {
        btnGaleria = (ImageButton) view.findViewById(R.id.captura_gallery_btn);
        btnGaleria.setOnClickListener(this);
        btnCamara = (ImageButton) view.findViewById(R.id.captura_camera_btn);
        btnCamara.setOnClickListener(this);
        btnSave = (ImageButton) view.findViewById(R.id.captura_save_btn);
        btnSave.setOnClickListener(this);
    }

    private void initSpinners(View view) {
        ArrayList<Color> colores1 = Color.listColores(context);
        ArrayList<Color> colores2 = Color.list(context);

        ArrayList<Lugar> lugares = Lugar.list(context);

        ArrayList<FormaVida> formasVida1 = FormaVida.listFormasVida(context);
        ArrayList<FormaVida> formasVida2 = FormaVida.list(context);

        spinnerColor1 = (Spinner) view.findViewById(R.id.captura_color1_spinner);
        spinnerColor1.setAdapter(new CapturaColorSpinnerAdapter(context, colores1));

        spinnerColor2 = (Spinner) view.findViewById(R.id.captura_color2_spinner);
        spinnerColor2.setAdapter(new CapturaColorSpinnerAdapter(context, colores2));

        spinnerLugar = (Spinner) view.findViewById(R.id.captura_lugar_spinner);
        spinnerLugar.setAdapter(new CapturaLugarSpinnerAdapter(context, lugares));

        spinnerFormaVida1 = (Spinner) view.findViewById(R.id.captura_forma_vida1_spinner);
        spinnerFormaVida1.setAdapter(new CapturaFormaVidaSpinnerAdapter(context, formasVida1));

        spinnerFormaVida2 = (Spinner) view.findViewById(R.id.captura_forma_vida2_spinner);
        spinnerFormaVida2.setAdapter(new CapturaFormaVidaSpinnerAdapter(context, formasVida2));
    }

    private void initAutocompletes(View view) {

        final List<Familia> familiaList = Familia.list(context);
//        final List<Genero> generoList = Genero.list(context);
        final List<Especie> especieList = Especie.list(context);

//        autocompleteNombreComun = (CustomAutoCompleteView) view.findViewById(R.id.captura_autocomplete_nombre_comun);
//        autocompleteNombreComun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
////                System.out.println("CLICK: " + pos + "   " + especieList.get(pos).nombreComun);
//                RelativeLayout rl = (RelativeLayout) arg1;
//                TextView tv = (TextView) rl.getChildAt(0);
//                autocompleteNombreComun.setText(tv.getText().toString());
//
//                String selection = tv.getText().toString();
//                Especie selected = null;
//
//                for (Especie especie : especieList) {
//                    if (especie.nombreComun.equals(selection)) {
//                        selected = especie;
//                        break;
//                    }
//                }
//                if (selected != null) {
//                    autocompleteFamilia.setText(selected.getGenero(context).getFamilia(context).nombre);
//                    autocompleteGenero.setText(selected.getGenero(context).nombre);
//                    autocompleteEspecie.setText(selected.nombre);
//                }
//            }
//        });
//        // add the listener so it will tries to suggest while the user types
//        autocompleteNombreComun.addTextChangedListener(new CapturaNombreComunAutocompleteTextChangedListener(context, this));
        // ObjectItemData has no value at first
        // set the custom ArrayAdapter
        nombreComunArrayAdapter = new CapturaNombreComunArrayAdapter(context, R.layout.captura_autocomplete_list_item, especieList);
//        autocompleteNombreComun.setAdapter(nombreComunArrayAdapter);

        autocompleteFamilia = (CustomAutoCompleteView) view.findViewById(R.id.captura_autocomplete_nombre_familia);
        autocompleteGenero = (CustomAutoCompleteView) view.findViewById(R.id.captura_autocomplete_nombre_genero);
        autocompleteEspecie = (CustomAutoCompleteView) view.findViewById(R.id.captura_autocomplete_nombre_especie);

        final CapturaFragment thisFragment = this;

        autocompleteFamilia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
//                System.out.println("CLICK: " + pos + "   ");

                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                String txt = tv.getText().toString();
                Familia fam = null;
                for (Familia familia : familiaList) {
                    if (familia.nombre.equals(txt)) {
                        fam = familia;
                        break;
                    }
                }
                if (fam != null) {
//                    System.out.println("FAMILIA:::: " + fam.nombre);
                    final List<Genero> generos = Genero.findAllByFamilia(context, fam);
//                    for (Genero genero : generos) {
//                        System.out.println("<<<>>>>>>>>>>>>>> " + genero.nombre);
//                    }
                    autocompleteGenero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
//                System.out.println("CLICK: " + pos + "   " + especieList.get(pos).nombreComun);
                            RelativeLayout rl = (RelativeLayout) arg1;
                            TextView tv = (TextView) rl.getChildAt(0);
                            String txt = tv.getText().toString();
                            autocompleteGenero.setText(txt);
                            Genero gen = null;
                            for (Genero genero : generos) {
                                if (genero.nombre.equals(txt)) {
                                    gen = genero;
                                    break;
                                }
                            }

                            if (gen != null) {
                                final List<Especie> especies = Especie.findAllByGenero(context, gen);
                                autocompleteEspecie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
//                System.out.println("CLICK: " + pos + "   " + especieList.get(pos).nombreComun);
                                        RelativeLayout rl = (RelativeLayout) arg1;
                                        TextView tv = (TextView) rl.getChildAt(0);
                                        autocompleteEspecie.setText(tv.getText().toString());
                                    }
                                });
                                // add the listener so it will tries to suggest while the user types
                                autocompleteEspecie.addTextChangedListener(new CapturaNombreEspecieAutocompleteTextChangedListener(context, thisFragment, gen));
                                // ObjectItemData has no value at first
                                // set the custom ArrayAdapter
                                nombreEspecieArrayAdapter = new CapturaNombreEspecieArrayAdapter(context, R.layout.captura_autocomplete_list_item, especies);
                                autocompleteEspecie.setAdapter(nombreEspecieArrayAdapter);
                            }
                        }
                    });
                    // add the listener so it will tries to suggest while the user types
                    autocompleteGenero.addTextChangedListener(new CapturaNombreGeneroAutocompleteTextChangedListener(context, thisFragment, fam));
                    // ObjectItemData has no value at first
                    // set the custom ArrayAdapter
                    nombreGeneroArrayAdapter = new CapturaNombreGeneroArrayAdapter(context, R.layout.captura_autocomplete_list_item, generos);
                    autocompleteGenero.setAdapter(nombreGeneroArrayAdapter);
                }
                autocompleteFamilia.setText(txt);
            }
        });
        // add the listener so it will tries to suggest while the user types
        autocompleteFamilia.addTextChangedListener(new CapturaNombreFamiliaAutocompleteTextChangedListener(context, this));
        // ObjectItemData has no value at first
        // set the custom ArrayAdapter
        nombreFamiliaArrayAdapter = new CapturaNombreFamiliaArrayAdapter(context, R.layout.captura_autocomplete_list_item, familiaList);
        autocompleteFamilia.setAdapter(nombreFamiliaArrayAdapter);
    }

    private void resetForm() {
        selectedImage.setImageDrawable(null);
//        for (ToggleButton toggle : toggles) {
//            toggle.setChecked(false);
//        }
        spinnerColor1.setSelection(0);
        spinnerColor2.setSelection(0);

//        autocompleteNombreComun.setText("");
        autocompleteFamilia.setText("");
        autocompleteGenero.setText("");
        autocompleteEspecie.setText("");

//        textoComentarios.setText("");
        hayFoto = false;
        deMapa = false;
    }

    private void alerta(String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    /**
     * Photo Selection result
     * Read more at http://www.airpair.com/android/android-image-picker-select-gallery-images#bok7edWCrB11olZ1.99
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST || requestCode == CAMERA_REQUEST) {
                deMapa = false;
                hayFoto = true;
//                updateStatus(null);
                MapActivity activity = (MapActivity) getActivity();
//                Bitmap thumb = ImageUtils.getThumbnailFromCameraData(data, activity);
                Bitmap thumb = ImageUtils.getThumbnailFromCameraData(data, activity);
                selectedImage.setImageBitmap(thumb);
                bitmap = ImageUtils.getBitmapFromCameraData(data, activity);

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                fotoPath = cursor.getString(columnIndex);

                try {
                    ExifInterface exif = new ExifInterface(fotoPath);
                    GeoDegree gd = new GeoDegree(exif);
                    fotoLat = gd.getLatitude();
                    fotoLong = gd.getLongitude();
                    String p = exif.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
                    String[] parts = p.split("/");
                    Double alt = Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
//                    System.out.println("**************************" + alt + "     " + fotoLat + "       " + fotoLong);
                    fotoAlt = alt;
                    if (fotoLat != null && fotoLong != null) {
                        alerta(getString(R.string.captura_success_tag_gps));
                    } else {
                        alerta(getString(R.string.captura_error_tag_gps));
                    }
                } catch (Exception e) {
                    alerta(getString(R.string.captura_error_tag_gps));
                    fotoLat = null;
                    fotoLong = null;
                    fotoAlt = null;
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
//        System.out.println("ON TOUCH");
//        Utils.hideSoftKeyboard(context);
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle(R.string.captura_title);
    }
}