package com.lzm.Cajas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.R;
import com.lzm.Cajas.db.*;
import com.lzm.Cajas.image.ImageUtils;
import com.lzm.Cajas.utils.Utils;

import java.util.List;

//import com.lzm.Cajas.utils.ImageLoader;

/**
 * Created by luz on 01/08/14.
 */
public class EncyclopediaSecondLevelAdapter extends BaseExpandableListAdapter {

    int position;

    List<Genero> generos;
    List<Especie> especies;
    MapActivity context;
    EncyclopediaSecondLevelListView vista;

    public EncyclopediaSecondLevelAdapter(MapActivity context, int position, List<Genero> generos, EncyclopediaSecondLevelListView vista) {
        this.context = context;
        this.position = position;
        this.generos = generos;
        this.vista = vista;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    public Genero getGenero(int pos) {
        return generos.get(pos);
    }

    public Especie getEspecie(int pos) {
        List<Especie> especies = Especie.findAllByGenero(context, getGenero(position));
        return especies.get(pos);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Genero genero = getGenero(position);
        Especie especie = especies.get(childPosition);

        Foto foto = Foto.findByEspecie(context, especie);

        int cantFotos = Foto.countByEspecie(context, especie);
        String labelNombreCientifico = genero.nombre + " " + especie.nombre;
        String labelCantFotos = "" + cantFotos;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.encyclopedia_nivel_3, null);
        }
        TextView itemNombreCientifico = (TextView) convertView.findViewById(R.id.encyclopedia_group_item_nivel_3_nombre_cientifico);
        TextView itemCantFotos = (TextView) convertView.findViewById(R.id.encyclopedia_group_item_nivel_3_cant_fotos);
        ImageView itemFoto = (ImageView) convertView.findViewById(R.id.encyclopedia_group_item_nivel_3_image);

        ImageView itemFv1 = (ImageView) convertView.findViewById(R.id.encyclopedia_group_item_nivel_3_fv1);
        ImageView itemFv2 = (ImageView) convertView.findViewById(R.id.encyclopedia_group_item_nivel_3_fv2);

        ImageView itemColor1 = (ImageView) convertView.findViewById(R.id.encyclopedia_group_item_nivel_3_cl1);
        ImageView itemColor2 = (ImageView) convertView.findViewById(R.id.encyclopedia_group_item_nivel_3_cl2);

        itemNombreCientifico.setText(labelNombreCientifico);
        itemCantFotos.setText(labelCantFotos);
        if (foto != null) {
            if (foto.esMia == 1) {
                itemFoto.setImageBitmap(ImageUtils.decodeFile(foto.path, 100, 100, false));
            } else {
                String path = foto.path.replaceAll("\\.jpg", "").replaceAll("-", "_").toLowerCase();
                path = "th_" + path;
                itemFoto.setImageResource(Utils.getImageResourceByName(context, path));
            }
        }
        itemFv1.setImageResource(Utils.getImageResourceByName(context, "ic_fv_" + especie.formaVida1));
        itemFv1.setVisibility(View.VISIBLE);
        if (especie.formaVida2 != null && !especie.formaVida2.equals("none")) {
            itemFv2.setImageResource(Utils.getImageResourceByName(context, "ic_fv_" + especie.formaVida2));
            itemFv2.setVisibility(View.VISIBLE);
        } else {
            itemFv2.setVisibility(View.GONE);
        }

        itemColor1.setImageResource(Utils.getImageResourceByName(context, "ic_cl_" + especie.color1 + "_tiny"));
        if (especie.color2 != null && !especie.color2.equals("none")) {
            itemColor2.setImageResource(Utils.getImageResourceByName(context, "ic_cl_" + especie.color2 + "_tiny"));
            itemColor2.setVisibility(View.VISIBLE);
        } else {
            itemColor2.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Genero genero = getGenero(position);
        especies = Especie.findAllByGenero(context, genero);
        vista.especies = especies;
        return especies.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return "--" + groupPosition;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String label = generos.get(position).nombre;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.encyclopedia_nivel_2, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.encyclopedia_group_item_nivel_2_lbl);
        item.setText(label);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}