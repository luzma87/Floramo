package com.lzm.Cajas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lzm.Cajas.R;
import com.lzm.Cajas.db.*;
import com.lzm.Cajas.image.ImageUtils;
import com.lzm.Cajas.utils.SearchResult;
import com.lzm.Cajas.utils.Utils;

import java.util.List;

/**
 * Created by Svt on 10/6/2014.
 */
public class BusquedaTropicosResultAdater extends ArrayAdapter<SearchResult> {
    private final Context context;
    private final List<SearchResult> results;

    public BusquedaTropicosResultAdater(Context context, List<SearchResult> results) {
        super(context, R.layout.busqueda_tropicos_result, results);
        this.context = context;
        this.results = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchResult result = results.get(position);


        String labelNombreCientifico = result.scientificName;
        String labelNombreFamilia = result.family;
        String labelCantFotos = "" + 0;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.busqueda_tropicos_result, null);
        }


        TextView itemNombreCientifico = (TextView) convertView.findViewById(R.id.busqueda_results_nombre_cientifico);
        TextView itemNombreFamilia = (TextView) convertView.findViewById(R.id.busqueda_results_familia);

        itemNombreCientifico.setText(labelNombreCientifico);
        itemNombreFamilia.setText(labelNombreFamilia);

        return convertView;
    }
}
