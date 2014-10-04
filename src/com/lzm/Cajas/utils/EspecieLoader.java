package com.lzm.Cajas.utils;

import android.graphics.Bitmap;
import com.google.android.gms.maps.model.LatLng;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.db.Coordenada;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.db.Foto;
import com.lzm.Cajas.image.EspecieUi;
import com.lzm.Cajas.image.ImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Svt on 10/4/2014.
 */
public class EspecieLoader implements Runnable {
    private MapActivity context;
    private Especie especie;
    private List<Foto> fotos;
    public EspecieLoader(MapActivity context,Especie especie){
        this.context=context;
        this.especie=especie;
    }
    @Override
    public void run() {
        fotos=Foto.findAllByEspecie(context,especie);
        boolean vert = false;
        int resId=0;
        InputStream io=null;
        String nombre = especie.getNombreCientifico();
        EspecieUi ui;
        for (Foto foto : fotos) {
            Coordenada cord = foto.getCoordenada(context);
            if(cord!=null){
                Bitmap myBitmap;
                if (foto.esMia == 1) {
                    File imgFile = new File(foto.path);
                    if (imgFile.exists()) {
                        myBitmap = ImageUtils.decodeFile(imgFile.getAbsolutePath(), 160, 90);
                        try {
                            io = new FileInputStream(new File(imgFile.getAbsolutePath()));
                        }catch (Exception e){
                            e.printStackTrace();
                            return;
                        }
                        int w = myBitmap.getWidth();
                        int h = myBitmap.getHeight();


                    }else{
                        myBitmap = null;
                        return;
                    }
                } else {
                    String path1 = foto.path.replaceAll("\\.jpg", "").replaceAll("-", "_").toLowerCase();
                    path1 = "th_" + path1;
                    resId = Utils.getImageResourceByName(context, path1);
                    io = context.getResources().openRawResource(resId);
                    path1 = foto.path.replaceAll("\\.jpg", "").replaceAll("-", "_").toLowerCase();
                    resId=Utils.getImageResourceByName(context, path1);
                    myBitmap = ImageUtils.decodeBitmap(io,160,90);
                }
                System.out.println("Especie "+especie.nombre+" foto "+foto.path+"  coord "+cord);
                final LatLng pos = new LatLng(cord.latitud, cord.longitud);
                ui = new EspecieUi(nombre,resId,especie.idTropicos.toString());
                context.setPingEspecie(ui,pos,myBitmap);

            }


        }
    }
}
