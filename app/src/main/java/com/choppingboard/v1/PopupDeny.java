package com.choppingboard.v1;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

/**
 * Created by Jeff on 7/8/15.
 */
public class PopupDeny extends android.widget.PopupWindow {

    Context ctx;
    View popupView;
    Button one;
    Button two;
    Button three;
    Button four;
    JSONObject o;

    public PopupDeny(Context context, JSONObject o) {
        super(context);
        ctx = context;
        this.o = o;
        popupView = LayoutInflater.from(context).inflate(R.layout.popupdeny, null);
        setContentView(popupView);

        //sets the size of the popup
        setHeight(850);
        setWidth(550);

    }

    // Attaches the view to its parent anchor-view at position x and y
    public void show(View anchor, int x, int y)
    {
        showAtLocation(anchor, Gravity.CENTER, x, y);
    }
}
