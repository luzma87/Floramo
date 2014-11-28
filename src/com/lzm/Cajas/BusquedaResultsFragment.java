package com.lzm.Cajas;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.lzm.Cajas.adapters.BusquedaResultsEspeciesListAdapter;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.db.Foto;
import com.lzm.Cajas.utils.Utils;

import java.util.List;

/**
 * Created by DELL on 16/08/2014.
 */
public class BusquedaResultsFragment extends ListFragment {

    MapActivity activity;
    List<Especie> especiesList;

    int fotoPos = 0;
    String dialogTitle = "";
    List<Foto> fotos;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MapActivity) getActivity();

        activity.activeFragment = activity.RESULTADO_BUSQUEDA_POS;
        especiesList = activity.especiesBusqueda;

        BusquedaResultsEspeciesListAdapter adapter = new BusquedaResultsEspeciesListAdapter(getActivity(), especiesList);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Especie selected = especiesList.get(position);
        Fragment fragment = new EspecieInfoFragment();
        Bundle args = new Bundle();
        args.putLong("especie", selected.id);

        String nombre = selected.getNombreCientifico() + " (" + selected.nombreComun + ")";

        Utils.openFragment(activity, fragment, nombre, args);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle(R.string.busqueda_title);
    }
}