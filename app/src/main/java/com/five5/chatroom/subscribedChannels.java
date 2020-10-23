package com.five5.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.five5.chatroom.Adapter.ChannelAdapter;

import com.five5.chatroom.Data.SubChannel;
import com.five5.chatroom.Data.chnnl;
import com.five5.chatroom.Data.mssg;
import com.five5.chatroom.Data.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class subscribedChannels extends AppCompatActivity {

    DatabaseReference mref;
    Toolbar tool;
    FirebaseAuth mAuth;
    Button CreateChannel;
    TextView ChannelNameStatus;
    SearchView searchView;
    EditText txtChannelName;
    ImageButton cancelDialog;
    String userEmail;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_channels);
        userEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mref=FirebaseDatabase.getInstance().getReference();


        searchView=(SearchView) findViewById(R.id.search);

        tool = (Toolbar)findViewById(R.id.toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.channelRecyclerView,new channel_new(userEmail)).commit();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.channelRecyclerView,new search_channel(searchView,userEmail)).commit();
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportFragmentManager().beginTransaction().replace(R.id.channelRecyclerView,new channel_new(userEmail)).commit();
                return false;
            }
        });


        tool.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.channel:
                        createChannel();
                     return true;
                    case R.id.logout:
                        logout();
                    return true;
                    default:
                   return false;}
                
            }
        });

    }



    private void logout() {
        mAuth= FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent i = new Intent(this,emailAuth.class);
        startActivity(i);
    }

    private void createChannel() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_channel_dialog);
        dialog.show();
        txtChannelName=(EditText)dialog.findViewById(R.id.txtNewChannelName);
        CreateChannel=(Button)dialog.findViewById(R.id.btnCreateChannel);
        ChannelNameStatus=(TextView)dialog.findViewById(R.id.txtNameStatus);
        cancelDialog=(ImageButton)dialog.findViewById(R.id.btnCancelDialog);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        txtChannelName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             String text= txtChannelName.getText().toString();
             if(!text.isEmpty()){
                 checkifChannelExists(text,ChannelNameStatus);

             }else{
                 ChannelNameStatus.setText("Empty");
                 ChannelNameStatus.setTextColor(Color.GRAY);
             }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        CreateChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name= txtChannelName.getText().toString();
                String status=ChannelNameStatus.getText().toString();
                if(status.equals("Name Available!!")){
                    mref.child("Channels").push().setValue(new chnnl(name,"Welcome"));
                    mref.child("Status").child(name).setValue("");
                    Date date =new Date();
                    mref.child("Messages").child(name).child(String.valueOf(date.getTime())).setValue(new mssg("Welcome","0",date.getTime(),FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                    dialog.cancel();}
                else{
                    txtChannelName.setError("Invalid Channel Name");
                }
                txtChannelName.setText("");

            }
        });


    }

    public void checkifChannelExists(String name,final TextView t) {

        Query query = mref.child("Channels").orderByChild("name").equalTo(name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){
                        doSomething(true,t);
                    }else{
                        doSomething(false,t);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void doSomething(boolean b,TextView t) {
        if(b){
            t.setText("Channel Name not Available");
            t.setTextColor(Color.RED);
        }else{
            t.setText("Name Available!!");
            t.setTextColor(Color.GREEN);
        }
    }

}