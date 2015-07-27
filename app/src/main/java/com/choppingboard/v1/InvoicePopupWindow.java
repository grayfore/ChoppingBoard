package com.choppingboard.v1;

import android.content.Context;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Jeff on 6/29/15.
 */
public class InvoicePopupWindow extends android.widget.PopupWindow {

    //....... Fields ........ //
    Context ctx;
    //    Button btnDismiss;
    View popupView;
    TextView orderid;
    TextView ordertime;
    TextView name;
    TextView add;
    TextView custnum;
    ImageView imagestatus;
    ImageView acceptbar;
    ImageView denybar;
    ScrollView sv;
    TableLayout table;
    private float x1, x2, move, y1, y2, scroll, scrollh;
    static final int MIN_DISTANCE = 120;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    boolean twist = true;
    View deny;
    PopupDeny pd;
    private static final String TAG = "PopupWindow";
    DatabaseHandler db;
    InvoiceCustomList adapter;

    //........ Constructor ........//
    public InvoicePopupWindow(Context context, final JSONObject o, View v, final InvoiceCustomList adapter) {
        super(context);

        //Initializing needed UI elements and fields//
        deny = v;
        ctx = context;
        db = new DatabaseHandler(ctx);
        this.adapter = adapter;
        popupView = LayoutInflater.from(context).inflate(R.layout.popup, null);
        setContentView(popupView);
        sv = (ScrollView) popupView.findViewById(R.id.listorderd);
//        btnDismiss = (Button)popupView.findViewById(R.id.btn_dismiss);
        orderid = (TextView) popupView.findViewById(R.id.orderid);
        ordertime = (TextView) popupView.findViewById(R.id.ordertime);
        name = (TextView) popupView.findViewById(R.id.name);
        add = (TextView) popupView.findViewById(R.id.add);
        custnum = (TextView) popupView.findViewById(R.id.custnum);
        table = (TableLayout) popupView.findViewById(R.id.orderItems);
        imagestatus = (ImageView)popupView.findViewById(R.id.imagestatus);

        //defines the size of the actual popup
        setHeight(850);
        setWidth(550);

        // Closes the popup window when touch outside of it - when looses focus
        setOutsideTouchable(true);
        setFocusable(true);
        setTouchable(true);

        // Sets the background for the swipe accept and deny
        TextDrawable back = new TextDrawable();
        setBackgroundDrawable(back);

//        btnDismiss.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }});

        //Creates the order by setting up customer info at the top of the screen
        //then dynamically adding rows to a tableview...
        try {
            orderid.setText("Order " + o.getString("id"));
            ordertime.setText("Order time: " + o.getString("reqtime"));
            name.setText("Name: " + o.getString("custName"));
            add.setText("Address: " + o.getString("custAdd"));
            custnum.setText("Number: " + o.getString("custNum"));

            //Then it loops through to get each ordered item....
            for (int i = 1; i <= 10; i++) {
                if (o.getString("OrderItem" + i) != null) {
                    String orderItem = o.getString("OrderItem" + i);
                    JSONObject orderitem = new JSONObject(orderItem);
                    // Inflate your row "template" and fill out the fields.
                    TableRow row = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.attrib_row, null);
                    ((TextView) row.findViewById(R.id.attrib_name)).setText(orderitem.getString("MenuItem"));
                    ((TextView) row.findViewById(R.id.attrib_value)).setText("$" + orderitem.getString("Price"));
                    ((TextView) row.findViewById(R.id.quantity)).setText("x" + orderitem.getString("Quantity"));
                    table.addView(row);

                    //and then another loop inside that loop to grab the extras
                    if (!orderitem.getString("Extras").equals("[null]")) {
                        TableRow extra = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.extra_row, null);
                        String thing = orderitem.getString("Extras");
                        String[] array = thing.split(",");
                        String stuff = "";
                        for (int j = 0; j < array.length; j++) {
                            stuff += (j + 1) + ": " + array[j] + "\n";
                        }
                        String newstuff = stuff.replace("[", "").replace("\"", "").replace("]", "");
                        ((TextView) extra.findViewById(R.id.attrib_name)).setText(newstuff);
                        table.addView(extra);
                    }

//                    TableRow instructions = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.extra_row, null);
//                    String instruc = orderitem.getString("Instructions");
//                    ((TextView) instructions.findViewById(R.id.attrib_name)).setText(instruc);
//                    table.addView(instructions);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //this is in a different try-catch because it doesnt work when its in the other one for some reason
        //These last
        try {
            TableRow deliv = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.total_row, null);
            ((TextView) deliv.findViewById(R.id.attrib_name)).setText("Delivery Charge: ");
            ((TextView) deliv.findViewById(R.id.attrib_value)).setText("$" + o.getString("delivCharge"));

            TableRow tax = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.total_row, null);
            ((TextView) tax.findViewById(R.id.attrib_name)).setText("Tax: ");
            ((TextView) tax.findViewById(R.id.attrib_value)).setText("$" + o.getString("salesTax"));
            tax.setPadding(0, 0, 0, 0);

            TableRow total = (TableRow) LayoutInflater.from(ctx).inflate(R.layout.total_row, null);
            ((TextView) total.findViewById(R.id.attrib_name)).setText("Subtotal: ");
            ((TextView) total.findViewById(R.id.attrib_value)).setText("$" + o.getString("subtotal"));
            total.setPadding(0, 0, 0, 0);

            table.addView(deliv);
            table.addView(tax);
            table.addView(total);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //This intercepts all touches that occur... all touch action is in here
        this.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        y2 = event.getY();
                        float deltaX = x2 - x1;
                        break;
                }
                //Below is for the scrolling action
                scroll = event.getY();
                int amount = (int) (y1 - scroll);
                sv.scrollTo(0, (int) (scrollh + amount));
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    popupView.setX(0);
                    scrollh = sv.getScrollY();
                }

                return true;
            }
        });

    } // End constructor

    // Attaches the view to its parent anchor-view at position x and y
    public void show(View anchor, int x, int y) {
        showAtLocation(anchor, Gravity.CENTER, x, y);
    }

    // If the user swipes to deny, it opens up a deny popupwindow
    public void openDeny(JSONObject o) {
        pd = new PopupDeny(ctx, o);
        pd.show(deny, 0, 0);
        Log.v("lambo", "Reached making a popup");
    }

    public PopupDeny getPd() {
        return pd;
    }


}

