package com.lzm.Cajas.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import com.lzm.Cajas.CapturaFragment;
import com.lzm.Cajas.MainActivity;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.R;
import com.lzm.Cajas.adapters.CapturaNombreComunArrayAdapter;
import com.lzm.Cajas.db.Especie;

import java.util.List;

/**
 * Created by luz on 04/08/14.
 */
public class CapturaNombreComunAutocompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CapturaNombreComunAutocompleteTextChangedListener.java";
    MapActivity context;
    CapturaFragment fragment;

    public CapturaNombreComunAutocompleteTextChangedListener(MapActivity context, CapturaFragment fragment) {
        this.context = context;
        this.fragment = fragment;
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
            fragment.nombreComunArrayAdapter.notifyDataSetChanged();

            // get suggestions from the database
            List<Especie> myObjs = Especie.findAllByNombreComunLike(context, userInput.toString());

            // update the nombreComunArrayAdapter
            fragment.nombreComunArrayAdapter = new CapturaNombreComunArrayAdapter(context, R.layout.captura_autocomplete_list_item, myObjs);

            fragment.autocompleteNombreComun.setAdapter(fragment.nombreComunArrayAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}