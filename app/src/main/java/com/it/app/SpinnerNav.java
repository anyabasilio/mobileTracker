package com.it.app;

/**
 * Created by Rusty on 9/7/14.
 */
public class SpinnerNav {

    private String title;
    private int icon;

    public SpinnerNav(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }
}