package com.android.Effect;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */

public class Blur {

    public Blur(Context context) {
        this.context = context;
    }

    private Context context;
    public static float radius = 5;

    public static float getRadius() {
        return radius;
    }

    public static void setRadius(float radius) {
        Blur.radius = radius;
    }

    public void applyBlur(final View viewbehind, final View viewabow) {
        viewbehind.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                viewbehind.getViewTreeObserver().removeOnPreDrawListener(this);
                viewbehind.buildDrawingCache();

                Bitmap bmp = viewbehind.getDrawingCache();
                blur(bmp, viewabow);
                return true;
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {


        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
                (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(this.context);

        Allocation overlayAlloc = Allocation.createFromBitmap(
                rs, overlay);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, overlayAlloc.getElement());

        blur.setInput(overlayAlloc);

        blur.setRadius(radius);

        blur.forEach(overlayAlloc);

        overlayAlloc.copyTo(overlay);

        view.setBackground(new BitmapDrawable(
                this.context.getResources(), overlay));

        rs.destroy();
    }
}
