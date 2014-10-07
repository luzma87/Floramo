package com.lzm.Cajas;

import android.app.ListFragment;
import android.os.Bundle;
import com.lzm.Cajas.adapters.BusquedaResultsEspeciesListAdapter;
import com.lzm.Cajas.adapters.BusquedaTropicosResultAdater;
import com.lzm.Cajas.utils.SearchResult;

import java.util.List;

/**
 * Created by Svt on 10/6/2014.
 */
public class BusquedaTropicosResult extends ListFragment {
    MapActivity activity;
    List<SearchResult> result;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MapActivity) getActivity();

        result = activity.result;



        BusquedaTropicosResultAdater adapter = new BusquedaTropicosResultAdater(getActivity(), result);
        setListAdapter(adapter);
    }
}
