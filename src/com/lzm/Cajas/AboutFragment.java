package com.lzm.Cajas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 16/11/2014.
 */
public class AboutFragment extends Fragment {

    MapActivity context;

    public static final int CAJAS = 0;
    public static final int PARAMO = 1;
    public static final int APP = 2;

    int tipo;

    ImageView imagen;
    TextView texto;

    List<String> roseta, almohadillas, hojas, pelos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        tipo = getArguments().getInt("tipo");

        context.activeFragment = context.ABOUT_CAJAS_PARAMO_POS;
        context.paramFrag = "" + tipo;

        View view = inflater.inflate(R.layout.about_layout, container, false);

        imagen = (ImageView) view.findViewById(R.id.about_img);
        texto = (TextView) view.findViewById(R.id.about_text);

        roseta = new ArrayList<String>();
        roseta.add("rosettes");
        roseta.add("rosetas");
        almohadillas = new ArrayList<String>();
        almohadillas.add("cushion-plants");
        almohadillas.add("almohadillas");
        hojas = new ArrayList<String>();
        hojas.add("hard leaves");
        hojas.add("hojas reducidas");
        pelos = new ArrayList<String>();
        pelos.add("silvery hairs");
        pelos.add("pelos blancos");

        switch (tipo) {
            case CAJAS:
                imagen.setImageResource(R.drawable.about_cajas);
                setTextWithLinks(getString(R.string.about_cajas));
                break;
            case PARAMO:
                imagen.setImageResource(R.drawable.about_paramo);
                setTextWithLinks(getString(R.string.about_paramo));
                break;
            case APP:
                imagen.setImageResource(R.drawable.ic_floramo);
                setTextWithLinks(getString(R.string.about));
                break;
        }

        return view;
    }

    private void setTextWithLinks(String str) {
        //This is my string;
//        String str = getString(R.string.about_paramo);

        String[] parts = str.split("<a href='#'>");

        // la primera parte es lo que hay antes del primer link
        texto.setText(Html.fromHtml(parts[0]));
        texto.setMovementMethod(LinkMovementMethod.getInstance());

        //cada una de las siguientes partes contiene un link
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];

            //la parte en pos 0 es el texto del link, en pos 1 es el texto entre links
            String[] textParts = part.split("</a>");

            SpannableString ss = new SpannableString(textParts[0]);
            ClickableSpan cs = new MyClickableSpan();
            ss.setSpan(cs, 0, textParts[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            texto.append(ss);
            if (textParts.length == 2) {
                texto.append(" ");
                texto.append(Html.fromHtml(textParts[1]));
            }
        }
    }

    class MyClickableSpan extends ClickableSpan { //clickable span
        public void onClick(final View textView) {
            // TODO add check if widget instanceof TextView
            TextView tv = (TextView) textView;
            // TODO add check if tv.getText() instanceof Spanned
            Spanned s = (Spanned) tv.getText();
            int start = s.getSpanStart(this);
            int end = s.getSpanEnd(this);
            String text = s.subSequence(start, end).toString();
            int res = -1;

            if (roseta.contains(text)) {
                res = R.drawable.about_def_roseta;
            } else if (almohadillas.contains(text)) {
                res = R.drawable.about_def_almohadilla;
            } else if (hojas.contains(text)) {
                res = R.drawable.about_def_hojas;
            } else if (pelos.contains(text)) {
                res = R.drawable.about_def_pelos;
            }

            if (res == -1) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(text));
                startActivity(myIntent);
            } else {
                LayoutInflater inflater = context.getLayoutInflater();
                View v = inflater.inflate(R.layout.settings_about_definition_dlg, null);

                ImageView imgAbout = (ImageView) v.findViewById(R.id.about_definition_img);
                imgAbout.setImageResource(res);

                final AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(v)
                        .setNeutralButton(R.string.dialog_btn_cerrar, null) //Set to null. We override the onclick
                        .setTitle(text);

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
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.settings_title);
        context.mDrawerList.setItemChecked(context.SETTINGS_POS, true);
    }

}