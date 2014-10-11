package com.lzm.Cajas.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.lzm.Cajas.*;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.db.Familia;
import com.lzm.Cajas.db.Genero;
import com.lzm.Cajas.utils.Utils;

import java.util.List;

/**
 * Created by luz on 01/08/14.
 */
public class EncyclopediaFirstLevelAdapter extends BaseExpandableListAdapter {

    MapActivity activity;
    EnciclopediaListFragment fragment;

    List<Familia> familias;

    public EncyclopediaFirstLevelAdapter(MapActivity activity, EnciclopediaListFragment fragment, List<Familia> familias) {
        this.familias = familias;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public Object getChild(int arg0, int arg1) {
        return arg1;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public Familia getFamilia(int pos) {
        return familias.get(pos);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        EncyclopediaSecondLevelListView secondLevelexplv = new EncyclopediaSecondLevelListView(activity);
        final List<Genero> generos = Genero.findAllByFamilia(activity, getFamilia(groupPosition));
        secondLevelexplv.setAdapter(new EncyclopediaSecondLevelAdapter(activity, childPosition, generos, secondLevelexplv));
        secondLevelexplv.setGroupIndicator(null);
        final int gp = childPosition;

        secondLevelexplv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                List<Especie> especies = Especie.findAllByGenero(activity, generos.get(gp));
                Especie selected = especies.get(childPosition);

                Fragment fragment = new EspecieInfoFragment();
                Bundle args = new Bundle();
                args.putLong("especie", selected.id);
                String nombre = selected.genero + " " + selected.nombre + " (" + selected.nombreComun + ")";
                Utils.openFragment(activity, fragment, nombre, args);
                return true;
            }
        });

        return secondLevelexplv;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Familia familia = familias.get(groupPosition);
        return Genero.countByFamilia(activity, familia);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return familias.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String label = getFamilia(groupPosition).nombre;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.encyclopedia_nivel_1, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.encyclopedia_group_item_nivel_1_lbl);
        item.setText(label);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}