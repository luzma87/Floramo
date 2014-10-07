package com.lzm.Cajas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.lzm.Cajas.adapters.EncyclopediaGridListAdapter;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 23/07/2014.
 */
public class EnciclopediaGridFragment extends Fragment implements Button.OnClickListener {

    MapActivity context;
    String pathFolder;

    ListView listView;

    Button btnCambiarVista;
    Button btnOrder;
    Button btnSort;

    String sort;
    String order;

    String[] sorts;
    String[] keysSorts;

    Integer sortPos = 0;

    EncyclopediaGridListAdapter adapter;

    public EnciclopediaGridFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();

        pathFolder = Utils.getFolder(context);

        sort = "f";
        order = "a";

        sorts = new String[2];
        keysSorts = new String[2];

        sorts[0] = getString(R.string.captura_nombre_familia_label);
        sorts[1] = getString(R.string.captura_nombre_cientifico_label);
        keysSorts[0] = "f";
        keysSorts[1] = "n";

        Utils.hideSoftKeyboard(this.getActivity());

        View view = inflater.inflate(R.layout.encyclopedia_grid_layout, container, false);


        btnCambiarVista = (Button) view.findViewById(R.id.encyclopedia_grid_btn);
        btnCambiarVista.setOnClickListener(this);
        btnSort = (Button) view.findViewById(R.id.encyclopedia_sort_btn);
        btnSort.setOnClickListener(this);
        btnOrder = (Button) view.findViewById(R.id.encyclopedia_order_btn);
        btnOrder.setOnClickListener(this);

        listView = (ListView) view.findViewById(R.id.encyclopedia_list);

        loadData();

        return view;
    }

    private void loadData() {
        List<Especie> especies = Especie.sortedList(context, sort, order);
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(especies);
            adapter.notifyDataSetChanged();
        } else {
            adapter = new EncyclopediaGridListAdapter(context, especies);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.encyclopedia_title);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnCambiarVista.getId()) {
            EnciclopediaListFragment fragment = new EnciclopediaListFragment();
            Utils.openFragment(context, fragment, getString(R.string.encyclopedia_title));
        } else if (view.getId() == btnOrder.getId()) {
            if (order.equalsIgnoreCase("a")) {
                order = "d";
                btnOrder.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_order_desc_dark, 0, 0, 0);
            } else if (order.equalsIgnoreCase("d")) {
                order = "a";
                btnOrder.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_order_asc_dark, 0, 0, 0);
            }
            loadData();
        } else if (view.getId() == btnSort.getId()) {
            sortPos++;
            if (sortPos > sorts.length - 1) {
                sortPos = 0;
            }
            btnSort.setText(sorts[sortPos]);
            sort = keysSorts[sortPos];
            loadData();
        }
        System.out.println("ORDER: " + order + " SORT: " + sort);
    }
}