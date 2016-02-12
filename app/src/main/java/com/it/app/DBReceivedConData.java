package com.it.app;

/**
 * Created by Rusty on 8/29/14.
 */
public class DBReceivedConData {

    //private variables
    int _id;
    String _name;
    String _num;

    // Empty constructor
    public DBReceivedConData(){

    }


    // constructor
    public DBReceivedConData(int id, String name, String num){
        this._id = id;
        this._name = name;
        this._num = num;
    }

    // constructor
    public DBReceivedConData(String name, String num){
        this._name = name;
        this._num = num;
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
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }
    // getting name
    public String getNum(){
        return this._num;
    }

    // setting name
    public void setNum(String num){
        this._num = num;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "["+_name + " : " + _num +"]";
    }
}
