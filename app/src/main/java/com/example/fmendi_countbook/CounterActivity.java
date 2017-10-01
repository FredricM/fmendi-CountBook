package com.example.fmendi_countbook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fredric on 2017-09-30.
 */

// Counter Activity for all the functions of a counter.
// increase, decrease, change initial, change name, reset, and save
public class CounterActivity extends AppCompatActivity {

    private Counter parameters; // counter object for to get counter from intent from main activity
    private String initialChange; // store value of initial for resetting
    private int counterValue; // store the value of current

    // create variable for all the text boxes
    private TextView nameView;
    private EditText initialView;
    private EditText commentView;
    private EditText dateView;
    private EditText valueView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_main); // using counter_main layout

        // grab intent from main activity
        Intent intent = getIntent();
        parameters = (Counter) intent.getSerializableExtra("counter"); // store in paremeters.

        // get and set name
        nameView = (TextView) findViewById(R.id.nameView);
        nameView.setText(parameters.getName());

        // get and set initial value and make it non-editable, non-clickable to user.
        initialView = (EditText) findViewById(R.id.initialView);
        initialView.setText(Integer.toString(parameters.getInitial()));
        initialChange = Integer.toString(parameters.getInitial());
        initialView.setEnabled(false);
        initialView.setClickable(false);

        // get and set comment
        commentView = (EditText) findViewById(R.id.commentView);
        commentView.setText(parameters.getComment());

        // get and set date in the form of yyyy-mm-dd, non-editable, non-clickable
        dateView = (EditText) findViewById(R.id.dateView);
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        dateView.setText( sdf.format( parameters.getDate()));
        dateView.setClickable(false);
        dateView.setEnabled(false);

        // get and set current value of counter. non-editable, non-clickable.
        valueView = (EditText)findViewById(R.id.valueView);
        valueView.setText(Integer.toString(parameters.getCurrent()));
        valueView.setClickable(false);
        valueView.setEnabled(false);

        // have variable for current value available for use.
        counterValue = parameters.getCurrent();
    }

    // When clicking increase button then increase counterValue variable and change text box to it.
    public void increaseClick(View view){
        counterValue++;
        valueView.setText(String.valueOf(counterValue));
    }

    // When clicking decrease button then decrease counterValue variable and change text box to it.
    public void decreaseClick(View view){
        // If value is 0, cannot be a negative number. Do nothing if current value is 0.
        if (counterValue <= 0){
        }else {
            counterValue--;
            valueView.setText(String.valueOf(counterValue));
        }
    }

    // using alert dialog, make user input a new initial value, which will be set on pressing reset.
    public void changeOnClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Initial value. Will change on Reset");
        final EditText input = new EditText(this); // ask user for new number.
        input.setInputType((InputType.TYPE_CLASS_NUMBER)); //input type prevents negative and decimal.
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String initialChangeCheck = input.getText().toString();

                // check if it is a string, if a string then close and do nothing.
                if ( initialChangeCheck.matches("^\\d+$") ){
                    initialChange = initialChangeCheck;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // Will reset initial value if changed, reset current value to inital value.
    public void resetOnClick(View view){
        initialView.setText(initialChange);
        valueView.setText(initialChange);
        counterValue = Integer.parseInt(initialChange);
    }

    // Save all activities done on the counter if user clicks save. O.W data is lost.
    public void saveOnClick(View view){
        // get text boxes
        nameView = (TextView) findViewById(R.id.nameView);
        valueView = (EditText)findViewById(R.id.valueView);
        dateView = (EditText) findViewById(R.id.dateView);
        initialView = (EditText) findViewById(R.id.initialView);
        commentView = (EditText) findViewById(R.id.commentView);

        // put text box values into parameter
        parameters.setName(nameView.getText().toString());
        parameters.setInitial(Integer.parseInt(initialView.getText().toString()));
        parameters.setComment(commentView.getText().toString());
        parameters.setDate(new Date());
        parameters.setCurrent(Integer.parseInt(valueView.getText().toString()));

        // return the intent from StartActivityForResult().
        Intent returnIntent = new Intent();
        returnIntent.putExtra("savedCounter", parameters);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}