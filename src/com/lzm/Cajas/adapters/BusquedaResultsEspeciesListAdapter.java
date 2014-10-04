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
import com.lzm.Cajas.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by DELL on 03/08/2014.
 */
public class BusquedaResultsEspeciesListAdapter extends ArrayAdapter<Especie> {
    private final Context context;
    private final List<Especie> especies;

    public BusquedaResultsEspeciesListAdapter(Context context, List<Especie> especies) {
        super(context, R.layout.busqueda_results_row, especies);
        this.context = context;
        this.especies = especies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Especie especie = especies.get(position);
        Genero genero = especie.getGenero(context);
        Familia familia = genero.getFamilia(context);
        Color color1 = especie.getColor1(context);
        Color color2 = especie.getColor2(context);
        FormaVida formaVida1 = especie.getFormaVida1(context);
        FormaVida formaVida2 = especie.getFormaVida2(context);

        List<Foto> fotos = Foto.findAllByEspecie(context, especie);
        Foto foto = fotos.get(0);

        int cantFotos = Foto.countByEspecie(context, especie);

        String labelNombreCientifico = genero.nombre + " " + especie.nombre;
        String labelNombreFamilia = familia.nombre;
        String labelCantFotos = "" + cantFotos;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.busqueda_results_row, null);
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

        itemColor1.setText(Utils.getStringResourceByName(context, "global_color_" + color1.nombre));
        if (color2 != null && !color2.nombre.equals("none")) {
            itemColor2.setText(Utils.getStringResourceByName(context, "global_color_" + color2.nombre));
            itemColor2.setVisibility(View.VISIBLE);
        } else {
            itemColor2.setVisibility(View.GONE);
        }

        itemFormaVida1.setImageResource(Utils.getImageResourceByName(context, "ic_fv_" + formaVida1.nombre));
        if (formaVida2 != null && !formaVida2.nombre.equals("none")) {
            itemFormaVida2.setImageResource(Utils.getImageResourceByName(context, "ic_fv_" + formaVida2.nombre));
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
