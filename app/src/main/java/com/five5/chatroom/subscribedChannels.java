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
import com.five5.chatroom.Adapter.searchAdapter;
import com.five5.chatroom.Data.SubChannel;
import com.five5.chatroom.Data.chnnl;
import com.five5.chatroom.Data.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class subscribedChannels extends AppCompatActivity {
    RecyclerView mChannel;
    ArrayList<chnnl> channels= new ArrayList<>();
    DatabaseReference mref;
    ChannelAdapter mAdapter;
    LinearLayoutManager manager = new LinearLayoutManager(this);
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


        searchView=(SearchView) findViewById(R.id.search);
        mAdapter = new ChannelAdapter(channels,getApplicationContext(),userEmail,searchView);
        tool = (Toolbar)findViewById(R.id.toolbar);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.changeLayout(1);

                if(!newText.isEmpty())

                {Query query = mref.child("Channels").orderByChild("name").startAt(newText).endAt(newText+"\uf8ff");
                    channels.clear();


                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        channels.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            chnnl c= snapshot1.getValue(chnnl.class);
                            Log.e("channel",c.getName());
                            channels.add(c);
                            mAdapter.notifyDataSetChanged();

                        }
                        Log.e("snap",snapshot.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });}
                else{refresh();
                mAdapter.notifyDataSetChanged();

                }

                return true;
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
        mref = FirebaseDatabase.getInstance().getReference();
        {refresh();
            {mAdapter.notifyDataSetChanged();}}

        mChannel = findViewById(R.id.channelRecyclerView);
        mChannel.setLayoutManager(manager);
        mChannel.setAdapter(mAdapter);
        channels.add(new chnnl("Shitaap","Hye"));
    }

    private void refresh() {

        mAdapter.changeLayout(0);
        channels.clear();
        Query q=mref.child("Users").orderByChild("email").equalTo(userEmail);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("Chnls",snapshot.getChildren().toString());
                for(DataSnapshot m: snapshot.getChildren()){
                    user u = m.getValue(user.class);
                    Log.e("mail",u.getEmail());
                    ArrayList<SubChannel> x= u.getChnls();
                    for(SubChannel c:x){
                        checkChannelandAdd(c.getName());
                        Log.e("name",c.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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

    private void checkChannelandAdd(String n) {
        Query query = mref.child("Channels").orderByChild("name").equalTo(n);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //channels.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    chnnl c= snapshot1.getValue(chnnl.class);
                    Log.e("channel",c.getName());
                    channels.add(c);



                }
                Log.e("snap",snapshot.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}