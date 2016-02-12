package com.it.app;

/**
 * Created by Rusty on 8/29/14.
 */
public  class DBServernum {

    //private variables
    int _id;
    String _senum;

    // Empty constructor
    public DBServernum(){

    }


    // constructor
    public DBServernum(int id, String num){
        this._id = id;
        this._senum = num;
    }

    // constructor
    public DBServernum(String num){
        this._senum = num;
    }


    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }
    // getting name
    public String getNum(){
        return this._senum;
    }

    // setting name
    public void setNum(String num){
        this._senum = num;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "["+ _senum +"]";
    }
}