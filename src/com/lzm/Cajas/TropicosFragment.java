package com.lzm.Cajas;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.lzm.Cajas.utils.BusquedaLoader;
import com.lzm.Cajas.utils.EspecieLoader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by luz on 01/10/14.
 *
 * Here is your API key:
 * 34a6225b-552c-4e0b-9937-fe12a2541176
 * Best wishes,
 * Heather
 * Missouri Botanical Garden
 *
 * Help is here:
 * http://services.tropicos.org/help
 * Example calls (using your api key):
 * name search:
 * http://services.tropicos.org/Name/Search?name=poa+annua&type=wildcard&apikey=34a6225b-552c-4e0b-9937-fe12a2541176&format=xml
 *
 * name detail:
 * http://services.tropicos.org/Name/25509881?apikey=34a6225b-552c-4e0b-9937-fe12a2541176&format=xml
 *
 * synonyms:
 * http://services.tropicos.org/Name/25509881/Synonyms?apikey=34a6225b-552c-4e0b-9937-fe12a2541176&format=xml
 *
 * accepted names:
 * http://services.tropicos.org/Name/25503923/AcceptedNames?apikey=34a6225b-552c-4e0b-9937-fe12a2541176&format=xml
 */
public class TropicosFragment extends Fragment implements Button.OnClickListener {
    MapActivity activity;
    View view;
    private Button buscar;
    EditText txtNameId;
    EditText txtName;
    EditText txtCommon;
    EditText txtFamily;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tropicos, container, false);
        activity=(MapActivity)getActivity();
        buscar = (Button) view.findViewById(R.id.btn_buscar);
        buscar.setOnClickListener(this);
        txtNameId = (EditText)view.findViewById(R.id.txt_nameId);
        txtNameId.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            InputMethodManager imm = (InputMethodManager)activity.getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(txtNameId.getWindowToken(), 0);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        txtName = (EditText)view.findViewById(R.id.txt_name);
        txtName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            InputMethodManager imm = (InputMethodManager)activity.getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        txtCommon = (EditText)view.findViewById(R.id.txt_commonname);
        txtCommon.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            InputMethodManager imm = (InputMethodManager)activity.getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(txtCommon.getWindowToken(), 0);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        txtFamily = (EditText)view.findViewById(R.id.txt_family);
        txtFamily.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            InputMethodManager imm = (InputMethodManager)activity.getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(txtFamily.getWindowToken(), 0);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        return view;
    }
    @Override
    public void onClick(View v) {
        System.out.println("click");
        if (v.getId() == buscar.getId()) {
            System.out.println("entro");
            String name = ((EditText) view.findViewById(R.id.txt_name)).getText().toString();
            String nameId = ((EditText) view.findViewById(R.id.txt_nameId)).getText().toString();
            String common = ((EditText) view.findViewById(R.id.txt_commonname)).getText().toString();
            String family = ((EditText) view.findViewById(R.id.txt_family)).getText().toString();
            if(name.trim().length()>0 || nameId.trim().length()>0 || common.trim().length()>0 || family.trim().length()>0){
                ExecutorService queue = Executors.newSingleThreadExecutor();
                queue.execute(new BusquedaLoader(activity,name,nameId,family,common));
            }else{
                 Toast.makeText(activity, getString(R.string.busqueda_no_parametros), Toast.LENGTH_SHORT).show();
            }

        }
    }
}