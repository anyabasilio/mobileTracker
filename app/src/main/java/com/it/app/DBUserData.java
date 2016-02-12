package com.it.app;

/**
 * Created by Rusty on 8/26/14.
 */
public  class DBUserData {

    //private variables
    int _id;
    String _name;
    String _uname;
    String _upass;

    // Empty constructor
    public DBUserData(){

    }


    // constructor
    public DBUserData(int id, String name, String uname, String upass){
        this._id = id;
        this._name = name;
        this._uname = uname;
        this._upass = upass;
    }

    // constructor
    public DBUserData(String name, String uname, String upass){
        this._name = name;
        this._uname = uname;
        this._upass = upass;
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


    public String getUname(){
        return this._uname;
    }


    public void setUname(String uname){
        this._uname = uname;
    }

    public String getUpass(){
        return this._upass;
    }


    public void setUpass(String upass){
        this._upass = upass;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UserInfo [name=" + _name + ", email=" + _uname + ",password =" + _upass + "]";
    }
}
