package com.lzm.Cajas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lzm.Cajas.db.Foto;
import com.lzm.Cajas.listeners.FieldListener;
import com.lzm.Cajas.utils.Utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DELL on 23/07/2014.
 */
public class SettingsFragment extends Fragment implements Button.OnClickListener {

    MapActivity context;

    Button btnLugares;
    Button btnFormasVida;
    Button btnBugs;
    Button btnComments;
    Button btnAbout;
    Button btnCreditos;

    /*
    <div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
    <div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
    <div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
    <div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
    <div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
    <div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
    <div>
    Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
     */
    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        View view = inflater.inflate(R.layout.settings_layout, container, false);

        btnLugares = (Button) view.findViewById(R.id.settings_btn_lugares);
        btnLugares.setOnClickListener(this);

        btnFormasVida = (Button) view.findViewById(R.id.settings_btn_formas_vida);
        btnFormasVida.setOnClickListener(this);

        btnBugs = (Button) view.findViewById(R.id.settings_btn_bugs);
        btnBugs.setOnClickListener(this);

        btnComments = (Button) view.findViewById(R.id.settings_btn_comments);
        btnComments.setOnClickListener(this);

        btnAbout = (Button) view.findViewById(R.id.settings_btn_about);
        btnAbout.setOnClickListener(this);

        btnCreditos = (Button) view.findViewById(R.id.settings_btn_creditos);
        btnCreditos.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Utils.hideSoftKeyboard(this.getActivity());
        if (view.getId() == btnLugares.getId()) {
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(R.layout.settings_lugar_dialog, null);

            final TextView txt = (TextView) v.findViewById(R.id.lugar_link_dialog_txt);
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = getString(R.string.cajas_link);
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(myIntent);
                }
            });

            final AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(v)
                    .setNeutralButton(R.string.dialog_btn_cerrar, null) //Set to null. We override the onclick
                    .setTitle(getString(R.string.lugar_title));

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
        } else if (view.getId() == btnFormasVida.getId()) {
//            SettingsBugsFragment fragment = new SettingsBugsFragment();
//            Utils.openFragment(context, fragment, getString(R.string.bugs_title));
        } else if (view.getId() == btnBugs.getId()) {
            SettingsBugsFragment fragment = new SettingsBugsFragment();
            Utils.openFragment(context, fragment, getString(R.string.bugs_title));
        } else if (view.getId() == btnComments.getId()) {
            SettingsCommentsFragment fragment = new SettingsCommentsFragment();
            Utils.openFragment(context, fragment, getString(R.string.bugs_title));
        } else if (view.getId() == btnAbout.getId()) {
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(R.layout.settings_about_dialog, null);

            final AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(v)
                    .setNeutralButton(R.string.dialog_btn_cerrar, null) //Set to null. We override the onclick
                    .setTitle(getString(R.string.about_title));

            final AlertDialog d = builder.create();
            final TextView txt = (TextView) v.findViewById(R.id.about_dialog_txt);
            txt.setText(getString(R.string.about));

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
        } else if (view.getId() == btnCreditos.getId()) {
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(R.layout.settings_creditos_dialog, null);

            final AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(v)
                    .setNeutralButton(R.string.dialog_btn_cerrar, null) //Set to null. We override the onclick
                    .setTitle(getString(R.string.creditos_title));

            final AlertDialog d = builder.create();
            final TextView txt = (TextView) v.findViewById(R.id.creditos_dialog_txt);
            txt.setText(getString(R.string.creditos));

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

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.settings_title);
    }
}