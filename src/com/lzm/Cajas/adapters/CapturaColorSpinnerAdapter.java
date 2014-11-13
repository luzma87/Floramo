package com.lzm.Cajas.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.R;
import com.lzm.Cajas.db.Color;
import com.lzm.Cajas.utils.Utils;

import java.util.ArrayList;

/**
 * Created by luz on 28/07/14.
 */
public class CapturaColorSpinnerAdapter extends BaseAdapter {

    MapActivity c;
    ArrayList<Color> colores;

    public CapturaColorSpinnerAdapter(MapActivity context, ArrayList<Color> colores) {
        super();
        this.c = context;
        this.colores = colores;
    }

    @Override
    public int getCount() {
        return colores.size();
    }

    @Override
    public Object getItem(int position) {
        return colores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Color cur_obj = colores.get(position);
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View row = inflater.inflate(R.layout.captura_color_select_row, parent, false);
        TextView sub = (TextView) row.findViewById(R.id.captura_row_color_label);
        ImageView img = (ImageView) row.findViewById(R.id.captura_row_color_img);

        sub.setText(Utils.getStringResourceByName(c, "global_color_" + cur_obj.getNombre()));
        img.setImageResource(Utils.getImageResourceByName(c, "ic_cl_" + cur_obj.nombre));
        return row;
    }
}
