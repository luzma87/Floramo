package com.lzm.Cajas;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.lzm.Cajas.utils.RulerView;
import com.lzm.Cajas.utils.Utils;

/**
 * Created by luz on 25/11/14.
 */
public class ToolsFragment extends Fragment {
    MapActivity context;

    Button btnGps;
    ImageButton btnConvert;

    EditText txtCm;
    EditText txtIn;

    boolean inches2cm = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tools_layout, container, false);

        context = (MapActivity) getActivity();

        btnGps = (Button) view.findViewById(R.id.tools_btn_gps);
        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openFragment(context, new CompassFragment(), getString(R.string.tools_title));
            }
        });

        btnConvert = (ImageButton) view.findViewById(R.id.tools_btn_convert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inches2cm) {
                    double in = Double.parseDouble(txtIn.getText().toString());
                    double cm = in2cm(in);
                    txtCm.setText("" + cm);
                } else {
                    double cm = Double.parseDouble(txtCm.getText().toString());
                    double in = cm2in(cm);
                    txtIn.setText("" + in);
                }
            }
        });

        txtCm = (EditText) view.findViewById(R.id.tools_txt_cm);
        txtIn = (EditText) view.findViewById(R.id.tools_txt_in);

        txtCm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                inches2cm = false;
                return false;
            }
        });

        txtIn.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                inches2cm = true;
                return false;
            }
        });

        return view;
    }

    private double cm2in(double cm) {
//        double cm = Double.parseDouble(txtCm.getText().toString());
        double in = cm / 2.54;
        in = (double) Math.round(in * 1000) / 1000;
        return in;
    }

    private double in2cm(double in) {
//        double in = Double.parseDouble(txtIn.getText().toString());
        double cm = in * 2.54;
        cm = (double) Math.round(cm * 1000) / 1000;
        return cm;
    }

}