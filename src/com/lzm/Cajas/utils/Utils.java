package com.lzm.Cajas.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.R;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by DELL on 30/07/2014.
 */
public class Utils {

    public static final double FACTOR_W = 6.75;
    public static final double FACTOR_H = 21.33;
    /*img mapa width, img mapa heigth, margen para el mapa w y h, width de las fotos en los fragments de show*/
    public static final int[] SIZE_1080 = {160, 90, 10, 36, 400, 224};
    public static final int[] SIZE_480 = {76, 40, 9, 18, 150, 84};
    public static final Map<Integer, int[]> size;

    static {
        size = new HashMap<Integer, int[]>();
        size.put(480, SIZE_480);
        size.put(1080, SIZE_1080);


    }

    public static int[] getSize(int sw) {
        int[] res;
        res = (int[]) size.get(sw);
//        System.out.println("get de !" + sw + "!  " + size.get(sw));
        if (res != null) {
            return res;
        } else {
            Iterator it = size.entrySet().iterator();
            boolean band = false;
            int[] last = null;
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
//                System.out.println(pairs.getKey() + " = " + (int[]) pairs.getValue());
                int t = Integer.parseInt(pairs.getKey().toString());
                if (sw < t) {
                    if (last != null) {
                        res = last;
                    } else {
                        res = (int[]) pairs.getValue();
                    }
                    break;
                }
                last = (int[]) pairs.getValue();
                it.remove(); // avoids a ConcurrentModificationException

            }
            return res;
        }
    }

    public static void openFragment(MapActivity context, Fragment fragment, String title) {
        openFragment(context, fragment, title, null);
    }

    public static void openFragment(MapActivity context, Fragment fragment, String title, Bundle args) {
        context.setTitle(title);
        FragmentManager fragmentManager = context.getFragmentManager();
        RelativeLayout mainLayout = (RelativeLayout) context.findViewById(R.id.rl2);
        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .hide(fragmentManager.findFragmentById(R.id.content_frame))
                    .addToBackStack("")
                    .commit();
            mainLayout.setVisibility(View.VISIBLE);
        } else {
            if (args != null) {
                fragment.setArguments(args);
            }
//                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack("")
                    .commit();
            mainLayout.setVisibility(LinearLayout.GONE);
        }
    }

    public static String getStringResourceByName(Context c, String aString) {
        String packageName = c.getPackageName();
        int resId = c.getResources().getIdentifier(aString, "string", packageName);
        if (resId == 0) {
            return aString;
        } else {
            return c.getString(resId);
        }
    }

    public static String getPluralResourceByName(Context c, String aString, int quantity, String param1) {
        String packageName = c.getPackageName();
        int resId = c.getResources().getIdentifier(aString, "plurals", packageName);
        if (resId == 0) {
            return aString;
        } else {
            return c.getResources().getQuantityString(resId, quantity, param1);
        }
    }

    public static int getImageResourceByName(Context c, String aString) {
        String packageName = c.getPackageName();
        int resId = c.getResources().getIdentifier(aString, "drawable", packageName);
        return resId;
    }

    public static String getFolder(Context context) {
        String pathFolder;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            pathFolder = Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.db_name);
        } else {
            /* save the folder in internal memory of phone */
            pathFolder = "/data/data/" + context.getPackageName() + File.separator + context.getString(R.string.db_name);
        }
        File folder = new File(pathFolder);
        folder.mkdirs();
        return pathFolder;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
