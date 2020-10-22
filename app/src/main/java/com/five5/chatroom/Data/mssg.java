package com.five5.chatroom.Data;

public class mssg {
    String text;
    String time;
    Long millis;
    String sender;

    public mssg(String text, String time, Long millis, String sender) {
        this.text = text;
        this.time = time;
        this.millis = millis;
        this.sender = sender;
    }

    public mssg() {
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getMillis() {
        return millis;
    }

    public void setMillis(Long millis) {
        this.millis = millis;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
