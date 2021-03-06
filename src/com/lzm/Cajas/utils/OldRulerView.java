package com.lzm.Cajas.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.lzm.Cajas.R;

/**
 * Created by luz on 28/11/14.
 */
public class OldRulerView extends View {
    private SharedPreferences pref;

    public OldRulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int i = 0, x, y;
        int offset = 50;
        int maxX = getWidth(), maxY = getHeight();

        String s;
        Paint paint = new Paint();

        pref = getContext().getSharedPreferences("Ruler", Context.MODE_PRIVATE);

        // Ticks and Numbers
        paint.setColor(getResources().getColor(R.color.white));
        paint.setAntiAlias(true);
        paint.setTextSize(12);

        // X axis
        int dpm = (int) pref.getFloat("xdpm", (float) 1.0);
        do {
            x = i * dpm + offset;
            if (i % 10 == 0) {
                s = Integer.toString(i / 10);
                canvas.drawText(s, x - (int) (paint.measureText(s) / 2),
                        offset - 20, paint);

                paint.setStrokeWidth(2);
                canvas.drawLine(x, 0, x, 15, paint);
                canvas.drawLine(x, offset - 15, x, offset, paint);
            } else if (i % 5 == 0) {
                paint.setStrokeWidth(1);
                canvas.drawLine(x, offset - 15, x, offset, paint);
                canvas.drawLine(x, 0, x, 15, paint);
            } else {
                paint.setStrokeWidth(1);
                canvas.drawLine(x, offset - 10, x, offset, paint);
                canvas.drawLine(x, 0, x, 10, paint);
            }
            i++;
        } while (x < maxX);

        // Y axis
        dpm = (int) pref.getFloat("ydpm", (float) 1.0);
        i = 0;
        do {
            y = i * dpm + offset;
            if (i % 10 == 0) {
                s = Integer.toString(i / 10);
                canvas.drawText(s, offset - 20 - (int) paint.measureText(s),
                        y + 2, paint);

                paint.setStrokeWidth(2);
                canvas.drawLine(offset - 15, y, offset, y, paint);
                canvas.drawLine(0, y, 15, y, paint);
            } else if (i % 5 == 0) {
                paint.setStrokeWidth(1);
                canvas.drawLine(offset - 15, y, offset, y, paint);
                canvas.drawLine(0, y, 15, y, paint);
            } else {
                paint.setStrokeWidth(1);
                canvas.drawLine(offset - 10, y, offset, y, paint);
                canvas.drawLine(0, y, 10, y, paint);
            }
            i++;
        } while (y < maxY);

        // Empty rectangle
        Rect rect = new Rect(offset, offset, getWidth(), getHeight());
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.black));
        canvas.drawRect(rect, paint);
    }
}