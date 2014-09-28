package com.lzm.Cajas;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.lzm.Cajas.adapters.BusquedaResultsEspeciesListAdapter;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.db.Foto;
import com.lzm.Cajas.utils.Utils;

import java.util.List;

/**
 * Created by DELL on 16/08/2014.
 */
public class BusquedaResultsFragment extends ListFragment {

    MapActivity activity;
    List<Especie> especiesList;

    int fotoPos = 0;
    String dialogTitle = "";
    List<Foto> fotos;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MapActivity) getActivity();

//        especiesList = activity.especiesBusqueda;

//        for (Foto foto : fotoList) {
//            System.out.println("--------------------------- " + foto.id);
//        }

        BusquedaResultsEspeciesListAdapter adapter = new BusquedaResultsEspeciesListAdapter(getActivity(), especiesList);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Especie selected = especiesList.get(position);
        Fragment fragment = new EspecieInfoFragment();
        Bundle args = new Bundle();
        args.putLong("especie", selected.id);
//        fragment.setArguments(args);

        String nombre = selected.getNombreCientifico() + " (" + selected.nombreComun + ")";

        Utils.openFragment(activity, fragment, nombre, args);

//        activity.setTitle(nombre);
//
//        FragmentManager fragmentManager = activity.getFragmentManager();
////        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//        fragmentManager.beginTransaction()
//                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
//                .replace(R.id.content_frame, fragment)
//                .addToBackStack("")
//                .commit();

//        Entry selectedEntry = entryList.get(position);
//        fotos = Foto.findAllByEntry(activity, selectedEntry);
//
//        LayoutInflater inflater = activity.getLayoutInflater();
//        View myView = inflater.inflate(R.layout.encyclopedia_entries_dialog, null);
//
//        final ImageView img = (ImageView) myView.findViewById(R.id.encyclopedia_entries_foto);
//        final TextView com = (TextView) myView.findViewById(R.id.encyclopedia_entries_comentarios);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle(R.string.encyclopedia_entries_dialog_title);
//        builder.setView(myView);
//
//        builder.setNegativeButton(R.string.dialog_btn_cerrar, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//            }
//        });
//
//        if (fotos.size() > 1) {
//            builder.setNeutralButton(R.string.dialog_btn_anterior, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    if (fotoPos > 0) {
//                        fotoPos -= 1;
//                        setFoto(img);
//                    }
//                }
//            });
//
//            builder.setPositiveButton(R.string.dialog_btn_siguiente, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int id) {
//                    if (fotoPos < fotos.size() - 1) {
//                        fotoPos += 1;
//                        setFoto(img);
//                    }
//                }
//            });
//        }
//        String comentarios = selectedEntry.comentarios;
////        while (comentarios.length() < 150) {
////            comentarios += " Lorem ipsum dolor sit amet ";
////        }
//        AlertDialog dialog = builder.create();
//        setFoto(img);
//        com.setText(comentarios);
//        dialog.show();
    }
    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle(R.string.busqueda_title);
    }
}