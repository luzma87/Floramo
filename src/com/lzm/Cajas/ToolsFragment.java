package com.lzm.Cajas;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lzm.Cajas.utils.RulerView;

/**
 * Created by luz on 25/11/14.
 */
public class ToolsFragment extends Fragment {
    MapActivity context;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tools_layout, container, false);

        context = (MapActivity) getActivity();

        return view;
    }
}