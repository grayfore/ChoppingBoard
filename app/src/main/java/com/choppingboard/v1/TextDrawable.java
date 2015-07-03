package com.choppingboard.v1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Jeff on 7/3/15.
 */
public class TextDrawable extends Drawable {

    private final Paint paint;
    private final Paint thing;
    private final Paint thing2;

    public TextDrawable() {


        this.paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
//        paint.setShadowLayer(6f, 0, 0, Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);

        this.thing = new Paint();
        thing.setColor(Color.GREEN);
        this.thing2 = new Paint();
        thing2.setColor(Color.RED);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect r = new Rect();
        r.set(0, 0,225,850);
        Rect s = new Rect();
        s.set(225,0,550,850);
        canvas.drawRect(r, thing);
        canvas.drawRect(s, thing2);
        canvas.drawText("Accept", 10, 400, paint);
        canvas.drawText("Deny", 460, 400, paint);

    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}