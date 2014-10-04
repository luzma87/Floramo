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

    TextView txtEspecieInfoNombreComun;
    TextView txtEspecieInfoNombreCientifico;

    TextView txtEspecieInfoFamilia;
    TextView txtEspecieInfoGenero;
    TextView txtEspecieInfoEspecie;

    TextView txtEspecieInfoColor1;
    TextView txtEspecieInfoColor2;

    TextView txtEspecieInfoFormaVida1;
    TextView txtEspecieInfoFormaVida2;

    TextView txtEspecieInfoAltura;

    TextView txtEspecieInfoFotos;

    ImageView imgEspecieInfoImagen;

    ImageView[] imageViews;
    int fotoPos;

    Button btnCajas;
    Button btnTropicos;

    Especie especie;
    List<Foto> fotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        View view = inflater.inflate(R.layout.especie_info_layout, container, false);

        txtEspecieInfoNombreComun = (TextView) view.findViewById(R.id.especie_info_nombre_comun);
        txtEspecieInfoNombreCientifico = (TextView) view.findViewById(R.id.especie_info_nombre_cientifico);

        txtEspecieInfoFamilia = (TextView) view.findViewById(R.id.especie_info_familia);
        txtEspecieInfoGenero = (TextView) view.findViewById(R.id.especie_info_genero);
        txtEspecieInfoEspecie = (TextView) view.findViewById(R.id.especie_info_especie);

        txtEspecieInfoColor1 = (TextView) view.findViewById(R.id.especie_info_color1);
        txtEspecieInfoColor2 = (TextView) view.findViewById(R.id.especie_info_color2);

        txtEspecieInfoFormaVida1 = (TextView) view.findViewById(R.id.especie_info_forma_vida1);
        txtEspecieInfoFormaVida2 = (TextView) view.findViewById(R.id.especie_info_forma_vida2);

//        txtEspecieInfoAltura = (TextView) view.findViewById(R.id.especie_info_altura);

        txtEspecieInfoFotos = (TextView) view.findViewById(R.id.especie_info_fotos);

        imgEspecieInfoImagen = (ImageView) view.findViewById(R.id.especie_info_imagen);

        btnCajas = (Button) view.findViewById(R.id.especie_info_web);
        btnCajas.setOnClickListener(this);
        btnTropicos = (Button) view.findViewById(R.id.especie_info_ikiam);
        btnTropicos.setOnClickListener(this);
        long especieId = getArguments().getLong("especie");
        especie = Especie.getDatos(context, especieId);
//        Genero genero = especie.getGenero(context);
//        Familia familia = genero.getFamilia(context);

        String color1 = "", color2 = "";
        String formaVida1 = "", formaVida2 = "";
//        Color c1 = especie.getColor1(context);
//        if (c1 != null) {
//            int id = getResources().getIdentifier("global_color_" + c1.nombre, "string", context.getPackageName());
//            color1 = id == 0 ? "" : (String) getResources().getText(id);
        color1 = Utils.getStringResourceByName(context, "global_color_" + especie.color1);
//        }
//        Color c2 = especie.getColor2(context);
        if (/*c2 != null && */especie.color2 != null && !especie.color2.equals("none") && !especie.color2.equals("")) {
//            int id = getResources().getIdentifier("global_color_" + c2.nombre, "string", context.getPackageName());
//            color2 = id == 0 ? "" : (", " + ((String) getResources().getText(id)));
            color2 = ", " + Utils.getStringResourceByName(context, "global_color_" + especie.color2);
        }
//        FormaVida f1 = especie.getFormaVida1(context);
//        if (f1 != null) {
        formaVida1 = Utils.getStringResourceByName(context, "global_forma_vida_" + especie.formaVida1);
