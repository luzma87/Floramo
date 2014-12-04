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
import com.lzm.Cajas.utils.ClickableLinkSpan;
import com.lzm.Cajas.utils.Utils;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        tipo = getArguments().getInt("tipo");

        context.activeFragment = context.ABOUT_CAJAS_PARAMO_POS;
        context.paramFrag = "" + tipo;

        View view = inflater.inflate(R.layout.about_layout, container, false);

        imagen = (ImageView) view.findViewById(R.id.about_img);
        texto = (TextView) view.findViewById(R.id.about_text);

        switch (tipo) {
            case CAJAS:
                imagen.setImageResource(R.drawable.about_cajas);
                Utils.setTextWithLinks(context, texto, getString(R.string.about_cajas));
                break;
            case PARAMO:
                imagen.setImageResource(R.drawable.about_paramo);
                Utils.setTextWithLinks(context, texto, getString(R.string.about_paramo));
                break;
            case APP:
                imagen.setImageResource(R.drawable.ic_floramo);
                Utils.setTextWithLinks(context, texto, getString(R.string.about));
                break;
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.settings_title);
        context.mDrawerList.setItemChecked(context.SETTINGS_POS, true);
    }

}