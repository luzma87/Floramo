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
import com.lzm.Cajas.db.FormaVida;
import com.lzm.Cajas.utils.Utils;

import java.util.ArrayList;

/**
 * Created by luz on 28/07/14.
 */
public class CapturaFormaVidaSpinnerAdapter extends BaseAdapter {

    MapActivity c;
    ArrayList<FormaVida> formasVida;

    public CapturaFormaVidaSpinnerAdapter(MapActivity context, ArrayList<FormaVida> formasVida) {
        super();
        this.c = context;
        this.formasVida = formasVida;
    }

    @Override
    public int getCount() {
        return formasVida.size();
    }

    @Override
    public Object getItem(int position) {
        return formasVida.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        FormaVida cur_obj = formasVida.get(position);
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View row = inflater.inflate(R.layout.captura_forma_vida_select_row, parent, false);
        TextView sub = (TextView) row.findViewById(R.id.captura_row_forma_vida_label);
        ImageView img = (ImageView) row.findViewById(R.id.captura_row_forma_vida_image);

        sub.setText(Utils.getStringResourceByName(c, "global_forma_vida_" + cur_obj.getNombre()));
        img.setImageResource(Utils.getImageResourceByName(c, "ic_fv_" + cur_obj.nombre));
        return row;
    }
}
