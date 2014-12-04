package com.lzm.Cajas.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.R;

/**
 * Created by luz on 04/12/14.
 */
public class ClickableLinkSpan extends ClickableSpan { //clickable span

    String spanTipo;
    String spanUrl;
    MapActivity context;

    public ClickableLinkSpan(MapActivity context, String spanTipo, String spanUrl) {
        this.context = context;
        this.spanTipo = spanTipo;
        this.spanUrl = spanUrl;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (spanTipo.equals("info")) {
            ds.setColor(context.getResources().getColor(R.color.floramo_claro));
        } else if (spanTipo.equals("mailto")) {
            ds.setColor(context.getResources().getColor(R.color.floramo_medio));
        }
    }

    public void onClick(final View textView) {
        // TODO add check if widget instanceof TextView
        TextView tv = (TextView) textView;
        // TODO add check if tv.getText() instanceof Spanned
        Spanned s = (Spanned) tv.getText();
        int start = s.getSpanStart(this);
        int end = s.getSpanEnd(this);
        String text = s.subSequence(start, end).toString();

        if (spanTipo.equals("info")) {
            int res = -1;
            if (spanUrl.equals("rosette")) {
                res = R.drawable.about_def_roseta;
            } else if (spanUrl.equals("cushion")) {
                res = R.drawable.about_def_almohadilla;
            } else if (spanUrl.equals("leaves")) {
                res = R.drawable.about_def_hojas;
            } else if (spanUrl.equals("hairs")) {
                res = R.drawable.about_def_pelos;
            }
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(R.layout.settings_about_definition_dlg, null);

            ImageView imgAbout = (ImageView) v.findViewById(R.id.about_definition_img);
            imgAbout.setImageResource(res);

            final AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(v)
                    .setNeutralButton(R.string.dialog_btn_cerrar, null) //Set to null. We override the onclick
                    .setTitle(text);

            final AlertDialog d = builder.create();

            d.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button cerrar = d.getButton(AlertDialog.BUTTON_NEUTRAL);
                    cerrar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d.dismiss();
                        }
                    });
                }
            });
            d.show();
        } else if (spanTipo.equals("link")) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(spanUrl));
            context.startActivity(myIntent);
        } else if (spanTipo.equals("mailto")) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", spanUrl, null));
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.about_contact)));
        }
    }
}