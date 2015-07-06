package com.choppingboard.v1;

import android.content.Context;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
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
//    Button btnDismiss;
    View popupView;
    TextView orderid;
    TextView ordertime;
    TextView name;
    TextView add;
    TextView custnum;
    ImageView acceptbar;
    ImageView denybar;
    ScrollView sv;
    TableLayout table;
    private float x1,x2,move,y1,y2,scroll,scrollh;
    static final int MIN_DISTANCE = 180;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    boolean twist = true;

    public PopupWindow(Context context, JSONObject o)
    {
        super(context);

        ctx = context;
        popupView = LayoutInflater.from(context).inflate(R.layout.popup, null);
        setContentView(popupView);
        sv = (ScrollView)popupView.findViewById(R.id.listorderd);
//        btnDismiss = (Button)popupView.findViewById(R.id.btn_dismiss);
        orderid = (TextView)popupView.findViewById(R.id.orderid);
        ordertime = (TextView)popupView.findViewById(R.id.ordertime);
        name = (TextView)popupView.findViewById(R.id.name);
        add = (TextView)popupView.findViewById(R.id.add);
        custnum = (TextView)popupView.findViewById(R.id.custnum);
        table = (TableLayout)popupView.findViewById(R.id.orderItems);

        try{
            orderid.setText("Order " + o.getString("id"));
            ordertime.setText("Order time: " + o.getString("reqtime"));
            name.setText("Name: " + o.getString("custName"));
            add.setText("Address: " + o.getString("custAdd"));
            custnum.setText("Number: " + o.getString("custNum"));

            for(int i = 1; i <= 10; i++) {
                if(o.getString("OrderItem" + i) != null){
                    String orderItem = o.getString("OrderItem" + i);
                    JSONObject orderitem = new JSONObject(orderItem);
                    // Inflate your row "template" and fill out the fields.
                    TableRow row = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.attrib_row, null);
                    ((TextView) row.findViewById(R.id.attrib_name)).setText(orderitem.getString("MenuItem"));
                    ((TextView) row.findViewById(R.id.attrib_value)).setText("$"+orderitem.getString("Price"));
                    table.addView(row);

                    if(!orderitem.getString("Extras").equals("[null]")){
                        TableRow extra = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.extra_row, null);
                        String thing = orderitem.getString("Extras");
                        String[] array = thing.split(",");
                        String stuff = "";
                        for(int j = 0; j < array.length; j++){
                            stuff += (j+1) + ": " + array[j] + "\n";
                        }
                        String newstuff = stuff.replace("[", "").replace("\"", "").replace("]", "");
                        ((TextView) extra.findViewById(R.id.attrib_name)).setText(newstuff);
                        table.addView(extra);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        //this is in a different try-catch because it doesnt work when its in the other one for some reason
        try{
            TableRow deliv = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.total_row, null);
            ((TextView) deliv.findViewById(R.id.attrib_name)).setText("Delivery Charge: ");
            ((TextView) deliv.findViewById(R.id.attrib_value)).setText("$" + o.getString("delivCharge"));

            TableRow tax = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.total_row, null);
            ((TextView) tax.findViewById(R.id.attrib_name)).setText("Tax: ");
            ((TextView) tax.findViewById(R.id.attrib_value)).setText("$" + o.getString("salesTax"));
            tax.setPadding(0,0,0,0);

            TableRow total = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.total_row, null);
            ((TextView) total.findViewById(R.id.attrib_name)).setText("Subtotal: ");
            ((TextView) total.findViewById(R.id.attrib_value)).setText("$" + o.getString("subtotal"));
            total.setPadding(0,0,0,0);

            table.addView(deliv);
            table.addView(tax);
            table.addView(total);
        }catch(JSONException e){
            e.printStackTrace();
        }


        setHeight(850);
        setWidth(550);

        // Closes the popup window when touch outside of it - when looses focus
        setOutsideTouchable(true);
        setFocusable(true);
        setTouchable(true);

            TextDrawable back = new TextDrawable();
            setBackgroundDrawable(back);

//        btnDismiss.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }});

            this.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    scrollh = sv.getScrollY();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x1 = event.getX();
                            y1 = event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            x2 = event.getX();
                            y2 = event.getY();
//                            scrollh = sv.getScrollY();
                            float deltaX = x2 - x1;
                            if (Math.abs(deltaX) > MIN_DISTANCE && twist) {
                                // Left to Right swipe action
                                if (x2 > x1) {
                                    Toast.makeText(ctx, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
                                }

                                // Right to left swipe action
                                else {
                                    Toast.makeText(ctx, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show();
                                }

                            }
                            break;
                    }

                    if(twist) {
                        move = event.getX();
                        if((Math.abs(move -x1) > 12)) {
                            popupView.setX(move - x1);
                        }
                        if (popupView.getX() > 120) {
                            popupView.setX(120);
                        }
                        if (popupView.getX() < -100) {
                            popupView.setX(-100);
                        }
                    }
                    scroll = event.getY();
                    int amount = (int)(y1 - scroll);
                    sv.scrollTo(0, (int) (scrollh + amount));
                    if(Math.abs(amount) > 40){
                        twist = false;
                        popupView.setX(0);
                    }else{
                        twist = true;
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        popupView.setX(0);
                        scrollh = sv.getScrollY();
                    }



                    return true;
                }
            });

//            popupView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            x1 = event.getX();
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            x2 = event.getX();
//                            float deltaX = x2 - x1;
//                            if (Math.abs(deltaX) > MIN_DISTANCE) {
//                                // Left to Right swipe action
//                                if (x2 > x1) {
//                                    Toast.makeText(ctx, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show();
//                                }
//
//                                // Right to left swipe action
//                                else {
//                                    Toast.makeText(ctx, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show();
//                                }
//
//                            } else {
//
//                            }
//                            break;
//                    }
//
//                    move = event.getX();
//                    popupView.setX(move - x1);
//                    if (popupView.getX() > 120) {
//                        popupView.setX(120);
//                    }
//                    if (popupView.getX() < -100) {
//                        popupView.setX(-100);
//                    }
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        popupView.setX(0);
//                    }
//
//                    return true;
//                }
//            });

    } // End constructor

    // Attaches the view to its parent anchor-view at position x and y
    public void show(View anchor, int x, int y)
    {
        showAtLocation(anchor, Gravity.CENTER, x, y);
    }

}


