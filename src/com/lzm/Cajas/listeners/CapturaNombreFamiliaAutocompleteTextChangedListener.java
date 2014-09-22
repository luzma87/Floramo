package com.lzm.Cajas.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import com.lzm.Cajas.CapturaFragment;
import com.lzm.Cajas.MainActivity;
import com.lzm.Cajas.R;
import com.lzm.Cajas.adapters.CapturaNombreFamiliaArrayAdapter;
import com.lzm.Cajas.db.Familia;

import java.util.List;

/**
 * Created by luz on 04/08/14.
 */
public class CapturaNombreFamiliaAutocompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CapturaNombreFamiliaAutocompleteTextChangedListener.java";
    MainActivity context;
    CapturaFragment fragment;

    public CapturaNombreFamiliaAutocompleteTextChangedListener(MainActivity context, CapturaFragment fragment) {
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
            fragment.nombreFamiliaArrayAdapter.notifyDataSetChanged();

            // get suggestions from the database
            List<Familia> myObjs = Familia.findAllByNombreLike(context, userInput.toString());
            // update the nombreComunArrayAdapter
            fragment.nombreFamiliaArrayAdapter = new CapturaNombreFamiliaArrayAdapter(context, R.layout.captura_autocomplete_list_item, myObjs);
            fragment.autocompleteFamilia.setAdapter(fragment.nombreFamiliaArrayAdapter);
            if (myObjs.size() == 1) {
                fragment.autocompleteGenero.addTextChangedListener(new CapturaNombreGeneroAutocompleteTextChangedListener(context, fragment, myObjs.get(0)));
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}