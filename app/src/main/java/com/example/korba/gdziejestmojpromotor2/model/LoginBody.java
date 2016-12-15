package com.example.korba.gdziejestmojpromotor2.model;

/**
 * Created by korba on 15.12.16.
 */

public class LoginBody {

    private String email;
    private String password;
    private String status;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {
        this.status = status;
    }

}
