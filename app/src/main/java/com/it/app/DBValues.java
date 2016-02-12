package com.it.app;

/**
 * Created by Rusty on 8/26/14.
 */
public  class DBValues {
    int _id;
    String _name;
    String _value;
    public DBValues(){

    }
    public DBValues(int id, String val1, String val2){
        this._id = id;
        this._name=val1;
        this._value=val2;
    }
    public DBValues(String val1, String val2){
        this._name=val1;
        this._value=val2;
    }

    public int getID(){
        return this._id;
    }
    public void setID(int id){
        this._id = id;
    }
    public String getName(){
        return this._name;
    }
    public void setName(String val){
        this._name = val;
    }
    public String getValue(){
        return this._value;
    }
    public void setValue(String val){
        this._value = val;
    }
    @Override
    public String toString() {
        return "UserInfo [name=]";
    }
}
