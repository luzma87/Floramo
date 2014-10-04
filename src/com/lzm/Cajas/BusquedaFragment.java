package com.lzm.Cajas;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lzm.Cajas.adapters.CapturaColorSpinnerAdapter;
import com.lzm.Cajas.adapters.CapturaFormaVidaSpinnerAdapter;
import com.lzm.Cajas.db.Color;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.db.FormaVida;
import com.lzm.Cajas.utils.Utils;

import java.util.ArrayList;

/**
 * Created by DELL on 13/08/2014.
 */
public class BusquedaFragment extends Fragment implements Button.OnClickListener, AdapterView.OnItemSelectedListener,
        TextWatcher, View.OnTouchListener {
    MapActivity context;

    ImageButton btnBuscar;

    EditText nombreTxt;

    RadioButton radioAnd;
    RadioButton radioOr;

    TextView lblInfoAndOr;
    TextView lblInfoFormaVida;
    TextView lblInfoColor;
    TextView lblInfoNombre;

    String searchFormaVida;
    String searchColor;
    String searchNombre;
    String searchAndOr;

    private Spinner spinnerColor;
    private Spinner spinnerFormaVida;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        View view = inflater.inflate(R.layout.busqueda_layout, container, false);
        view.setOnTouchListener(this);

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

        btnBuscar = (ImageButton) view.findViewById(R.id.busqueda_buscar_btn);
        btnBuscar.setOnClickListener(this);

        searchFormaVida = "";
        searchColor = "";
        searchNombre = "";
        searchAndOr = "AND";

        initSpinners(view);
        updateAll();
        return view;
    }

    private void initSpinners(View view) {
        ArrayList<Color> colores = Color.list(context);
        ArrayList<FormaVida> formasVida = FormaVida.list(context);

        spinnerColor = (Spinner) view.findViewById(R.id.busqueda_color_spinner);
        spinnerColor.setAdapter(new CapturaColorSpinnerAdapter(context, colores));
        spinnerColor.setOnItemSelectedListener(this);

        spinnerFormaVida = (Spinner) view.findViewById(R.id.busqueda_forma_vida_spinner);
        spinnerFormaVida.setAdapter(new CapturaFormaVidaSpinnerAdapter(context, formasVida));
        spinnerFormaVida.setOnItemSelectedListener(this);
    }

    private void updateAll() {
        updateAndOr();
        updateFormaVida();
        updateColor();
        updateNombre();
    }

    private void updateFormaVida() {
        String info = "";
        String formaVida = spinnerFormaVida.getSelectedItem().toString().replaceAll("\\*", "");
        if (!formaVida.equals("none")) {
            searchFormaVida = formaVida;
            int id = getResources().getIdentifier("global_forma_vida_" + formaVida, "string", context.getPackageName());
            if (id > 0) {
                String fv = Utils.getStringResourceByName(context, "global_forma_vida_" + formaVida);
                info = getString(R.string.busqueda_info_forma_vida, fv);
            }
        }
        if (!info.equals("")) {
            lblInfoFormaVida.setVisibility(View.VISIBLE);
            lblInfoFormaVida.setText(info);
        } else {
            lblInfoFormaVida.setVisibility(View.GONE);
        }
    }

    private void updateColor() {
        String info = "";
        String color = spinnerColor.getSelectedItem().toString().replaceAll("\\*", "");
        if (!color.equals("none")) {
            searchColor = color;
            int id = getResources().getIdentifier("global_color_" + color, "string", context.getPackageName());
            if (id > 0) {
                String c = Utils.getStringResourceByName(context, "global_color_" + color);
                info = getString(R.string.busqueda_info_color, c);
            }
        }
        if (!info.equals("")) {
            lblInfoColor.setVisibility(View.VISIBLE);
            lblInfoColor.setText(info);
        } else {
            lblInfoColor.setVisibility(View.GONE);
        }
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

    @Override
    public void onClick(View v) {
        Utils.hideSoftKeyboard(this.getActivity());
        if (v.getId() == btnBuscar.getId()) {
            updateAll();
            context.especiesBusqueda = Especie.busqueda(context, searchFormaVida, searchColor, searchNombre, searchAndOr);
            ListFragment fragment = new BusquedaResultsFragment();
            Utils.openFragment(context, fragment, getString(R.string.busqueda_title));
        } else {
            updateAndOr();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Utils.hideSoftKeyboard(this.getActivity());
        updateColor();
        updateFormaVida();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Utils.hideSoftKeyboard(this.getActivity());
        updateColor();
        updateFormaVida();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        updateNombre();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Utils.hideSoftKeyboard(context);
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.busqueda_title);
    }
}