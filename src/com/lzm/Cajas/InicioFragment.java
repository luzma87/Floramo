package com.lzm.Cajas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.lzm.Cajas.utils.Utils;

/**
 * Created by DELL on 11/11/2014.
 */
public class InicioFragment extends Fragment implements View.OnClickListener {
    MapActivity context;

    Button btnBrowse;
    Button btnSearch;
    Button btnMap;
    Button btnAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();

        context.activeFragment = context.INICIO_POS;

        View view = inflater.inflate(R.layout.inicio_layout, container, false);

        btnBrowse = (Button) view.findViewById(R.id.btn_inicio_ver_nombres);
        btnSearch = (Button) view.findViewById(R.id.btn_inicio_buscar);
        btnMap = (Button) view.findViewById(R.id.btn_inicio_mapa);
        btnAbout = (Button) view.findViewById(R.id.btn_inicio_about);

        btnBrowse.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnAbout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnBrowse.getId()) {
            Utils.openFragment(context, new EnciclopediaGridFragment(), getString(R.string.encyclopedia_title));
            context.activeFragment = context.ENCICLOPEDIA_POS;
            context.mDrawerList.setItemChecked(context.ENCICLOPEDIA_POS, true);
        } else if (view.getId() == btnSearch.getId()) {
            Utils.openFragment(context, new BusquedaFragment(), getString(R.string.busqueda_title));
            context.activeFragment = context.BUSQUEDA_POS;
        } else if (view.getId() == btnMap.getId()) {
            context.selectItem(context.MAP_POS);
        } else if (view.getId() == btnAbout.getId()) {
            context.selectItem(context.SETTINGS_POS);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.inicio_title);
        context.mDrawerList.setItemChecked(context.INICIO_POS, true);
    }
}