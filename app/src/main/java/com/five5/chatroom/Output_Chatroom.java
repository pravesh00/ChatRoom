package com.five5.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.five5.chatroom.Adapter.messageAdapter;
import com.five5.chatroom.Data.message;
import com.five5.chatroom.restapi.notificationAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
    ArrayList<message> arrayMssg= new ArrayList<>();
    Button img;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    TextView chanl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String user=getIntent().getStringExtra("User");
        final messageAdapter adapter = new messageAdapter(arrayMssg,user);
        setContentView(R.layout.activity_output__chatroom);
        intializeUI();
        mssgRecycler.setAdapter(adapter);
        arrayMssg.add(new message("2","01:20","1","Message"));
        final LinearLayoutManager mlay=new LinearLayoutManager(this);
        mssgRecycler.setLayoutManager(mlay);
        mDatabase =FirebaseDatabase.getInstance();
        final String chnl= getIntent().getStringExtra("Name");
        chanl.setText(chnl);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRef= (DatabaseReference) mDatabase.getReference().child(chnl).child("Messages");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayMssg.clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    message msg=new message(snap.child("mssgId").getValue().toString(),snap.child("timeStamp").getValue().toString(),snap.child("senderId").getValue().toString(),snap.child("mssg").getValue().toString());
                    arrayMssg.add(msg);

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
                writeData();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date= new Date();
                String d= date.toString().substring(11,16);





                if(!mssgBox.getText().toString().isEmpty())
                {
                    mRef.child(String.valueOf(date.getTime())).setValue(new message("2",d,user,mssgBox.getText().toString()));
                    adapter.notifyDataSetChanged();



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
        FirebaseMessaging.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic(chnl);



    }

    private void intializeUI() {
        mssgBox =(EditText) findViewById(R.id.txtMessageText);
        sendBtn=(ImageButton) findViewById(R.id.btnSend);
        sendPgr=(ProgressBar) findViewById(R.id.pgrSend);
        mssgRecycler=(RecyclerView) findViewById(R.id.recyclerMessages);
        img=(Button)findViewById(R.id.btnCancel);
        chanl=(TextView)findViewById(R.id.ChannelName);

    }
    //JSonarray to file convertor
    public void writeData() {
        JSONObject j=new JSONObject();


        try {
            j.put("Name","");
            j.put("Id","");
            FileOutputStream fi= openFileOutput("X",MODE_PRIVATE);
            fi.write(j.toString().getBytes());
            fi.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }}

}