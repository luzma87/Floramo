package com.lzm.Cajas.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.db.Foto;
import com.lzm.Cajas.image.ImageUtils;

import java.io.InputStream;

/**
 * Created by Svt on 10/4/2014.
 */
public class EspecieDialogImageLoader implements Runnable {
    private MapActivity context;
    int resId;
    ImageView img;
    Bitmap imagen;

    public EspecieDialogImageLoader(MapActivity context,int resId,ImageView img){
        this.context=context;
        this.resId=resId;
        this.img=img;
    }

    @Override
    public void run() {
        InputStream io;
        io = context.getResources().openRawResource(resId);
        imagen = ImageUtils.decodeBitmap(io, 700, 400);
        context.setImgDialog(imagen,img);

    }
}
