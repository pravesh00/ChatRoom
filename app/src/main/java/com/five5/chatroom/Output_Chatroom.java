package com.five5.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.five5.chatroom.Adapter.messageAdapter;
import com.five5.chatroom.Data.mssg;
import com.five5.chatroom.restapi.notificationAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Output_Chatroom extends AppCompatActivity {
    EditText mssgBox;
    ImageButton sendBtn;
    ProgressBar sendPgr;
    RecyclerView mssgRecycler;
    ArrayList<mssg> arrayMssg= new ArrayList<>();
    Button img;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    TextView chanl;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String user=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final messageAdapter adapter = new messageAdapter(arrayMssg,user);
        setContentView(R.layout.activity_output__chatroom);
        intializeUI();


        arrayMssg.add(new mssg("Loading Messages...","", (long) 0,"Please Wait"));
        final LinearLayoutManager mlay=new LinearLayoutManager(this);
        mssgRecycler.setLayoutManager(mlay);
        mRef =FirebaseDatabase.getInstance().getReference();
        final String chnl= getIntent().getStringExtra("Name");
        chanl.setText(chnl);
        status=(TextView)findViewById(R.id.txtTypeStatus);

        mssgRecycler.setAdapter(adapter);

        DatabaseReference dref=FirebaseDatabase.getInstance().getReference().child("Status").child(chnl);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                changeStatus(snapshot.getValue().toString(),status);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mssgBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        FirebaseDatabase.getInstance().getReference().child("Status").child(chnl).setValue(user+" is typing...");



            }

            @Override
            public void afterTextChanged(Editable editable) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseDatabase.getInstance().getReference().child("Status").child(chnl).setValue("");
                    }
                },1500);



            }
        });

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        mRef= mRef.child("Messages").child(chnl);

        mRef.orderByChild("millis").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayMssg.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    mssg msg=new mssg(snap.child("text").getValue().toString(),snap.child("time").getValue().toString(),Long.parseLong(snap.child("millis").getValue().toString()),snap.child("sender").getValue().toString());
                    arrayMssg.add(msg);
                    Log.e("Messages",snap.toString());
                    mssgRecycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                    mlay.smoothScrollToPosition(mssgRecycler,null,adapter.getItemCount()-1);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),subscribedChannels.class));

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date= new Date();
                String d= date.toString().substring(11,16);





                if(!mssgBox.getText().toString().isEmpty())
                {
                    changeLastMssg(chnl,mssgBox.getText().toString());
                    adapter.notifyDataSetChanged();
                    mRef.push().setValue(new mssg(mssgBox.getText().toString(),d,date.getTime(),FirebaseAuth.getInstance().getCurrentUser().getEmail()));



                    mlay.smoothScrollToPosition(mssgRecycler,null,adapter.getItemCount()-1);
                  notificationAPI api = retrofit.create(notificationAPI.class);
                    JsonObject notify = new JsonObject();
                    String a="/topics/"+chnl;
                    notify.addProperty("to",a);
                    JsonObject notification = new JsonObject();
                    notification.addProperty("title",user);
                    notification.addProperty("body",mssgBox.getText().toString());
                    notify.add("notification",notification);
                    JsonObject data =new JsonObject();
                    data.addProperty("senderName", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    data.addProperty("channel",chnl);
                    data.addProperty("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    notify.add("data",data);
                    mssgBox.setText("");
                    Call<JsonObject> call = api.sendnotification(notify);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.e("send",response.message());
                            if(response.isSuccessful()){
                                Log.e("Success","Done");
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("Fail",t.getMessage());

                        }
                    });



                }
            }
        });
        mlay.setStackFromEnd(true);
        mlay.smoothScrollToPosition(mssgRecycler,null,adapter.getItemCount()-1);






    }

    private void changeStatus(final String toString, final TextView status) {

                status.setText(toString);

    }

    private void changeLastMssg(String chnl, final String toString) {
        final DatabaseReference mreference =FirebaseDatabase.getInstance().getReference();
        Query query=mreference.child("Channels").orderByChild("name").equalTo(chnl);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d:snapshot.getChildren()){
                    mreference.child("Channels").child(d.getKey()).child("lstMssg").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail()+": "+toString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changeStatus(final String toString, final TextView status) {

                status.setText(toString);

    }

    private void intializeUI() {
        mssgBox =(EditText) findViewById(R.id.txtMessageText);
        sendBtn=(ImageButton) findViewById(R.id.btnSend);

        mssgRecycler=(RecyclerView) findViewById(R.id.recyclerMessages);
        img=(Button)findViewById(R.id.btnCancel);
        chanl=(TextView)findViewById(R.id.ChannelName);

    }


}