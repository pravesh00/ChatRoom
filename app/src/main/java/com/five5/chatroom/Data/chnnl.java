package com.five5.chatroom.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class chnnl implements Serializable {

    String name;
    String lstMssg;

    public chnnl(String name, String lstMssg) {
        this.name = name;
        this.lstMssg = lstMssg;
    }

    public chnnl() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLstMssg() {
        return lstMssg;
    }

    public void setLstMssg(String lstMssg) {
        this.lstMssg = lstMssg;
    }
}
