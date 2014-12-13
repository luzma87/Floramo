package com.lzm.Cajas.utils;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.image.ImageUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Svt on 10/7/2014.
 */
public class FotoDownloader  implements Runnable {
    private MapActivity context;
    SearchResult current;
    ImageView img;
    Bitmap imagen;
    ProgressDialog dialog;
    String imageId;
    public FotoDownloader(MapActivity context, SearchResult current, ImageView img, String imagenId, ProgressDialog dialog) {
        this.context = context;
        this.current = current;
        this.img = img;
        this.dialog = dialog;
        this.imageId= imagenId;
    }

    @Override
    public void run() {
        try{
            String url = "http://tropicos.org/ImageDownload.aspx?imageid="+imageId;
//            //System.out.println("url foto "+url);
            //http://tropicos.org/ImageDownload.aspx?imageid=40773
            URL urlFoto = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlFoto.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream io = connection.getInputStream();
            imagen =  imagen = ImageUtils.decodeBitmap(io, 700, 400);
            context.setImgDialogLoader(imagen,img,dialog);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
