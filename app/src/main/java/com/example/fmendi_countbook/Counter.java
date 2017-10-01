package com.example.fmendi_countbook;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fredric on 2017-09-30.
 */

/*
 * Making a custom object for a custom array adapter. Holding all the info for the counter
 * implements Serializable so it can be passed with intent.putExtra. and received.
 */
public class Counter implements Serializable {

    private String name; //name of counter
    private int initial; // initial value of counter
    private String comment; // comments of the comment
    private Date date; // date when it is created and changed
    private int current; //current value of the counter.

    public Counter(){
    }

    //Using this constructor on MainActivity to add counters to the listview
    public Counter(String name, int initial, String comment, Date date) {
        this.name = name;
        this.initial = initial;
        this.comment = comment;
        this.date = date;
        this.current = initial;
    }

    // Getters and setters of all elements of Counter
    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInitial() {
        return initial;
    }

    public void setInitial(int initial) {
        this.initial = initial;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
