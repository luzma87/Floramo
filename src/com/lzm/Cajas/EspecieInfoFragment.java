package com.lzm.Cajas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lzm.Cajas.db.*;
import com.lzm.Cajas.image.ImageUtils;
import com.lzm.Cajas.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by DELL on 15/08/2014.
 */
public class EspecieInfoFragment extends Fragment implements Button.OnClickListener, View.OnTouchListener {
    MapActivity context;

    TextView txtEspecieInfoNombreCientifico;

    TextView txtEspecieInfoFamilia;
    TextView txtEspecieInfoGenero;
    TextView txtEspecieInfoEspecie;

    ImageView imgEspecieInfoColor1;
    ImageView imgEspecieInfoColor2;

    TextView txtEspecieInfoFormaVida1;
    TextView txtEspecieInfoFormaVida2;
    ImageView imgEspecieInfoFormaVida1;
    ImageView imgEspecieInfoFormaVida2;

    TextView txtEspecieInfoAltura;

    TextView txtEspecieInfoFotos;

    ImageView imgEspecieInfoImagen;

    ImageView[] imageViews;
    int fotoPos;

    //    Button btnCajas;
    Button btnTropicos;
    Button btnDescripcion;

    Especie especie;
    List<Foto> fotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        View view = inflater.inflate(R.layout.especie_info_layout, container, false);

        txtEspecieInfoNombreCientifico = (TextView) view.findViewById(R.id.especie_info_nombre_cientifico);

        txtEspecieInfoFamilia = (TextView) view.findViewById(R.id.especie_info_familia);
        txtEspecieInfoGenero = (TextView) view.findViewById(R.id.especie_info_genero);
        txtEspecieInfoEspecie = (TextView) view.findViewById(R.id.especie_info_especie);

        imgEspecieInfoColor1 = (ImageView) view.findViewById(R.id.especie_info_color1);
        imgEspecieInfoColor2 = (ImageView) view.findViewById(R.id.especie_info_color2);

        txtEspecieInfoFormaVida1 = (TextView) view.findViewById(R.id.especie_info_forma_vida1);
        txtEspecieInfoFormaVida2 = (TextView) view.findViewById(R.id.especie_info_forma_vida2);

        imgEspecieInfoFormaVida1 = (ImageView) view.findViewById(R.id.especie_info_forma_vida1_img);
        imgEspecieInfoFormaVida2 = (ImageView) view.findViewById(R.id.especie_info_forma_vida2_img);

        txtEspecieInfoFotos = (TextView) view.findViewById(R.id.especie_info_fotos);

        imgEspecieInfoImagen = (ImageView) view.findViewById(R.id.especie_info_imagen);

//        btnCajas = (Button) view.findViewById(R.id.especie_info_web);
//        btnCajas.setOnClickListener(this);
        btnDescripcion = (Button) view.findViewById(R.id.especie_info_descripcion);
        btnDescripcion.setOnClickListener(this);
        btnTropicos = (Button) view.findViewById(R.id.especie_info_floramo);
        btnTropicos.setOnClickListener(this);
        long especieId = getArguments().getLong("especie");
        especie = Especie.getDatos(context, especieId);

        txtEspecieInfoNombreCientifico.setText(especie.genero + " " + especie.nombre.toLowerCase());
        txtEspecieInfoFamilia.setText(especie.familia);
        txtEspecieInfoGenero.setText(especie.genero);
        txtEspecieInfoEspecie.setText(especie.nombre);

//        txtEspecieInfoColor1.setText(Utils.getStringResourceByName(context, "global_color_" + especie.color1));
//        if (especie.color2 != null && !especie.color2.equals("none") && !especie.color2.equals("")) {
//            txtEspecieInfoColor2.setText(Utils.getStringResourceByName(context, "global_color_" + especie.color2));
//            txtEspecieInfoColor2.setVisibility(View.VISIBLE);
//        } else {
//            txtEspecieInfoColor2.setVisibility(View.GONE);
//        }

