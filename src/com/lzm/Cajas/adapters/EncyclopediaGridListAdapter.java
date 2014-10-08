package com.lzm.Cajas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lzm.Cajas.*;
import com.lzm.Cajas.db.*;
import com.lzm.Cajas.image.ImageUtils;
import com.lzm.Cajas.utils.Utils;

import java.util.List;

/**
 * Created by luz on 01/08/14.
 */
public class EncyclopediaGridListAdapter extends ArrayAdapter<Especie> {

    MapActivity context;
    EnciclopediaGridFragment fragment;

    List<Especie> especies;
//    List<Genero> generos;

    public EncyclopediaGridListAdapter(MapActivity context, List<Especie> especies) {
        super(context, R.layout.encyclopedia_grid_row, especies);
        this.context = context;
        this.especies = especies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Especie especie = especies.get(position);

        List<Foto> fotos = Foto.findAllByEspecie(context, especie);
        Foto foto = fotos.get(0);

        int cantFotos = Foto.countByEspecie(context, especie);

        String labelNombreCientifico = especie.genero + " " + especie.nombre;
        String labelNombreFamilia = especie.familia;
        String labelCantFotos = "" + cantFotos;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.encyclopedia_grid_row, null);
        }

        ImageView itemFoto = (ImageView) convertView.findViewById(R.id.busqueda_results_image);

        TextView itemNombreCientifico = (TextView) convertView.findViewById(R.id.busqueda_results_nombre_cientifico);
        TextView itemNombreFamilia = (TextView) convertView.findViewById(R.id.busqueda_results_familia);
        TextView itemCantFotos = (TextView) convertView.findViewById(R.id.busqueda_results_cant_fotos);
        TextView itemColor1 = (TextView) convertView.findViewById(R.id.busqueda_results_color1);
        TextView itemColor2 = (TextView) convertView.findViewById(R.id.busqueda_results_color2);
        ImageView itemFormaVida1 = (ImageView) convertView.findViewById(R.id.busqueda_results_fv_1);
        ImageView itemFormaVida2 = (ImageView) convertView.findViewById(R.id.busqueda_results_fv_2);

        itemNombreCientifico.setText(labelNombreCientifico);
        itemNombreFamilia.setText(labelNombreFamilia);
        itemCantFotos.setText(labelCantFotos);

        itemColor1.setText(Utils.getStringResourceByName(context, "global_color_" + especie.color1));
        if (especie.color2 != null && !especie.color2.equals("none")) {
            itemColor2.setText(Utils.getStringResourceByName(context, "global_color_" + especie.color2));
            itemColor2.setVisibility(View.VISIBLE);
        } else {
            itemColor2.setVisibility(View.GONE);
        }

        itemFormaVida1.setImageResource(Utils.getImageResourceByName(context, "ic_fv_" + especie.formaVida1 + "_tiny"));
        if (especie.formaVida2 != null && !especie.formaVida2.equals("none")) {
            itemFormaVida2.setImageResource(Utils.getImageResourceByName(context, "ic_fv_" + especie.formaVida2 + "_tiny"));
            itemFormaVida2.setVisibility(View.VISIBLE);
        } else {
            itemFormaVida2.setVisibility(View.GONE);
        }

        if (foto.esMia == 1) {
            itemFoto.setImageBitmap(ImageUtils.decodeFile(foto.path, 100, 100, false));
        } else {
            String path = foto.path.replaceAll("\\.jpg", "").replaceAll("-", "_").toLowerCase();
            path = "th_" + path;
            itemFoto.setImageResource(Utils.getImageResourceByName(context, path));
        }

        return convertView;
    }
}