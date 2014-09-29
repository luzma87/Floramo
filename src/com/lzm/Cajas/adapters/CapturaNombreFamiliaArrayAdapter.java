package com.lzm.Cajas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.lzm.Cajas.MainActivity;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.R;
import com.lzm.Cajas.db.Familia;

import java.util.List;

/**
 * Created by luz on 04/08/14.
 */
public class CapturaNombreFamiliaArrayAdapter extends ArrayAdapter<Familia> {

    MapActivity context;
    List<Familia> familias;
    int layoutResourceId;

    public CapturaNombreFamiliaArrayAdapter(MapActivity context, int layoutResourceId, List<Familia> familias) {
        super(context, layoutResourceId, familias);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.familias = familias;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layoutResourceId, parent, false);

        // object item based on the position
        Familia objectItem = familias.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) rowView.findViewById(R.id.captura_autocomplete_item_texto);
        textViewItem.setText(objectItem.nombre);

        // in case you want to add some style, you can do something like:
//        textViewItem.setBackgroundColor(Color.CYAN);

        return rowView;
    }
}
