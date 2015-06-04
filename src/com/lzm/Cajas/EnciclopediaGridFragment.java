package com.lzm.Cajas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.lzm.Cajas.adapters.EncyclopediaGridListAdapter;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 23/07/2014.
 */
public class EnciclopediaGridFragment extends Fragment implements Button.OnClickListener {

    MapActivity context;
    String pathFolder;

    ListView listView;
    int listHeight = 0;
    TextView selectedTextView;

    Button btnCambiarVista;
    Button btnOrder;
    Button btnSort;

    String sort;
    String order;

    String[] sorts;
    String[] keysSorts;

    Integer sortPos = 1;
    Map<String, Integer> mapIndex;
    EncyclopediaGridListAdapter adapter;

    LinearLayout indexLayout;

    List<Especie> especies;

    public EnciclopediaGridFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        pathFolder = Utils.getFolder(context);

        sort = "n";
        order = "a";

        sorts = new String[2];
        keysSorts = new String[2];

        sorts[0] = getString(R.string.captura_nombre_familia_label);
        sorts[1] = getString(R.string.captura_nombre_cientifico_label);
        keysSorts[0] = "f";
        keysSorts[1] = "n";

        Utils.hideSoftKeyboard(this.getActivity());

        View view = inflater.inflate(R.layout.encyclopedia_grid_layout, container, false);

        indexLayout = (LinearLayout) view.findViewById(R.id.side_index);

        btnCambiarVista = (Button) view.findViewById(R.id.encyclopedia_grid_btn);
        btnCambiarVista.setOnClickListener(this);
        btnSort = (Button) view.findViewById(R.id.encyclopedia_sort_btn);
        btnSort.setOnClickListener(this);
        btnOrder = (Button) view.findViewById(R.id.encyclopedia_order_btn);
        btnOrder.setOnClickListener(this);

        listView = (ListView) view.findViewById(R.id.encyclopedia_list);
        ViewTreeObserver vto2 = listView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (listHeight == 0) {
                    listHeight = listView.getHeight();
                }
                displayIndex();
                ViewTreeObserver obs = listView.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
            }
        });

        loadData();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                final int positionToRemove = pos;
                final Especie selected = especies.get(positionToRemove);
                if (selected.esMia == 1) {
                    Vibrator v1 = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v1.vibrate(100);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Chain together various setter methods to set the dialog characteristics
                    builder.setMessage(R.string.enciclopedia_dlg_delete_contenido)
                            .setTitle(R.string.enciclopedia_dlg_delete_title);

                    // Add the buttons
                    builder.setPositiveButton(R.string.global_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button

                            Especie.delete(context, selected);
                            especies.remove(positionToRemove);
                            adapter.notifyDataSetChanged();

                            Toast.makeText(getActivity(), getString(R.string.enciclopedia_delete), Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(R.string.global_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    // Set other dialog properties

                    // Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Especie selected = especies.get(pos);
                Fragment fragment = new EspecieInfoFragment();
                Bundle args = new Bundle();
                args.putLong("especie", selected.id);
                String nombre = selected.genero + " " + selected.nombre + " (" + selected.nombreComun + ")";
                Utils.openFragment(context, fragment, nombre, args);
            }
        });

        return view;
    }

    private void loadData() {
        if (context.enciclopediaPause) {
            adapter = null;
            context.enciclopediaPause = false;
        }
        especies = Especie.sortedList(context, sort, order);
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(especies);
            adapter.notifyDataSetChanged();
        } else {
            adapter = new EncyclopediaGridListAdapter(context, especies);
            listView.setAdapter(adapter);
        }
        getIndexList(especies);
        if (listHeight > 0) {
            displayIndex();
        }
    }

    private void getIndexList(List<Especie> especies) {
        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < especies.size(); i++) {
            String nombre;
            if (sort.equalsIgnoreCase("f")) {
                nombre = especies.get(i).familia;
            } else if (sort.equalsIgnoreCase("n")) {
                nombre = especies.get(i).genero;
            } else {
                nombre = especies.get(i).nombre;
            }
            String index = nombre.substring(0, 1);

            if (mapIndex.get(index) == null) {
                mapIndex.put(index, i);
            }
        }
    }

    private void displayIndex() {
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        indexLayout.removeAllViews();

        int totalH = listHeight;
        int totalItems = indexList.size();

        if (totalH > 0 && totalItems > 0) {
            int h = (totalH / totalItems);
            for (String index : indexList) {
                LayoutInflater inflater = LayoutInflater.from(context);
                TextView textView = (TextView) inflater.inflate(R.layout.encyclopedia_grid_side_index_item, null);
                textView.setText(index);
                textView.setHeight(h);
                textView.setOnClickListener(this);
                indexLayout.addView(textView);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (context.enciclopediaListHeight != 0 && context.enciclopediaListHeight != listHeight) {
            listHeight = context.enciclopediaListHeight;
        }
        context.setTitle(R.string.encyclopedia_title);
        context.mDrawerList.setItemChecked(context.ENCICLOPEDIA_POS, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        context.enciclopediaListHeight = listHeight;
        context.enciclopediaPause = true;
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
        } else {
            if (selectedTextView != null) {
                selectedTextView.setBackgroundResource(R.drawable.border_side_index_item);
            }
            TextView selectedIndex = (TextView) view;
            selectedTextView = selectedIndex;
            selectedIndex.setBackgroundResource(R.drawable.border_side_index_selected_item);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    selectedTextView.setBackgroundResource(R.drawable.border_side_index_item);
                }
            }, 1000);
            listView.setSelection(mapIndex.get(selectedIndex.getText()));
        }
    }
}