package com.lzm.Cajas;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.lzm.Cajas.adapters.RutasListAdapter;
import com.lzm.Cajas.db.Nota;
import com.lzm.Cajas.db.Ruta;


import java.util.List;

/**
 * Created by Svt on 8/15/2014.
 */
public class RutasFragment extends ListFragment {
    MapActivity activity;
    List<Ruta> list;
    String dialogTitle = "";
    RutasListAdapter adapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MapActivity) getActivity();

        setEmptyText(getResources().getString(R.string.list_no_routes));
        TextView emptyTextView = (TextView) getListView().getEmptyView();
        emptyTextView.setTextColor(getResources().getColor(R.color.list_no_results));

        list = Ruta.list(activity);
        adapter = new RutasListAdapter(getActivity(), list);
        setListAdapter(adapter);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
                final int positionToRemove = position;

                Vibrator v1 = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v1.vibrate(100);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.rutas_dlg_delete_contenido)
                        .setTitle(R.string.rutas_dlg_delete_title);

                // Add the buttons
                builder.setPositiveButton(R.string.global_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Ruta selected = list.get(positionToRemove);
                        Ruta.delete(activity, selected);
                        list.remove(positionToRemove);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(getActivity(), getString(R.string.rutas_deleted), Toast.LENGTH_LONG).show();
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
                return true;
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Ruta selected = list.get(position);
        activity.openRutaFragment(selected);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle(R.string.rutas_title);
        activity.mDrawerList.setItemChecked(activity.RUTAS_POS, true);
    }

}