        imgEspecieInfoColor1.setImageResource(Utils.getImageResourceByName(context, "ic_cl_" + especie.color1 + "_tiny"));
        if (especie.color2 != null && !especie.color2.equals("none")) {
            imgEspecieInfoColor2.setImageResource(Utils.getImageResourceByName(context, "ic_cl_" + especie.color2 + "_tiny"));
            imgEspecieInfoColor2.setVisibility(View.VISIBLE);
        } else {
            imgEspecieInfoColor2.setVisibility(View.GONE);
        }

        txtEspecieInfoFormaVida1.setText(Utils.getStringResourceByName(context, "global_forma_vida_" + especie.formaVida1));
        imgEspecieInfoFormaVida1.setImageResource(Utils.getImageResourceByName(context, "ic_fv_" + especie.formaVida1));
        if (especie.formaVida2 != null && !especie.formaVida2.equals("none") && !especie.formaVida2.equals("")) {
            txtEspecieInfoFormaVida2.setText(Utils.getStringResourceByName(context, "global_forma_vida_" + especie.formaVida2));
            imgEspecieInfoFormaVida2.setImageResource(Utils.getImageResourceByName(context, "ic_fv_" + especie.formaVida2));
            imgEspecieInfoFormaVida2.setVisibility(View.VISIBLE);
            txtEspecieInfoFormaVida2.setVisibility(View.VISIBLE);
        } else {
            imgEspecieInfoFormaVida2.setVisibility(View.GONE);
            txtEspecieInfoFormaVida2.setVisibility(View.GONE);
        }

        boolean vert = false;

        fotos = Foto.findAllByEspecie(context, especie);

        imageViews = new ImageView[6];
        imageViews[0] = (ImageView) view.findViewById(R.id.especie_info_foto1);
        imageViews[1] = (ImageView) view.findViewById(R.id.especie_info_foto2);
        imageViews[2] = (ImageView) view.findViewById(R.id.especie_info_foto3);
        imageViews[3] = (ImageView) view.findViewById(R.id.especie_info_foto4);
        imageViews[4] = (ImageView) view.findViewById(R.id.especie_info_foto5);
        imageViews[5] = (ImageView) view.findViewById(R.id.especie_info_foto6);

        int cantFotos = fotos.size();
        int showing = Math.min(imageViews.length, cantFotos);
        String strMostrando = getResources().getQuantityString(R.plurals.especie_info_fotos, cantFotos, cantFotos, showing);
        txtEspecieInfoFotos.setText(strMostrando);

