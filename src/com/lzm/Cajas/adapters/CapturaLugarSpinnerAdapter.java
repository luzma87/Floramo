package com.lzm.Cajas.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.R;
import com.lzm.Cajas.db.Lugar;

import java.util.ArrayList;

/**
 * Created by luz on 28/07/14.
 */
public class CapturaLugarSpinnerAdapter extends BaseAdapter {

    MapActivity c;
    ArrayList<Lugar> lugares;

    public CapturaLugarSpinnerAdapter(MapActivity context, ArrayList<Lugar> lugares) {
        super();
        this.c = context;
        this.lugares = lugares;
    }

    @Override
    public int getCount() {
        return lugares.size();
    }

    @Override
    public Object getItem(int position) {
        return lugares.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Lugar cur_obj = lugares.get(position);
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View row = inflater.inflate(R.layout.captura_lugar_select_row, parent, false);
        TextView sub = (TextView) row.findViewById(R.id.captura_row_lugar_label);
        sub.setText(cur_obj.nombre);
        return row;
    }
}
