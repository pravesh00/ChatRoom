package com.five5.chatroom.restapi;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface notificationAPI {
    @Headers({"Authorization: key=AAAASqT_mg4:APA91bG85qdhIGGvFXTxDnCpWEtY281CHC1nHljiu8Wfp8nLOy0ahYQzgwHp1XVpiwNNxydVJEaLKMtRarAlFzziC6wenl4OXTURl504nz8hsSkxT4GePVnzO9jmG5mzsj4JEnNFLEaO",
    "Content-Type: application/json"})
    @POST("fcm/send")
    Call<JsonObject> sendnotification(@Body JsonObject jsonObject);

}
