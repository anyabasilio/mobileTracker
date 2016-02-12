package com.it.app;

/**
 * Created by Rusty on 8/26/14.
 */
public  class DBSimnumbersData {

    //private variables
    int _id;
    String _num;

    // Empty constructor
    public DBSimnumbersData(){

    }


    // constructor
    public DBSimnumbersData(int id, String num){
        this._id = id;
        this._num = num;
    }

    // constructor
    public DBSimnumbersData(String num){
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
        return _num;
    }
}

