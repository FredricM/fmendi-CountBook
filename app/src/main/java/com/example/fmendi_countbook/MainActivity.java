package com.example.fmendi_countbook;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/*
 * Main Activity, holds the list of all the counters. Can add a new counter with
 * name, initial value and comments. Select a counter on the list and choose to view/edit
 * or delete.
 */
public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "project.sav"; // save file for the date
    private Counter selected; // the current counter selected on the listView
    private Counter saved; // saved the counters parameters after view/edit counter
    private int saved_position; // the current counter's position in the listView

    private EditText nameCounterView; // counter's name text box
    private EditText intValueView; // initial value text box
    private EditText commentsView; // counter's comment text box
    private EditText selectedCounterView; // selected counter text box
    private ListView counterView; // the list for the counters

    ArrayList<Counter> CounterArray; // create the array of custom objects
    private CounterAdapter counterAdapter; // create the custom adapter


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // layout for the MainActivity.

        Button addOnClick = (Button) findViewById(R.id.addView); // ADD button for counters
        Button deleteOnClick = (Button) findViewById(R.id.deleteView); // DELETE button for counters
        Button viewOnClick = (Button) findViewById(R.id.viewEdit); // View/Edit button for counters

        /*
         * Attributing all the text box variables with corresponding text box in layout
         */
        nameCounterView = (EditText) findViewById(R.id.addCounter);
        intValueView = (EditText) findViewById(R.id.initialValue);
        commentsView = (EditText) findViewById(R.id.commentCounter);
        selectedCounterView = (EditText) findViewById(R.id.selectedCounter);
        selectedCounterView.setEnabled(false); // User is unable to edit this text box
        selectedCounterView.setClickable(false); // User is unable to click this text box
        counterView = (ListView) findViewById(R.id.counterView);

        /*
         * When user clicks on add button, create a counter with all attributes
         * from the name, initial, and comment textbox.
         */
        addOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameCounterView.getText().toString(); // get name
                String value = intValueView.getText().toString();   // get initial
                String comments = commentsView.getText().toString(); // get comment

                // Using an alert dialog, if input for a name is nothing. Tell user input a name.
                if(name.matches("") ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Input a Counter name");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }

                //Using alert dialog, if input for initial value is not integer. Input "+" integer
                else if ( value.matches("^\\d+$") == false){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Place a positive integer value");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }

                //Counter can be empty. If no errors then create a new counter and put on list.
                else {
                    nameCounterView.setText("Counter name");
                    intValueView.setText("Initial value");
                    commentsView.setText("Comments");
                    int finalValue=Integer.parseInt(value);
                    CounterArray.add(new Counter(name, finalValue, comments, new Date()));
                    counterAdapter.notifyDataSetChanged();
                    saveInFile();
                }
            }
        });

        // Selecting items off the list and placing it in the "selected" text box
        counterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = (Counter) counterView.getItemAtPosition(position);
                saved_position = position;
                selectedCounterView.setText(selected.getName());
            }
        });

        // If there is a selected counter then will be able to delete the counter. O.W do nothing
        deleteOnClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CounterArray.remove(selected);
                selectedCounterView.setText("Select a Counter");
                counterAdapter.notifyDataSetChanged();
                saveInFile();
            }
        });

        // If there is no selected counter prompt user to pick a counter.
        // If selected a counter, then able to view/edit the counter.
        viewOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prompt user to pick counter from list
                if(selected == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Select a Counter");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }

                // View/edit counter and go to CounterActivity to do Counter functions
                // Using startActivityForResult.
                else {
                    selectedCounterView.setText("Select a Counter");
                    Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                    intent.putExtra("counter", selected);
                    selected = null; // reset the selected counter.
                    startActivityForResult(intent, 1); // go to CounterActivity
                    onActivityResult(1, 1, intent); // Receive results if saved
                }
            }
        });
    }

    // Taken from LonelyTwitter labs and branches
    // On start up load a file and fill list with counters. If not found create file.
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile(); //taken from lonely twitter, load file

        // initialize the custom Adapter and attach it to our list.
        counterAdapter = new CounterAdapter(this, R.layout.list_item, CounterArray);
        counterView.setAdapter(counterAdapter);
    }

    // from lonely twitter, changed for custom object
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Counter>>(){}.getType();
            CounterArray = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            CounterArray = new ArrayList<Counter>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    //from lonely twitter, changed for custom object
    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            // convert list into a string. and saving it on output.
            gson.toJson(CounterArray, out);
            out.flush();
            fos.close();
            //something fails it will catch.
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    // if clicking on add text box, clear the text box.
    public void addClearOnClick(View v){
        nameCounterView = (EditText) findViewById(R.id.addCounter);
        nameCounterView.setText("");
    }

    // if clicking on the initial value text box, clear the text box
    public void valueClearOnClick(View v){
        intValueView = (EditText) findViewById(R.id.initialValue);
        intValueView.setText("");
    }

    // if clicking on the comment text box, clear the text box
    public void commentClearOnClick(View v){
        commentsView = (EditText) findViewById(R.id.commentCounter);
        commentsView.setText("");
    }

    // For using getting the results for using StartActivityForResult() and calling
    // onActivityResult. return results if User clicks on SAVED in the CounterActivity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                saved = (Counter) data.getSerializableExtra("savedCounter"); // retrieve counter.
                CounterArray.set(saved_position, saved); // modify counter in the list
                counterAdapter.notifyDataSetChanged();
                saveInFile();
            }
        }
    }
}
