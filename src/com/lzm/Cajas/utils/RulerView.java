package com.lzm.Cajas.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.lzm.Cajas.R;

/**
 * Created by luz on 28/11/14.
 */
public class RulerView extends View {
    Paint paint;
    SharedPreferences pref;
    int dens;
    int fullLength;
    int halfLength;
    int quartLength;
    int offsetX;
    int colorCm = Color.BLUE;
    int colorIn = Color.BLACK;


    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFocusable(true);
        paint = new Paint();
        pref = getContext().getSharedPreferences("Ruler", Context.MODE_PRIVATE);
        dens = (int) pref.getFloat("density", (float) 1.0);

        fullLength = 15 * dens;
        halfLength = 8 * dens;
        quartLength = 5 * dens;

        offsetX = 60 * dens;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int maxY = getHeight();
        int i = 0;
        int yCm, yIn;
        int offsetY = 15 * dens;

        // Ticks and Numbers
        paint.setColor(getResources().getColor(R.color.black));
        paint.setAntiAlias(true);
        paint.setTextSize(12 * dens);

        // Y axis
        do {
            double yCmTemp = (i * ((pref.getFloat("ydpm", (float) 1.0) / 25.4) * 5) + offsetY);
            yCmTemp = yCmTemp + (i * dens);

            double yInTemp = (i * (pref.getFloat("ydpm", (float) 1.0) / 4)) + offsetY;
            yInTemp = yInTemp + (i * dens);

            yCm = (int) Math.ceil(yCmTemp);
            yIn = (int) Math.ceil(yInTemp);

            if (i % 4 == 0) {
                //pulgada entera
                drawIn(i, yIn, canvas);
                //cm entero
                drawCm(i, yCm, canvas);
            } else if (i % 4 == 1) {
                //cuarto pulgada
                drawIn(i, yIn, canvas);
                //medio cm
                drawCm(i, yCm, canvas);
            } else if (i % 4 == 2) {
                //media pulgada
                drawIn(i, yIn, canvas);
                //cm entero
                drawCm(i, yCm, canvas);
            } else if (i % 4 == 3) {
                //cuarto pulgada
                drawIn(i, yIn, canvas);
                //medio cm
                drawCm(i, yCm, canvas);
            }
            i++;
        } while (yCm < maxY);
    }

    private void drawCm(int i, int yCm, Canvas canvas) {
        int l = i % 2 == 0 ? fullLength : halfLength;
        paint.setColor(colorCm);

        if (i % 2 == 0) {
            String s = "" + ((int) (i / 2));
            canvas.drawText(s, l + (5 * dens), yCm + (2 * dens), paint);
        }

        paint.setStrokeWidth(dens);
        canvas.drawLine(0, yCm, l, yCm, paint);
    }

    private void drawIn(int i, int yIn, Canvas canvas) {
        int l = i % 4 == 0 ? quartLength : i % 4 == 2 ? halfLength : fullLength;
        paint.setColor(colorIn);
        if (i % 4 == 0) {
            String s = "" + ((int) (i / 4));
            canvas.drawText(s, offsetX + l - (15 * dens), yIn + (2 * dens), paint);
        }

        paint.setStrokeWidth(dens);
        canvas.drawLine(offsetX + l, yIn, offsetX * 2, yIn, paint);

    }

}