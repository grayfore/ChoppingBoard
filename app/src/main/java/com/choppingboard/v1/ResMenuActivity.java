package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ResMenuActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
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
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();

                Intent intent = new Intent(ResMenuActivity.this, MenuExpand.class);
                intent.putExtra("hello", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                startActivity(intent);

                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Combos");
        listDataHeader.add("Platters");
        listDataHeader.add("Signature Sides");
        listDataHeader.add("Boneless Chicken");
        listDataHeader.add("Bone In Chicken");
        listDataHeader.add("Buttermilk Biscuits");
        listDataHeader.add("Family Meals");
        listDataHeader.add("Dessert");
        listDataHeader.add("Beverages");

        List<String> Combos = new ArrayList<String>();
        Combos.add("Bone In Chicken");
        Combos.add("Boneess Chicken");
        Combos.add("Seafood");

        List<String> Platters = new ArrayList<String>();
        Platters.add("Bone In Chicken");
        Platters.add("Boneless Chicken");
        Platters.add("Seafood");

        List<String> SignatureSides = new ArrayList<String>();
        SignatureSides.add("Corn on the Cob");
        SignatureSides.add("Onion Rings");
        SignatureSides.add("Cajun Rice");
        SignatureSides.add("Mashed Potatoes with Cajun Gravy");
        SignatureSides.add("Red Beans and Rice");
        SignatureSides.add("Cajun Fries");
        SignatureSides.add("Green Beans");

        List<String> BonelessChicken = new ArrayList<String>();
        BonelessChicken.add("3 Piece Tenders");
        BonelessChicken.add("5 Piece Tenders");
        BonelessChicken.add("10 Piece Tenders");
        BonelessChicken.add("15 Piece Tenders");
        BonelessChicken.add("25 Piece Tenders");
        BonelessChicken.add("50 Piece Tenders");
        BonelessChicken.add("75 Piece Tenders");
        BonelessChicken.add("100 Piece Tenders");

        List<String> BoneInChicken = new ArrayList<String>();
        BoneInChicken.add("8 Piece Mixed Chicken");
        BoneInChicken.add("12 Piece Mixed Chicken");
        BoneInChicken.add("16 Piece Mixed Chicken");
        BoneInChicken.add("20 Piece Mixed Chicken");
        BoneInChicken.add("24 Piece Mixed Chicken");
        BoneInChicken.add("50 Piece Mixed Chicken");
        BoneInChicken.add("100 Piece Mixed Chicken");
        BoneInChicken.add("200 Piece Mixed Chicken");

        List<String> ButtermilkBiscuits = new ArrayList<String>();
        ButtermilkBiscuits.add("1 Biscuit");
        ButtermilkBiscuits.add("6 Biscuits");
        ButtermilkBiscuits.add("12 Biscuits");

        List<String> FamilyMeals = new ArrayList<String>();
        FamilyMeals.add("12 Pieces Mixed Chicken");
        FamilyMeals.add("16 Pieces Mixed Chicken");
        FamilyMeals.add("20 Pieces Mixed Chicken");
        FamilyMeals.add("24 Pieces Mixed Chicken");


        List<String> Dessert = new ArrayList<String>();
        Dessert.add("Cinnamon Apple Pie");
        Dessert.add("2 Cinnamon Apple Pies");
        Dessert.add("Lemonade Ice Box Pie");


        List<String> Beverages = new ArrayList<String>();
        Beverages.add("Bottled Water");
        Beverages.add("Bottled Soda");
        Beverages.add("Gallons");



        listDataChild.put(listDataHeader.get(0), Combos); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Platters);
        listDataChild.put(listDataHeader.get(2), SignatureSides);
        listDataChild.put(listDataHeader.get(3), BonelessChicken);
        listDataChild.put(listDataHeader.get(4), BoneInChicken);
        listDataChild.put(listDataHeader.get(5), ButtermilkBiscuits);
        listDataChild.put(listDataHeader.get(6), FamilyMeals);
        listDataChild.put(listDataHeader.get(7), Dessert);
        listDataChild.put(listDataHeader.get(8), db.getAllMenu());


    }
}
