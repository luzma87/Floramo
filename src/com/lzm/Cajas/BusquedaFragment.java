package com.lzm.Cajas;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 13/08/2014.
 */
public class BusquedaFragment extends Fragment implements Button.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {
    MapActivity activity;

    ImageButton btnBuscar;

    String[] coloresNombres;
    ToggleButton[] coloresViews;

    String[] fvNombres;
    ToggleButton[] fvViews;

    EditText nombreTxt;

    RadioButton radioAnd;
    RadioButton radioOr;

    TextView lblInfoAndOr;
    TextView lblInfoFormaVida;
    TextView lblInfoColor;
    TextView lblInfoNombre;

    ArrayList<String> searchFormaVida;
    ArrayList<String> searchColor;
    String searchNombre;
    String searchAndOr;

    final int marginPx = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MapActivity) getActivity();
        View view = inflater.inflate(R.layout.busqueda_layout, container, false);

        activity.activeFragment = activity.BUSQUEDA_POS;

        btnBuscar = (ImageButton) view.findViewById(R.id.busqueda_buscar_btn);
        btnBuscar.setOnClickListener(this);

        searchFormaVida = new ArrayList<String>();
        searchColor = new ArrayList<String>();
        searchNombre = "";
        searchAndOr = "AND";

        lblInfoAndOr = (TextView) view.findViewById(R.id.busqueda_info_and_or_lbl);
        lblInfoFormaVida = (TextView) view.findViewById(R.id.busqueda_info_forma_vida_lbl);
        lblInfoColor = (TextView) view.findViewById(R.id.busqueda_info_color_lbl);
        lblInfoNombre = (TextView) view.findViewById(R.id.busqueda_info_nombre_lbl);

        nombreTxt = (EditText) view.findViewById(R.id.busqueda_nombre_txt);
        nombreTxt.addTextChangedListener(this);

        radioAnd = (RadioButton) view.findViewById(R.id.busqueda_radio_and);
        radioAnd.setOnClickListener(this);
        radioOr = (RadioButton) view.findViewById(R.id.busqueda_radio_or);
        radioOr.setOnClickListener(this);

        initColores(view);
        initFormasVida(view);
        updateAll();

        return view;
    }

    private void updateAll() {
        updateAndOr();
        updateFormaVida();
        updateColor();
        updateNombre();
    }

    private void updateNombre() {
        String info = "";
        String nombre = nombreTxt.getText().toString().trim();
        if (!nombre.equals("")) {
            searchNombre = nombre;
            info += getString(R.string.busqueda_info_nombre, nombre);
        }
        if (!info.equals("")) {
            lblInfoNombre.setVisibility(View.VISIBLE);
            lblInfoNombre.setText(info);
        } else {
            lblInfoNombre.setVisibility(View.GONE);
        }
    }

    private void updateColor() {
        String color = "";
        searchColor = new ArrayList<String>();
        for (int i = 0; i < coloresViews.length; i++) {
            ToggleButton toggle = coloresViews[i];
            if (toggle.isChecked()) {
                if (!color.equals("")) {
                    color += ", ";
                }
                color += Utils.getStringResourceByName(activity, "global_color_" + coloresNombres[i]);
                searchColor.add(coloresNombres[i]);
            }
        }
        if (color.equals("")) {
            lblInfoColor.setVisibility(View.GONE);
        } else {
            lblInfoColor.setText(color);
            lblInfoColor.setVisibility(View.VISIBLE);
        }
    }

    private void updateFormaVida() {
        String fv = "";
        searchFormaVida = new ArrayList<String>();
        for (int i = 0; i < fvViews.length; i++) {
            ToggleButton toggle = fvViews[i];
            if (toggle.isChecked()) {
                if (!fv.equals("")) {
                    fv += ", ";
                }
                fv += Utils.getStringResourceByName(activity, "global_forma_vida_" + fvNombres[i]);
                searchFormaVida.add(fvNombres[i]);
            }
        }
        if (fv.equals("")) {
            lblInfoFormaVida.setVisibility(View.GONE);
        } else {
            lblInfoFormaVida.setText(fv);
            lblInfoFormaVida.setVisibility(View.VISIBLE);
        }
    }

    private void updateAndOr() {
        String info = "";
        Boolean and = radioAnd.isChecked();
        if (and) {
            searchAndOr = "AND";
            info += getString(R.string.busqueda_info_and);
        } else {
            searchAndOr = "OR";
            info += getString(R.string.busqueda_info_or);
        }
        if (!info.equals("")) {
            lblInfoAndOr.setVisibility(View.VISIBLE);
            lblInfoAndOr.setText(info);
        } else {
            lblInfoAndOr.setVisibility(View.GONE);
        }
    }

    private ToggleButton makeToggleButton(Drawable icon, String title) {
        int padding = 5;
        int width = 55;
        ToggleButton tb = new ToggleButton(activity);
        tb.setTextOn(title);
        tb.setTextOff(title);
        tb.setText(title);
        tb.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        tb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//        tb.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tb.setLayoutParams(lp);

        tb.setWidth(Utils.dp2px(activity, width));
        tb.setPadding(Utils.dp2px(activity, padding), Utils.dp2px(activity, padding), Utils.dp2px(activity, padding), Utils.dp2px(activity, padding));
        tb.setOnCheckedChangeListener(this);

        return tb;
    }

    private void initColores(View view) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.busqueda_color_layout);

        coloresNombres = new String[9];
        coloresNombres[0] = "white";
        coloresNombres[1] = "green";
        coloresNombres[2] = "pink";
        coloresNombres[3] = "yellow";
        coloresNombres[4] = "orange";
        coloresNombres[5] = "purple";
        coloresNombres[6] = "brown";
        coloresNombres[7] = "red";
        coloresNombres[8] = "blue";

        coloresViews = new ToggleButton[coloresNombres.length];
        for (int i = 0; i < coloresNombres.length; i++) {
            String color = coloresNombres[i];
            ToggleButton tb = makeToggleButton(activity.getResources().getDrawable(Utils.getImageResourceByName(activity, "ic_cl_" + color + "_tiny")),
                    Utils.getStringResourceByName(activity, "global_color_" + color));
            layout.addView(tb);
            coloresViews[i] = tb;
        }
        populateViews(layout, coloresViews, activity, null);
    }

    private void initFormasVida(View view) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.busqueda_forma_vida_layout);

        fvNombres = new String[8];
        fvNombres[0] = "cushion";
        fvNombres[1] = "herb";
        fvNombres[2] = "shrub";
        fvNombres[3] = "grass";
        fvNombres[4] = "rosette";
        fvNombres[5] = "aquatic";
        fvNombres[6] = "liana";
        fvNombres[7] = "tree";

        fvViews = new ToggleButton[fvNombres.length];
        for (int i = 0; i < fvNombres.length; i++) {
            String fv = fvNombres[i];
            ToggleButton tb = makeToggleButton(activity.getResources().getDrawable(Utils.getImageResourceByName(activity, "ic_fv_" + fv + "_tiny")),
                    Utils.getStringResourceByName(activity, "global_forma_vida_" + fv));
            layout.addView(tb);
            fvViews[i] = tb;
        }
        populateViews(layout, fvViews, activity, null);
    }

    @Override
    public void onClick(View v) {
        Utils.hideSoftKeyboard(this.getActivity());
        if (v.getId() == btnBuscar.getId()) {
            updateAll();
            activity.searchFormaVida = new ArrayList<String>(searchFormaVida);
            activity.searchColor = new ArrayList<String>(searchColor);
            activity.searchNombre = searchNombre;
            activity.searchAndOr = searchAndOr;

            activity.especiesBusqueda = Especie.busqueda(activity, searchFormaVida, searchColor, searchNombre, searchAndOr);
            ListFragment fragment = new BusquedaResultsFragment();
            Utils.openFragment(activity, fragment, getString(R.string.busqueda_title));
        } else {
            updateAndOr();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        System.out.println("*************************************************** PAUSE ************************************");
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle(R.string.busqueda_title);
        updateAll();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        updateNombre();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        updateColor();
        updateFormaVida();
    }

    /**
     * Copyright 2011 Sherif
     * Updated by Karim Varela to handle LinearLayouts with other views on either side.
     *
     * @param linearLayout
     * @param views        : The views to wrap within LinearLayout
     * @param context
     * @param extraView    : An extra view that may be to the right or left of your LinearLayout.
     * @author Karim Varela
     */
    private void populateViews(LinearLayout linearLayout, View[] views, Context context, View extraView) {
        if (extraView != null) {
            extraView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // kv : May need to replace 'getSherlockActivity()' with 'this' or 'getActivity()'
//        Display display = activity.getWindowManager().getDefaultDisplay();
        linearLayout.removeAllViews();
        int maxWidth = activity.screenWidth - Utils.dp2px(activity, 20);
//        if (extraView != null) {
//            maxWidth = display.getWidth() - extraView.getMeasuredWidth() - 20;
//        } else {
//            maxWidth = display.getWidth() ;
//        }
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params;
        LinearLayout newLL = new LinearLayout(context);
        newLL.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newLL.setGravity(Gravity.LEFT);
        newLL.setOrientation(LinearLayout.HORIZONTAL);

        int widthSoFar = 0;

        for (int i = 0; i < views.length; i++) {
            LinearLayout LL = new LinearLayout(context);
            LL.setOrientation(LinearLayout.HORIZONTAL);
            LL.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            LL.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            views[i].measure(0, 0);
            params = new LinearLayout.LayoutParams(views[i].getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, Utils.dp2px(activity, marginPx), 0);

            LL.addView(views[i], params);
            LL.measure(0, 0);
            widthSoFar += views[i].getMeasuredWidth() + marginPx;
            if (widthSoFar >= maxWidth) {
                linearLayout.addView(newLL);

                newLL = new LinearLayout(context);
//                newLL.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, Utils.dp2px(activity, marginPx), 0, 0);
                newLL.setLayoutParams(params);

                newLL.setOrientation(LinearLayout.HORIZONTAL);
                newLL.setGravity(Gravity.LEFT);
                params = new LinearLayout.LayoutParams(LL.getMeasuredWidth(), LL.getMeasuredHeight());
//                params.setMargins(0, 0, Utils.dp2px(activity, marginPx), 0);
                newLL.addView(LL, params);
                widthSoFar = LL.getMeasuredWidth() + marginPx;
            } else {
                newLL.addView(LL);
            }
        }
        linearLayout.addView(newLL);
    }
}