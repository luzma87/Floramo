package com.lzm.Cajas.utils;

import android.graphics.Bitmap;
import com.lzm.Cajas.MapActivity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Svt on 10/6/2014.
 */
public class BusquedaLoader implements Runnable {
    String name ;
    String nameId;
    String common ;
    String family;
    private MapActivity context;

    public BusquedaLoader(MapActivity context ,String name, String nameId, String family, String common) {
        this.name = name;
        this.nameId = nameId;
        this.family = family;
        this.common = common;
        this.context = context;
    }

    @Override
    public void run() {
        String urlstr = "http://services.tropicos.org/Name/Search";
        String key = "34a6225b-552c-4e0b-9937-fe12a2541176";

        try {
            DataOutputStream dos = null;
            HttpURLConnection conn = null;



            int serverResponseCode = 0;
            // Name/Search?nameid={nameid}&name={name}&commonname={commonname}&orderby={orderby}&sortorder={sortorder}&pagesize={pagesize}&startrow={startrow}&type={type}&apikey={apikey}&format={format}
            String parameters = "";
            if(!name.trim().equals("")){
                parameters+="name="+name.trim();
            }
            if(!nameId.trim().equals("")){
                if(!parameters.equals(""))
                    parameters+="&";
                parameters+="nameid="+nameId.trim()+"&type=exact";
            }
            if(!common.trim().equals("")){
                if(!parameters.equals(""))
                    parameters+="&";
                parameters+="commonname="+common.trim();
            }
            if(!family.trim().equals("")){
                if(!parameters.equals(""))
                    parameters+="&";
                parameters+="family="+family.trim();
            }
            if(!parameters.equals(""))
                parameters+="&";
            parameters+="apikey="+key+"&format=json&pagesize=10";
            urlstr+="?"+parameters;

            // Create http cliient object to send request to server

            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            //Log.i("httpget", URL);


            String SetServerString = "";

            // Create Request to server and get response
            System.out.println("url  "+urlstr);
            HttpGet httpget = new HttpGet(urlstr);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            SetServerString = Client.execute(httpget, responseHandler);
            System.out.println("response "+SetServerString);
            // Show response on activity





           /* URL url = new URL(urlstr);
            System.out.println("url  "+urlstr);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            dos = new DataOutputStream(
                    conn.getOutputStream());
            dos.writeBytes(parameters);
            dos.flush();
            dos.close();

            //Get Response
            InputStream is = conn.getInputStream();
            serverResponseCode = conn.getResponseCode();

            System.out.println("response code "+serverResponseCode);
            if (serverResponseCode == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                System.out.println("Resultado "+response.toString());
            }
            dos.flush();
            dos.close();
            */

        } catch (Exception e) {
            System.out.println("error busqueda " + e);
            for (StackTraceElement ste : e.getStackTrace()) {
                System.out.println(ste);
            }
        }
    }
}
