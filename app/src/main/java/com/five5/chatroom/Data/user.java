package com.five5.chatroom.Data;

import java.util.ArrayList;
import java.util.HashMap;

public class user {
    String pass;
    String email;
    HashMap<String,SubChannel> chnls;

    public user(String pass, String email, HashMap<String,SubChannel> chnls) {
        this.pass = pass;
        this.email = email;
        this.chnls = chnls;
    }

    public user() {
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, SubChannel> getChnls() {
        return chnls;
    }

    public void setChnls(HashMap<String, SubChannel> chnls) {
        this.chnls = chnls;
    }
}
