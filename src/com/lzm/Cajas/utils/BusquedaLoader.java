package com.lzm.Cajas.utils;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import com.lzm.Cajas.MapActivity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Svt on 10/6/2014.
 */
public class BusquedaLoader implements Runnable {
    String name;
    String nameId;
    String common;
    String family;
    private MapActivity context;
    ProgressDialog dialog;

    public BusquedaLoader(MapActivity context, String name, String nameId, String family, String common, ProgressDialog dialog) {
        this.name = name;
        this.nameId = nameId;
        this.family = family;
        this.common = common;
        this.context = context;
        this.dialog = dialog;
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
            if (!name.trim().equals("")) {
                parameters += "name=" + name.trim();
            }
            if (!nameId.trim().equals("")) {
                if (!parameters.equals(""))
                    parameters += "&";
                parameters += "nameid=" + nameId.trim() + "&type=exact";
            }
            if (!common.trim().equals("")) {
                if (!parameters.equals(""))
                    parameters += "&";
                parameters += "commonname=" + common.trim();
            }
            if (!family.trim().equals("")) {
                if (!parameters.equals(""))
                    parameters += "&";
                parameters += "family=" + family.trim();
            }
            if (!parameters.equals(""))
                parameters += "&";
            parameters += "apikey=" + key + "&format=json&pagesize=10";
            urlstr += "?" + parameters;

            // Create http cliient object to send request to server
            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            //Log.i("httpget", URL);


            String SetServerString = "";

            // Create Request to server and get response
//            System.out.println("url  "+urlstr);
            HttpGet httpget = new HttpGet(urlstr);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            SetServerString = Client.execute(httpget, responseHandler);
//            System.out.println("response "+SetServerString);
            List<SearchResult> result;
            try {
                JSONArray arr = new JSONArray(SetServerString);
                result = new ArrayList<SearchResult>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    SearchResult curent = new SearchResult(obj.getString("NameId"), obj.getString("ScientificName"), obj.getString("ScientificNameWithAuthors"), obj.getString("Family"), obj.getString("RankAbbreviation"), obj.getString("Author"), obj.getString("DisplayReference"), obj.getString("DisplayDate"));
                    result.add(curent);
                }
                context.showSearchResults(result, dialog);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
//            System.out.println("error busqueda " + e);
            for (StackTraceElement ste : e.getStackTrace()) {
//                System.out.println(ste);
            }
        }
    }
}
