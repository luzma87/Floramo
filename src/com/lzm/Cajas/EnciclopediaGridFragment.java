package com.lzm.Cajas;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.lzm.Cajas.adapters.EncyclopediaGridListAdapter;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.utils.Utils;

import java.util.*;

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

    Integer sortPos = 0;
    Map<String, Integer> mapIndex;
    EncyclopediaGridListAdapter adapter;

    LinearLayout indexLayout;

    public EnciclopediaGridFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();

        /*
        ColorStateList oldColors =  textView.getTextColors(); //save original colors
        textView.setTextColor(Color.RED);
        ....
        textView.setTextColor(oldColors);//restore original colors
         */

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

        indexLayout = (LinearLayout) view.findViewById(R.id.side_index);

        btnCambiarVista = (Button) view.findViewById(R.id.encyclopedia_grid_btn);
        btnCambiarVista.setOnClickListener(this);
        btnSort = (Button) view.findViewById(R.id.encyclopedia_sort_btn);
        btnSort.setOnClickListener(this);
        btnOrder = (Button) view.findViewById(R.id.encyclopedia_order_btn);
        btnOrder.setOnClickListener(this);
//        ViewTreeObserver vto = btnOrder.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.d("TEST", "Height = " + btnOrder.getHeight() + " Width = " + btnOrder.getWidth());
//                ViewTreeObserver obs = btnOrder.getViewTreeObserver();
//                obs.removeGlobalOnLayoutListener(this);
//            }
//        });

        listView = (ListView) view.findViewById(R.id.encyclopedia_list);
        ViewTreeObserver vto2 = listView.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                listHeight = listView.getHeight();
                displayIndex();
                ViewTreeObserver obs = listView.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
            }
        });

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