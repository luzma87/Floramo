package com.lzm.Cajas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.db.Settings;
import com.lzm.Cajas.image.EspecieUi;
import com.lzm.Cajas.utils.EspecieDialogImageLoader;
import com.lzm.Cajas.utils.FotoDownloader;
import com.lzm.Cajas.utils.ImageDownloader;
import com.lzm.Cajas.utils.SearchResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Svt on 10/7/2014.
 */
public class ResultInfoFragment  extends Fragment implements Button.OnClickListener, View.OnTouchListener  {
    MapActivity context;
    SearchResult current;
    Button web;
    Button imagenes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        current = context.result.get(context.posSearch);
        View view = inflater.inflate(R.layout.search_result_info, container, false);
        ((TextView)view.findViewById(R.id.especie_info_nombre_cientifico)).setText(current.scientificName);
        ((TextView)view.findViewById(R.id.especie_info_familia)).setText(current.family);
        ((TextView)view.findViewById(R.id.txt_autor)).setText(current.author);
        ((TextView)view.findViewById(R.id.txt_rank)).setText(current.rankAbbreviation);
        ((TextView)view.findViewById(R.id.txt_display_ref)).setText(current.displayReference);
        ((TextView)view.findViewById(R.id.txt_display_date)).setText(current.displayDate);
        web = (Button) view.findViewById(R.id.web);
        imagenes = (Button) view.findViewById(R.id.ver_fotos);
        web.setOnClickListener(this);
        imagenes.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        final Settings sett = Settings.getSettings(context);
        if (v.getId() == web.getId()) {
            String url = sett.tropicosBase + current.nameId;
            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (v.getId() == imagenes.getId()) {
            LayoutInflater inflater = context.getLayoutInflater();
            View myView = inflater.inflate(R.layout.dialog, null);
            final ImageView img = (ImageView) myView.findViewById(R.id.image);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Foto");
            builder.setView(myView);
            builder.setNegativeButton(R.string.dialog_btn_cerrar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }


            });
            /*builder.setPositiveButton(R.string.dialog_btn_siguiente, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   if(current.fotos!=null){
                       if(current.fotos.size()-1>current.pos){
                           ProgressDialog dialogLoading = ProgressDialog.show(context, "",getString(R.string.loading), true);
                           current.pos++;
                           ExecutorService queue = Executors.newSingleThreadExecutor();
                           queue.execute(new FotoDownloader((MapActivity)context, current,  img,  current.fotos.get(current.pos), dialogLoading));
                       }
                   }
                }
            });*/
            builder.setPositiveButton(R.string.dialog_btn_siguiente,null);
            context.dialog = builder.create();
            context.dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    Button b = context.dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            if(current.fotos!=null){
                                if(current.fotos.size()-1>current.pos){
                                    img.setImageBitmap(null);
                                    ProgressDialog dialogLoading = ProgressDialog.show(context, "",getString(R.string.loading), true);
                                    current.pos++;
                                    ExecutorService queue = Executors.newSingleThreadExecutor();
                                    queue.execute(new FotoDownloader((MapActivity)context, current,  img,  current.fotos.get(current.pos), dialogLoading));
                                    return;
                                }
                            }
                        }
                    });
                }
            });
            context.dialog.show();

            ProgressDialog dialogLoading = ProgressDialog.show(context, "",getString(R.string.loading), true);
            ExecutorService queue = Executors.newSingleThreadExecutor();
            queue.execute(new ImageDownloader((MapActivity) context,current,img,dialogLoading));

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
