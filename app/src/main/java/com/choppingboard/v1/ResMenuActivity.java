package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResMenuActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<JSONObject>> listDataChild;
    DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_menu);

        db = new DatabaseHandler(this);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ResMenuActivity.this, MenuExpand.class);
                try {
                    intent.putExtra("hello", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getString("number"));
                }catch (JSONException e){
                    e.printStackTrace();
                }
                startActivity(intent);

                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<JSONObject>>();

        Map<String, ArrayList<JSONObject>> Menu = db.getCatMenu();

        listDataHeader.addAll(Menu.keySet());
        for(String s : listDataHeader){
            listDataChild.put(s, Menu.get(s));
        }

        // Adding child data
//        listDataHeader.add("Combos");
//        listDataHeader.add("Platters");
//        listDataHeader.add("Signature Sides");
//        listDataHeader.add("Boneless Chicken");
//        listDataHeader.add("Bone In Chicken");
//        listDataHeader.add("Buttermilk Biscuits");
//        listDataHeader.add("Family Meals");
//        listDataHeader.add("Dessert");
//        listDataHeader.add("Beverages");
//
//        List<String> Combos = new ArrayList<String>();
//        List<String> Platters = new ArrayList<String>();
//        List<String> SignatureSides = new ArrayList<String>();
//        List<String> BonelessChicken = new ArrayList<String>();
//        List<String> BoneInChicken = new ArrayList<String>();
//        List<String> ButtermilkBiscuits = new ArrayList<String>();
//        List<String> FamilyMeals = new ArrayList<String>();
//        List<String> Dessert = new ArrayList<String>();
//        List<String> Beverages = new ArrayList<String>();

//        for(JSONObject o : db.getAllMenu()){
//            try {
//
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//        }


//        listDataChild.put(listDataHeader.get(0), Combos); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), Platters);
//        listDataChild.put(listDataHeader.get(2), SignatureSides);
//        listDataChild.put(listDataHeader.get(3), BonelessChicken);
//        listDataChild.put(listDataHeader.get(4), BoneInChicken);
//        listDataChild.put(listDataHeader.get(5), ButtermilkBiscuits);
//        listDataChild.put(listDataHeader.get(6), FamilyMeals);
//        listDataChild.put(listDataHeader.get(7), Dessert);
//        listDataChild.put(listDataHeader.get(8), Beverages);


    }
}
