package com.choppingboard.v1;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeff on 6/29/15.
 */
public class PopupWindow extends android.widget.PopupWindow
{
    Context ctx;
    Button btnDismiss;
    View popupView;
    TextView orderid;
    TextView ordertime;
    TextView name;
    TextView add;
    TextView custnum;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    public PopupWindow(Context context, JSONObject o)
    {
        super(context);

        ctx = context;
        popupView = LayoutInflater.from(context).inflate(R.layout.popup, null);
        setContentView(popupView);

        btnDismiss = (Button)popupView.findViewById(R.id.btn_dismiss);
        orderid = (TextView)popupView.findViewById(R.id.orderid);
        ordertime = (TextView)popupView.findViewById(R.id.ordertime);
        name = (TextView)popupView.findViewById(R.id.name);
        add = (TextView)popupView.findViewById(R.id.add);
        custnum = (TextView)popupView.findViewById(R.id.custnum);

        try{
            orderid.setText("Order " + o.getString("id"));
            ordertime.setText("Order time: " +o.getString("reqtime"));
            name.setText("Name: " + o.getString("custName"));
            add.setText("Address: " + o.getString("custAdd"));
            custnum.setText("Number: " + o.getString("custNum"));
        }catch (JSONException e){
            e.printStackTrace();
        }



        setHeight(1600);
        setWidth(800);

        // Closes the popup window when touch outside of it - when looses focus
        setOutsideTouchable(true);
        setFocusable(true);

        // Removes default black background
//        setBackgroundDrawable(new BitmapDrawable());

        btnDismiss.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                dismiss();
            }});

        // Closes the popup window when touch it
     this.setTouchInterceptor(new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    x2 = event.getX();
                    float deltaX = x2 - x1;

                    if (Math.abs(deltaX) > MIN_DISTANCE)
                    {
                        // Left to Right swipe action
                        if (x2 > x1)
                        {
                            Toast.makeText(ctx, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();
                        }

                        // Right to left swipe action
                        else
                        {
                            Toast.makeText(ctx, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
                        }

                    }
                    else
                    {
                        // consider as something else - a screen tap for example
                        dismiss();
                    }
                    break;
            }
            return true;
        }
    });
    } // End constructor

    // Attaches the view to its parent anchor-view at position x and y
    public void show(View anchor, int x, int y)
    {
        showAtLocation(anchor, Gravity.CENTER, x, y);
    }
}