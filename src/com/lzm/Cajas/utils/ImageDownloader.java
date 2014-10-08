package com.lzm.Cajas.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.R;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Svt on 10/7/2014.
 */
public class ImageDownloader implements Runnable {
    private MapActivity context;
    SearchResult current;
    ImageView img;
    ProgressDialog dialog;

    public ImageDownloader(MapActivity context, SearchResult current, ImageView img,ProgressDialog dialog) {
        this.context = context;
        this.current = current;
        this.img = img;
        this.dialog = dialog;
    }

    @Override
    public void run() {
        String urlstr = "http://services.tropicos.org/Name/";
        urlstr+=current.nameId+"/Images?apikey=34a6225b-552c-4e0b-9937-fe12a2541176&format=json";
        HttpClient Client = new DefaultHttpClient();

        // Create URL string
        //Log.i("httpget", URL);


        String SetServerString = "";

        // Create Request to server and get response
        System.out.println("url  "+urlstr);
        try {
            HttpGet httpget = new HttpGet(urlstr);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            SetServerString = Client.execute(httpget, responseHandler);
            System.out.println("response "+SetServerString);

            JSONArray arr = new JSONArray(SetServerString);
            if(arr.length()>0) {
                current.fotos = new ArrayList<String>();
                current.pos=0;
            }
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                //System.out.println("obj "+obj);
               // System.out.println("image id "+obj.getString("ImageId"));
                String imageId =  obj.getString("ImageId");
                current.fotos.add(imageId);
                if(i==0){
                    ExecutorService queue = Executors.newSingleThreadExecutor();
                    queue.execute(new FotoDownloader((MapActivity)context, current,  img,  obj.getString("ImageId"), dialog));
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
            context.showError(dialog,context.getString(R.string.search_no_results));
        }

    }
}
