package com.example.korba.gdziejestmojpromotor2.model;

/**
 * Created by korba on 19.11.16.
 */

public class Users {

    int _id;
    String _email;
    String _password;



    public Users(){

    }

    public Users(int id, String email, String password){
        this._id = id;
        this._email = email;
        this._password = password;

    }


    public Users(String email, String password){
        this._email = email;
        this._password = password;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }
}
