package com.it.app;

/**
 * Created by Rusty on 8/26/14.
 */
public  class DBMessages {

    //private variables
    int _id;
    String _clientnum;
    String _lat;
    String _lon;
    String _address;
    String _type;
    String _smscontact;
    String _smscontent;
    String _smsdate;

    // Empty constructor
    public DBMessages(){

    }


    // constructor
    public DBMessages(int id,String clientnum, String lat, String lon,String address,String type,String smscontact,String smscontent, String smsdate){
        this._id = id;
        this._clientnum = clientnum;
        this._lat = lat;
        this._lon = lon;
        this._address = address;
        this._type = type;
        this._smscontact = smscontact;
        this._smscontent = smscontent;
        this._smsdate = smsdate;
    }

    // constructor
    public DBMessages( String clientnum,String lat, String lon,String address,String type,String smscontact,String smscontent, String smsdate){
        this._clientnum = clientnum;
        this._lat = lat;
        this._lon = lon;
        this._address = address;
        this._type = type;
        this._smscontact = smscontact;
        this._smscontent = smscontent;
        this._smsdate = smsdate;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getClientnum(){
        return this._clientnum;
    }

    public void setClientnum(String clientnum){
        this._clientnum = clientnum;
    }

    public String getLat(){
        return this._lat;
    }

    public void setLat(String lat){
        this._lat = lat;
    }

    public String getLon(){
        return this._lon;
    }

    public String getAddress(){
        return this._address;
    }

    public void setAddress(String address){
        this._address = address;
    }

    public void setType(String type){
        this._type = type;
    }

    public String getType(){
       return this._type;
    }

    public void setLon(String lon){
        this._lon = lon;
    }

    public String getSMScontact(){
        return this._smscontact;
    }

    public void setSMScontact(String smscontact){
        this._smscontact = smscontact;
    }

    public String getSMScontent(){
        return this._smscontent;
    }

    public void setSMScontent(String smscontent){
        this._smscontent = smscontent;
    }

    public String getSMSdate(){
        return this._smsdate;
    }

    public void setSMSdate(String smsdate){
        this._smsdate = smsdate;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MessagesInfo [lat=" + _lat + ", lon=" + _lon + ",body =" + _smscontent + "]";
    }
}
