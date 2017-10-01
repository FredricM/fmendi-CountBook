package com.example.fmendi_countbook;

/**
 * Created by Fredric on 2017-09-30.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*
 * Create a custom adapter for the list in main activity.
 * Make list view a little bit nicer looking and less plain
 * ************* CITE*****************************8
 * TAKEN FROM https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/
 * 2017-09-30
 * changed to fit Counter object instead.
 */
public class CounterAdapter extends ArrayAdapter<Counter> {

    private ArrayList<Counter> objects;  // declaring our ArrayList of items

    // Constructor for the CounterAdapter.
    public CounterAdapter(Context context, int textViewResourceId, ArrayList<Counter> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    // Override the getView method, and make the look of each item on the list.
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView; // assign view to a variable v

        // if view is null, then show view with inflate.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        Counter i = objects.get(position); // custom item object and get it's position in list

        if (i != null) {
            // obtain a reference to the TextViews.
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            TextView ttd = (TextView) v.findViewById(R.id.toptextdata);
            TextView mt = (TextView) v.findViewById(R.id.middletext);
            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
            TextView btd = (TextView) v.findViewById(R.id.desctext);

            // check to see if each individual textview is null.
            // if not, assign some text
            if (tt != null){
                tt.setText("Name: ");
            }
            if (ttd != null){
                ttd.setText(i.getName()); // set the name
            }
            if (mt != null){
                String temp = Integer.toString(i.getInitial());
                mt.setText("inital " + temp + " current = " + i.getCurrent()); // initial & current
            }
            if (bt != null){
                SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
                bt.setText("Last Date changed: " + sdf.format( i.getDate())); // date in yyyy-mm-dd
            }
            if (btd != null){
                btd.setText(i.getComment()); // comments for counter.
            }
        }
        // view is returned to activity
        return v;
    }
}