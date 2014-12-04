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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lzm.Cajas.utils.ClickableLinkSpan;
import com.lzm.Cajas.utils.Utils;

import java.util.List;

/**
 * Created by DELL on 16/11/2014.
 */
public class CreditsFragment extends Fragment {

    MapActivity context;

    TextView texto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();

        context.activeFragment = context.ABOUT_CAJAS_PARAMO_POS;

        View view = inflater.inflate(R.layout.credits_layout, container, false);

        texto = (TextView) view.findViewById(R.id.credits_text);
        Utils.setTextWithLinks(context, texto, getString(R.string.creditos));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.creditos_title);
        context.mDrawerList.setItemChecked(context.SETTINGS_POS, true);
    }

}