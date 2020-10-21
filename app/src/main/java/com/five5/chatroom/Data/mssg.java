package com.five5.chatroom.Data;

public class mssg {
    String text;
    String time;
    Long millis;

    public mssg(String text, String time, Long millis) {
        this.text = text;
        this.time = time;
        this.millis = millis;
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
}
