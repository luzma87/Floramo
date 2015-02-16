package com.lzm.Cajas;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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

        setEmptyText(getResources().getString(R.string.list_no_results));
        TextView emptyTextView = (TextView) getListView().getEmptyView();
        emptyTextView.setTextColor(getResources().getColor(R.color.list_no_results));

//        //System.out.println("creando busqueda results.....");
//        //System.out.println(";;; " + savedInstanceState);
        if (savedInstanceState != null) {
//            //System.out.println("... " + savedInstanceState.getSerializable("activeFragment").toString());
            activity.searchFormaVida = savedInstanceState.getStringArrayList("searchFormaVida");
            activity.searchColor = savedInstanceState.getStringArrayList("searchColor");
            activity.searchNombre = savedInstanceState.getString("searchNombre");
            activity.searchAndOr = savedInstanceState.getString("searchAndOr");
            activity.especiesBusqueda = Especie.busqueda(activity, activity.searchFormaVida, activity.searchColor, activity.searchNombre, activity.searchAndOr);
        }

        activity.activeFragment = activity.RESULTADO_BUSQUEDA_POS;
        especiesList = activity.especiesBusqueda;

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> " + especiesList);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>> " + especiesList.size());

        BusquedaResultsEspeciesListAdapter adapter = new BusquedaResultsEspeciesListAdapter(getActivity(), especiesList);
        setListAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
//        //System.out.println("save state fragment ");
        savedInstanceState.putSerializable("activeFragment", activity.activeFragment);
        savedInstanceState.putSerializable("searchFormaVida", activity.searchFormaVida);
        savedInstanceState.putSerializable("searchColor", activity.searchColor);
        savedInstanceState.putSerializable("searchNombre", activity.searchNombre);
        savedInstanceState.putSerializable("searchAndOr", activity.searchAndOr);
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