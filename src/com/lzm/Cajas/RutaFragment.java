package com.lzm.Cajas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lzm.Cajas.db.Coordenada;
import com.lzm.Cajas.db.Foto;
import com.lzm.Cajas.image.ImageUtils;
import com.lzm.Cajas.listeners.FieldListener;
import com.lzm.Cajas.utils.Utils;
import com.lzm.Cajas.utils.UtilsDistancia;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Svt on 8/15/2014.
 */
public class RutaFragment extends Fragment implements Button.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    MapActivity activity;
    private Button[] botones;
    private ImageButton[] imgBotones;
    List<Foto> fotos;
    List<Coordenada> cords;
    List<ImageView> imgs;
    View view;

    ImageView imagen;

    int fotoPos = 0;

    private Button subir;
    private Button shareButton;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;
    private static final int REAUTH_ACTIVITY_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = (MapActivity) getActivity();
        view = inflater.inflate(R.layout.ruta_fragment, container, false);
        view.setOnTouchListener(this);
        int[] resSize = Utils.getSize(activity.screenWidth);
        imagen = (ImageView) view.findViewById(R.id.ruta_img);
        imagen.getLayoutParams().width = resSize[4];
        imagen.getLayoutParams().height = resSize[5];
        TextView texto = (TextView) view.findViewById(R.id.txt_descripcion);
        texto.setText(activity.ruta.descripcion);
        ((TextView) view.findViewById(R.id.ruta_fecha)).setText(activity.ruta.fecha);
        imgs = new ArrayList<ImageView>();
        fotos = Foto.findAllByRuta(activity, activity.ruta);
        cords = Coordenada.findAllByRuta(activity, activity.ruta);
        double distancia = 0;
        double alturaMinima = 0;
        double alturaMaxima = 0;
        if (cords.size() > 2) {
            for (int i = 0; i < cords.size() - 1; i++) {
                Coordenada current = cords.get(i);
                Coordenada next = cords.get(i + 1);
                if (i == 0) {
                    alturaMinima = current.altitud;
                }

                if (alturaMinima > current.altitud)
                    alturaMinima = current.altitud;
                if (alturaMaxima < current.altitud)
                    alturaMaxima = current.altitud;
                if (alturaMinima > next.altitud)
                    alturaMinima = next.altitud;
                if (alturaMaxima < next.altitud)
                    alturaMaxima = next.altitud;
                distancia += UtilsDistancia.dist(current.getLatitud(), current.getLongitud(), next.getLatitud(), next.getLongitud());
            }
        }
        distancia = distancia * 1000; /*para sacar en metros*/
        distancia = Math.round(distancia * 100.0) / 100.0;
        Foto foto;
        int width = 400;
        if (fotos.size() > 0) {
            foto = fotos.get(0);
            File imgFile = new File(foto.path);
            if (imgFile.exists()) {
                Bitmap myBitmap = ImageUtils.decodeBitmap(foto.path, width, (int) Math.floor(width * 0.5625));
                imagen.setImageBitmap(myBitmap);
                imagen.setOnClickListener(this);
                imagen.setOnLongClickListener(this);
            }
            for (int i = 1; i < fotos.size(); i++) {
                if (i > 5)
                    break;
                int res = R.id.image_1;
                switch (i) {
                    case 1:
                        res = R.id.image_1;
                        break;
                    case 2:
                        res = R.id.image_2;
                        break;
                    case 3:
                        res = R.id.image_3;
                        break;
                    case 4:
                        res = R.id.image_4;
                        break;
                    case 5:
                        res = R.id.image_5;
                        break;

                    default:
                        break;
                }
//                File file = new File(fotos.get(i).path);
                ImageView im = (ImageView) view.findViewById(res);
                im.setOnClickListener(this);
                im.setOnLongClickListener(this);
                imgs.add(im);
                Bitmap myBitmap = ImageUtils.decodeBitmap(fotos.get(i).path, 200, (int) Math.floor(200 * 0.5625));
                im.setImageBitmap(myBitmap);
                im.setVisibility(View.VISIBLE);
            }
        } else {
            imagen.setMinimumWidth(width);
            imagen.setMinimumHeight((int) Math.floor(width * 0.5625));
            imagen.setImageResource(R.drawable.ic_launcher);
        }

        ((TextView) view.findViewById(R.id.lbl_fotos)).setText("" + fotos.size() + " " + getString(R.string.ruta_lbl_foto));
        ((TextView) view.findViewById(R.id.lbl_valor_distancia)).setText("" + distancia + "m");
        ((TextView) view.findViewById(R.id.lbl_valor_altura_1)).setText("Min: " + String.format("%.2f", alturaMinima) + " m  -  Max: " + String.format("%.2f", alturaMaxima) + " m");
        botones = new Button[1];
        imgBotones = new ImageButton[1];
        imgBotones[0] = (ImageButton) view.findViewById(R.id.btn_guardar_desc);
        botones[0] = (Button) view.findViewById(R.id.ver_mapa);
        for (int i = 0; i < botones.length; i++) {
            botones[i].setOnClickListener(this);
        }
        for (int i = 0; i < imgBotones.length; i++) {
            imgBotones[i].setOnClickListener(this);
        }
        if (savedInstanceState != null) {
            pendingPublishReauthorization =
                    savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
        }

        return view;
    }

    private void showDlgDelete() {

        //System.out.println("---------------------------------------------------- " + fotoPos);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.ruta_dlg_delete_foto_contenido)
                .setTitle(R.string.ruta_dlg_delete_foto_title);

        // Add the buttons
        builder.setPositiveButton(R.string.global_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                int res = R.id.ruta_img;
                switch (fotoPos) {
                    case 1:
                        res = R.id.image_1;
                        break;
                    case 2:
                        res = R.id.image_2;
                        break;
                    case 3:
                        res = R.id.image_3;
                        break;
                    case 4:
                        res = R.id.image_4;
                        break;
                    case 5:
                        res = R.id.image_5;
                        break;
                    default:
                        break;
                }

                if (fotoPos > 0) {
                    ImageView im = (ImageView) view.findViewById(res);
                    im.setVisibility(View.GONE);
                } else {
                    if (fotos.size() > 1) {
                        ImageView im = (ImageView) view.findViewById(R.id.image_1);
                        im.setVisibility(View.GONE);
                        Foto f = fotos.get(1);
                        int width = 400;
                        ImageView im1 = (ImageView) view.findViewById(R.id.ruta_img);
                        im1.setMinimumWidth(width);
                        im1.setMinimumHeight((int) Math.floor(width * 0.5625));
                        Bitmap myBitmap = ImageUtils.decodeBitmap(f.path, width, (int) Math.floor(width * 0.5625));
                        im1.setImageBitmap(myBitmap);
                    } else {
                        int width = 400;
                        ImageView im = (ImageView) view.findViewById(res);
                        im.setMinimumWidth(width);
                        im.setMinimumHeight((int) Math.floor(width * 0.5625));
                        im.setImageResource(R.drawable.ic_launcher);
                    }
                }
                //System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
                Foto foto = fotos.get(fotoPos);
                //System.out.println("2222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
                foto.delete();
                //System.out.println("33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
                fotos.remove(fotoPos);
                //System.out.println("4444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444");

                Toast.makeText(getActivity(), getString(R.string.ruta_foto_deleted), Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.global_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onLongClick(View v) {
//        //System.out.println("Long click     " + v.getId());
        Vibrator v1 = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v1.vibrate(100);
        if (v.getId() == imagen.getId()) {
            fotoPos = 0;
            showDlgDelete();
        }

        if (imgs.size() > 0) {
            if (v.getId() == imgs.get(0).getId()) {
                fotoPos = 1;
                showDlgDelete();
            }
        }
        if (imgs.size() > 1) {
            if (v.getId() == imgs.get(1).getId()) {
                fotoPos = 2;
                showDlgDelete();
            }
        }
        if (imgs.size() > 2) {
            if (v.getId() == imgs.get(2).getId()) {
                fotoPos = 3;
                showDlgDelete();
            }
        }
        if (imgs.size() > 3) {
            if (v.getId() == imgs.get(3).getId()) {
                fotoPos = 4;
                showDlgDelete();
            }
        }
        if (imgs.size() > 4) {
            if (v.getId() == imgs.get(4).getId()) {
                fotoPos = 5;
                showDlgDelete();
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Utils.hideSoftKeyboard(this.getActivity());
        if (v.getId() == imgBotones[0].getId()) {

            String descripcion = ((EditText) view.findViewById(R.id.txt_descripcion)).getText().toString().trim();
            if (descripcion.length() > 0) {
                activity.ruta.descripcion = descripcion;
                activity.ruta.save();
                Toast.makeText(activity, getString(R.string.ruta_datos_guardados), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, getString(R.string.ruta_error_datos), Toast.LENGTH_SHORT).show();
            }
        }
        if (v.getId() == botones[0].getId()) {
            /*Ver en el mapa*/
            activity.showRuta(cords, fotos, this);
        }

        if (v.getId() == imagen.getId()) {
            fotoPos = 0;
            showDlg();
        }

        if (imgs.size() > 0) {
            if (v.getId() == imgs.get(0).getId()) {
                fotoPos = 1;
                showDlg();
            }
        }
        if (imgs.size() > 1) {
            if (v.getId() == imgs.get(1).getId()) {
                fotoPos = 2;
                showDlg();
            }
        }
        if (imgs.size() > 2) {
            if (v.getId() == imgs.get(2).getId()) {
                fotoPos = 3;
                showDlg();
            }
        }
        if (imgs.size() > 3) {
            if (v.getId() == imgs.get(3).getId()) {
                fotoPos = 4;
                showDlg();
            }
        }
        if (imgs.size() > 4) {
            if (v.getId() == imgs.get(4).getId()) {
                fotoPos = 5;
                showDlg();
            }
        }
    }

    private void setFotoDlg(View myView) {
        ImageView img = (ImageView) myView.findViewById(R.id.image);
        img.setImageBitmap(activity.getFotoDialog(fotos.get(fotoPos), activity.screenWidth, 300));
    }

    private void showDlg() {
        LayoutInflater inflater = activity.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.dialog, null);
        final String t = getResources().getQuantityString(R.plurals.encyclopedia_entries_dialog_title, fotos.size());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(t + " (" + (fotoPos + 1) + "/" + fotos.size() + ")");
        builder.setNeutralButton(R.string.dialog_btn_cerrar, null);
        if (fotos.size() > 1) {
            builder.setPositiveButton(R.string.dialog_btn_siguiente, null)
                    .setNegativeButton(R.string.dialog_btn_anterior, null);
        }
        builder.setView(myView);

        final AlertDialog d = builder.create();
        setFotoDlg(myView);
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button cerrar = d.getButton(AlertDialog.BUTTON_NEUTRAL);
                cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                Button anterior = d.getButton(AlertDialog.BUTTON_NEGATIVE);
                anterior.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fotoPos > 0) {
                            fotoPos -= 1;
                        } else {
                            fotoPos = fotos.size() - 1;
                        }
                        setFotoDlg(myView);
                        d.setTitle(t + " (" + (fotoPos + 1) + "/" + fotos.size() + ")");
                    }
                });
                Button siguiente = d.getButton(AlertDialog.BUTTON_POSITIVE);
                siguiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fotoPos < fotos.size() - 1) {
                            fotoPos += 1;
                        } else {
                            fotoPos = 0;
                        }
                        setFotoDlg(myView);
                        d.setTitle(t + " (" + (fotoPos + 1) + "/" + fotos.size() + ")");
                    }
                });
            }
        });
        d.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Utils.hideSoftKeyboard(this.getActivity());
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle(R.string.rutas_title);
        activity.mDrawerList.setItemChecked(activity.RUTAS_POS, true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        imgs = null;
        fotos = null;
        cords = null;
        super.onDestroy();
    }
}
