package com.lzm.Cajas;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.lzm.Cajas.adapters.BusquedaTropicosResultAdater;
import com.lzm.Cajas.utils.SearchResult;
import com.lzm.Cajas.utils.Utils;

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


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        activity.posSearch=position;
        Fragment fragment = new ResultInfoFragment();
        Utils.openFragment(activity, fragment, getString(R.string.tropicos));
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle(R.string.busqueda_title);
    }
}
