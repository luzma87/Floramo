package com.lzm.Cajas;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.lzm.Cajas.utils.Utils;

/**
 * Created by luz on 08/10/14.
 */
public class SettingsCommentsFragment extends Fragment implements View.OnClickListener {

    MapActivity context;
    Button btnSend;
    TextView lblVersion;
    TextView lblMarca;

    EditText txtDetalles;

    String androidVersion;
    String deviceInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        View view = inflater.inflate(R.layout.settings_comments_layout, container, false);

        btnSend = (Button) view.findViewById(R.id.comments_send_btn);
        btnSend.setOnClickListener(this);

        lblVersion = (TextView) view.findViewById(R.id.comments_version_txt);
        lblMarca = (TextView) view.findViewById(R.id.comments_device_txt);

        txtDetalles = (EditText) view.findViewById(R.id.comments_descripcion_txt);

        String myVersion = Build.VERSION.RELEASE; // e.g. myVersion := "1.6"
        int sdkVersion = Build.VERSION.SDK_INT; // e.g. sdkVersion := 8;
        androidVersion = "Android " + myVersion + ", SDK " + sdkVersion;

        lblVersion.setText(androidVersion);

        deviceInfo = getDeviceName();
        lblMarca.setText(deviceInfo);

        return view;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String brand = Build.BRAND;
        if (model.startsWith(manufacturer)) {
            return capitalize(model) + " (" + brand + " " + product + ")";
        } else {
            return capitalize(manufacturer) + " " + model + " (" + brand + " " + product + ")";
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void alerta(String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        Utils.hideSoftKeyboard(context);
        if (view.getId() == btnSend.getId()) {

            String detalles = txtDetalles.getText().toString().trim();
            if (detalles.equals("")) {
                alerta(getString(R.string.comments_descripcion_error));
            } else {

                String direccion = getString(R.string.comments_mail);
                String subject = getString(R.string.comments_subject);
                String txt = androidVersion + "\n" + deviceInfo + "\n\n" + detalles;

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", direccion, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, txt);
                startActivity(Intent.createChooser(emailIntent, getString(R.string.comments_activity_title)));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        context.setTitle(R.string.settings_title);
        context.mDrawerList.setItemChecked(context.SETTINGS_POS, true);
    }
}