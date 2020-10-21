package com.five5.chatroom.Data;

import java.util.ArrayList;

public class user {
    String pass;
    String email;
    ArrayList<SubChannel> chnls;

    public user(String pass, String email, ArrayList<SubChannel> chnls) {
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

    public ArrayList<SubChannel> getChnls() {
        return chnls;
    }

    public void setChnls(ArrayList<SubChannel> chnls) {
        this.chnls = chnls;
    }
}
