package com.lzm.Cajas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import com.lzm.Cajas.adapters.EncyclopediaFirstLevelAdapter;
import com.lzm.Cajas.db.Familia;
import com.lzm.Cajas.utils.Utils;

import java.util.List;

/**
 * Created by DELL on 23/07/2014.
 */
public class EnciclopediaListFragment extends Fragment implements Button.OnClickListener {

    MapActivity context;
    String pathFolder;

    ExpandableListView expandableListView;

    Button btnCambiarVista;

    public EnciclopediaListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        pathFolder = Utils.getFolder(context);
        Utils.hideSoftKeyboard(this.getActivity());

        View view = inflater.inflate(R.layout.encyclopedia_list_layout, container, false);
        List<Familia> familias = Familia.findAllByTieneEspecies(context);

        btnCambiarVista = (Button) view.findViewById(R.id.encyclopedia_lvl1_btn);
        btnCambiarVista.setOnClickListener(this);

        expandableListView = (ExpandableListView) view.findViewById(R.id.encyclopedia_level_1);
        expandableListView.setAdapter(new EncyclopediaFirstLevelAdapter(context, this, familias));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.encyclopedia_title);
        context.mDrawerList.setItemChecked(context.ENCICLOPEDIA_POS, true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnCambiarVista.getId()) {
            EnciclopediaGridFragment fragment = new EnciclopediaGridFragment();
            Utils.openFragment(context, fragment, getString(R.string.encyclopedia_title));
        }
    }
}