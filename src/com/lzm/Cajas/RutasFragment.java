package com.lzm.Cajas;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.lzm.Cajas.adapters.RutasListAdapter;
import com.lzm.Cajas.db.Ruta;


import java.util.List;

/**
 * Created by Svt on 8/15/2014.
 */
public class RutasFragment extends ListFragment {
    MapActivity activity;
    List<Ruta> list;
    String dialogTitle = "";


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MapActivity) getActivity();
        list = Ruta.list(activity);
        RutasListAdapter adapter = new RutasListAdapter(getActivity(), list);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Ruta selected = list.get(position);
        activity.openRutaFragment(selected);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle(R.string.rutas_title);
    }

}
