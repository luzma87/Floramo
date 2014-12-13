package com.lzm.Cajas.image;

/**
 * Created by Svt on 7/29/2014.
 */

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import com.lzm.Cajas.MapActivity;

/**
 * Class to observe changes to images table
 *
 * @author Jan Peter Hooiveld
 */
public class ImageTableObserver extends ContentObserver
{
    /**
     * Main application
     */
    private MapActivity application;
    private boolean first = true;

    /**
     *  Queue that handles image uploads
     */


    /**
     * Constructor
     *
     * @param handler Handler for this class
     * @param application Main application

     */
    public ImageTableObserver(Handler handler, MapActivity application)
    {
        super(handler);
        this.application = application;

    }

    /**
     * This function is fired when a change occurs on the image table
     *
     * @param selfChange
     */
    @Override
    public void onChange(boolean selfChange){
        // get latest image id
        if(!first) {
            String[] columns = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.ORIENTATION};
            Cursor cursor = application.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.Media._ID + " DESC");

            // check if table has any rows at all
            if (!cursor.moveToFirst()) {
                return;
            }
            int latestId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            application.setImageIndex(latestId);
        }
        this.first=false;
//        //System.out.println("lastest id "+latestId);

    }


}