        if (fotos.size() > 0) {
            Foto foto = fotos.get(0);

            String path = foto.path.replaceAll("\\.jpg", "").replaceAll("-", "_").toLowerCase();
            if (foto.esMia == 1) {
                File imgFile = new File(foto.path);
                if (imgFile.exists()) {
                    Bitmap myBitmap = ImageUtils.decodeFile(imgFile.getAbsolutePath(), 200, 200);
                    int w = myBitmap.getWidth();
                    int h = myBitmap.getHeight();
                    if (h > w) {
                        vert = true;
                    }
                    imgEspecieInfoImagen.setImageBitmap(myBitmap);
                }
            } else {
                imgEspecieInfoImagen.setImageResource(Utils.getImageResourceByName(context, path));
            }
            imgEspecieInfoImagen.setOnClickListener(this);

            int i = 0;
            for (Foto foto1 : fotos) {
                ImageView curIV = imageViews[i];
                if (foto1.esMia == 1) {
                    String path1 = foto1.path;
                    File imgFile = new File(path1);
                    if (imgFile.exists()) {
                        if (i < imageViews.length) {
                            Bitmap myBitmap = ImageUtils.getThumbnail(imgFile.getAbsolutePath(), false);
                            curIV.setImageBitmap(myBitmap);
                        }
                    }
                } else {
                    String path1 = foto1.path.replaceAll("\\.jpg", "").replaceAll("-", "_").toLowerCase();
                    path1 = "th_" + path1;
                    curIV.setImageResource(Utils.getImageResourceByName(context, path1));
                }
                curIV.setVisibility(View.VISIBLE);
                curIV.setOnClickListener(this);
                i++;
            }
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        Utils.hideSoftKeyboard(this.getActivity());
        boolean band = false;

        Settings sett = Settings.getSettings(context);
//        if (view.getId() == btnCajas.getId()) {
//            String url = sett.floraBase + especie.genero + "+" + especie.nombre;
////            System.out.println("::::::::::::::::::::::::::::: URL FLORA" + url);
//            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(myIntent);
//            band = true;
//        }
        if (view.getId() == btnDescripcion.getId()) {
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(R.layout.especie_info_entry_dialog, null);

            ImageView img = (ImageView) v.findViewById(R.id.especie_info_dialog_image);
            TextView txt = (TextView) v.findViewById(R.id.especie_info_dialog_comentarios);

            Foto foto = fotos.get(0);
            String path1 = foto.path.replaceAll("\\.jpg", "").replaceAll("-", "_").toLowerCase();
            if (foto.esMia == 1) {
                img.setImageBitmap(context.getFotoDialog(foto, context.screenWidth, 300));
            } else {
                img.setImageResource(Utils.getImageResourceByName(context, path1));
            }

            txt.setText(especie.descripcionEs);

            final AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(v)
                    .setNeutralButton(R.string.dialog_btn_cerrar, null) //Set to null. We override the onclick
                    .setTitle(especie.genero + " " + especie.nombre);

            final AlertDialog d = builder.create();

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
                }
            });
            d.show();

            band = true;
        }
        if (view.getId() == btnTropicos.getId()) {
            String url = sett.tropicosBase + especie.idTropicos;
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
            band = true;
        }
        if (!band) {
            int i;
            if (view.getId() == imgEspecieInfoImagen.getId()) {
                i = 0;
            } else {
                for (i = 0; i < imageViews.length; i++) {
                    if (view.getId() == imageViews[i].getId()) {
                        break;
                    }
                }
            }
            fotoPos = i;

            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(R.layout.especie_info_entry_dialog, null);
            final String t = getResources().getQuantityString(R.plurals.encyclopedia_entries_dialog_title, fotos.size());
            final AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(v)
                    .setNeutralButton(R.string.dialog_btn_cerrar, null) //Set to null. We override the onclick
                    .setTitle(t + " (" + (fotoPos + 1) + "/" + fotos.size() + ")");
            if (fotos.size() > 1) {
                builder.setPositiveButton(R.string.dialog_btn_siguiente, null)
                        .setNegativeButton(R.string.dialog_btn_anterior, null);
            }
            final AlertDialog d = builder.create();
            final ImageView img = (ImageView) v.findViewById(R.id.especie_info_dialog_image);
            final EditText txt = (EditText) v.findViewById(R.id.especie_info_dialog_comentarios);
            setFoto(img, txt);
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
                            setFoto(img, txt);
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
                            setFoto(img, txt);
                            d.setTitle(t + " (" + (fotoPos + 1) + "/" + fotos.size() + ")");
                        }
                    });
                }
            });
            d.show();
        }
    }

    private void setFoto(ImageView img, EditText txt) {
        if (fotos != null) {
            String comentarios = "";
            if (comentarios != null) {
                comentarios = comentarios.trim();
            }

            Foto foto = fotos.get(fotoPos);
            String path1 = foto.path.replaceAll("\\.jpg", "").replaceAll("-", "_").toLowerCase();
            if (foto.esMia == 1) {
                img.setImageBitmap(context.getFotoDialog(foto, context.screenWidth, 300));
            } else {
                img.setImageResource(Utils.getImageResourceByName(context, path1));
            }
            if (comentarios == null || comentarios.equals("")) {
                txt.setVisibility(View.GONE);
            } else {
                txt.setText(comentarios);
                txt.setSelection(txt.getText().length());
                txt.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Utils.hideSoftKeyboard(this.getActivity());
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.especie_info_title);
    }
}