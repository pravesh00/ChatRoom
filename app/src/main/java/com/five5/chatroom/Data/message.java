package com.five5.chatroom.Data;

public class message {
    String mssgId,timeStamp,senderId,mssg;


    public message(String mssgId, String timeStamp, String senderId, String mssg) {
        this.mssgId = mssgId;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.mssg = mssg;

    }

    public message() {
    }

    public String getMssgId() {
        return mssgId;
    }

    public void setMssgId(String mssgId) {
        this.mssgId = mssgId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMssg() {
        return mssg;
    }

    public void setMssg(String mssg) {
        this.mssg = mssg;
    }


}