//        }
//        FormaVida f2 = especie.getFormaVida2(context);
        if (/*f2 != null && */especie.formaVida2 != null && !especie.formaVida2.equals("none") && !especie.formaVida2.equals("")) {
            formaVida2 = ", " + Utils.getStringResourceByName(context, "global_forma_vida_" + especie.formaVida2);
        }

        double altMin = 0, altMax = 0;
        boolean vert = false;

        fotos = Foto.findAllByEspecie(context, especie);
        //especie_info_foto5
        //(ImageView) view.findViewById(R.id.especie_info_imagen);
        //im.setVisibility(View.VISIBLE);

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
//        if (cantFotos == 1) {
//            strMostrando = getString(R.string.especie_info_foto, cantFotos, showing);
//        } else {
//            strMostrando = getString(R.string.especie_info_fotos, cantFotos, showing);
//        }
        txtEspecieInfoFotos.setText(strMostrando);

        if (fotos.size() > 0) {
            Foto foto = fotos.get(0);
            altMin = foto.altitud;
            altMax = foto.altitud;

            String path = foto.path.replaceAll("\\.jpg", "").replaceAll("-", "_").toLowerCase();
//            System.out.println("PATH:::: " + path);
            if (foto.esMia == 1) {
                File imgFile = new File(foto.path);
                if (imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                    Bitmap myBitmap = ImageUtils.decodeBitmap(imgFile.getAbsolutePath(), 200, 200);
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

            int screenWidth = context.screenWidth - 40;
            int currentWidth = 0;
            int idPrev = 0;
            int highest = 0;
            int idHighest = 0;

            int i = 0;
            for (Foto foto1 : fotos) {
                ImageView curIV = imageViews[i];
                if (foto1.esMia == 1) {
                    String path1 = foto1.path;
                    File imgFile = new File(path1);
                    if (imgFile.exists()) {
                        if (i < imageViews.length) {
//                            Bitmap myBitmap = ImageUtils.decodeFile(imgFile.getAbsolutePath(), 100, 100, true);
                            Bitmap myBitmap = ImageUtils.getThumbnail(imgFile.getAbsolutePath(), false);
//                            int w = myBitmap.getWidth();
//                            int h = myBitmap.getHeight();
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


//            for (Foto f : fotos) {
//                imgFile = new File(f.path);
//                if (imgFile.exists()) {
//                    if (i < imageViews.length) {
//                        ImageView curIV = imageViews[i];
//                        if (currentWidth == 0) {
//                            idPrev = 0;
//                        }
//                        Bitmap myBitmap = ImageUtils.decodeFile(imgFile.getAbsolutePath(), 100, 100, true);
//                        int w = myBitmap.getWidth();
//                        int h = myBitmap.getHeight();
////                                if (h > w) {
////                                    System.out.println("foto es VERT");
////                                }
//                        curIV.setImageBitmap(myBitmap);
//                        curIV.setVisibility(View.VISIBLE);
//                        curIV.setOnClickListener(this);
//                               /* currentWidth += (w + 30);
//                                RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) curIV.getLayoutParams();
//                                if (idPrev > 0) {
//                                    p.addRule(RelativeLayout.RIGHT_OF, idPrev);
////                                    p.setMargins(0, highest + 15, 0, 0);
//                                }
//                                if (currentWidth > screenWidth) {
//                                    p.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//                                    p.addRule(RelativeLayout.BELOW, idHighest);
//                                    p.setMargins(0, highest + 25, 0, 0);
//                                    currentWidth = 0;
//                                }
//                                curIV.setLayoutParams(p);
//                                idPrev = curIV.getId();
//                                if (h > highest) {
//                                    highest = h;
//                                    idHighest = curIV.getId();
//                                }
//                                 */
//                    }
//
//                    if (foto.altitud < altMin) {
//                        altMin = foto.altitud;
//                    }
//                    if (foto.altitud > altMax) {
//                        altMax = foto.altitud;
//                    }
//                    i++;
//                }
//            }
        }

        txtEspecieInfoNombreComun.setText(especie.nombreComun);
        txtEspecieInfoNombreCientifico.setText(especie.genero + " " + especie.nombre.toLowerCase());

        txtEspecieInfoFamilia.setText(especie.familia);
        txtEspecieInfoGenero.setText(especie.genero);
        txtEspecieInfoEspecie.setText(especie.nombre);

        txtEspecieInfoColor1.setText(color1);
        txtEspecieInfoColor2.setText(color2);

        txtEspecieInfoFormaVida1.setText(formaVida1);
        txtEspecieInfoFormaVida2.setText(formaVida2);

        if (vert) {
            //la foto es vertical
            TextView t = (TextView) view.findViewById(R.id.especie_info_color_lbl);
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) t.getLayoutParams();
            p.addRule(RelativeLayout.ALIGN_START, R.id.especie_info_especie_lbl);
            t.setLayoutParams(p);
        } else {
            //la foto es horizontal
            TextView t = (TextView) view.findViewById(R.id.especie_info_color_lbl);
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) t.getLayoutParams();
            p.setMargins(0, 10, 0, 0);
            t.setLayoutParams(p);

//            t = (TextView) view.findViewById(R.id.especie_info_altura_lbl);
            p = (RelativeLayout.LayoutParams) t.getLayoutParams();
            p.setMargins(0, 10, 0, 0);
            p.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
            t.setLayoutParams(p);

            p = (RelativeLayout.LayoutParams) imgEspecieInfoImagen.getLayoutParams();
            p.setMargins(0, 10, 0, 0);
            imgEspecieInfoImagen.setLayoutParams(p);
        }

//        txtEspecieInfoAltura.setText(getString(R.string.global_min) + ": " + altMin + " m. "
//                + getString(R.string.global_max) + ": " + altMax + "m.");

        return view;
    }

    @Override
    public void onClick(View view) {
        Utils.hideSoftKeyboard(this.getActivity());
        boolean band = false;

        Settings sett = Settings.getSettings(context);
        if (view.getId() == btnCajas.getId()) {
            String url = sett.floraBase + especie.getGenero(context).nombre + "+" + especie.nombre;
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
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
//        System.out.println("SET FOTO " + fotoPos);
        if (fotos != null) {
            String comentarios = "";
//            while (comentarios.length() < 15000) {
//                comentarios += " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce ultricies luctus imperdiet. Pellentesque libero erat, laoreet ac magna sit amet, blandit vulputate nisl. Nam dignissim non velit eget cursus. Aenean dui metus, vehicula a leo quis, tincidunt gravida est. Fusce semper nec purus quis consectetur. Vestibulum risus felis, accumsan vitae nulla eu, fringilla vulputate lectus. Nam scelerisque magna vel sollicitudin molestie. Nulla venenatis ipsum sem, nec dignissim lacus vestibulum eget. ";
//            }
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
//            img.setImageBitmap(context.getFotoDialog(foto, context.screenWidth, 300));
//            img.setImageBitmap(ImageUtils.decodeFile(foto.path, context.screenWidth, 300));
            if (comentarios == null || comentarios.equals("")) {
                txt.setVisibility(View.GONE);
            } else {
                txt.setText(comentarios);
                txt.setSelection(txt.getText().length());
                txt.setVisibility(View.VISIBLE);
            }
//        dialogTitle = R.string.encyclopedia_entries_dialog_title + " (" + (fotoPos + 1) + "/" + fotos.size() + ")";
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