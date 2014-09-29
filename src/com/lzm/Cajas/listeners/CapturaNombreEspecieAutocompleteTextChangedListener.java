package com.lzm.Cajas.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import com.lzm.Cajas.CapturaFragment;
import com.lzm.Cajas.MainActivity;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.R;
import com.lzm.Cajas.adapters.CapturaNombreEspecieArrayAdapter;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.db.Genero;

import java.util.List;

/**
 * Created by luz on 04/08/14.
 */
public class CapturaNombreEspecieAutocompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CapturaNombreFamiliaAutocompleteTextChangedListener.java";
    MapActivity context;
    CapturaFragment fragment;
    Genero genero;

    public CapturaNombreEspecieAutocompleteTextChangedListener(MapActivity context, CapturaFragment fragment, Genero genero) {
        this.context = context;
        this.fragment = fragment;
        this.genero = genero;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        try {

            // if you want to see in the logcat what the user types
//            Log.e(TAG, "User input: " + userInput);

            // update the adapater
            fragment.nombreEspecieArrayAdapter.notifyDataSetChanged();

            // get suggestions from the database
            List<Especie> myObjs = Especie.findAllByGeneroAndNombreLike(context, genero, userInput.toString());

            // update the nombreComunArrayAdapter
            fragment.nombreEspecieArrayAdapter = new CapturaNombreEspecieArrayAdapter(context, R.layout.captura_autocomplete_list_item, myObjs);
            fragment.autocompleteEspecie.setAdapter(fragment.nombreEspecieArrayAdapter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}