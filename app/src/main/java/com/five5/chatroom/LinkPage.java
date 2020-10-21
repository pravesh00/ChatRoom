package com.five5.chatroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.five5.chatroom.Data.message;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class LinkPage extends AppCompatActivity {
    EditText createText,joinText,userText;
    Button createButton,joinButton,okButton;
    ProgressBar createPgr,joinPgr;
    FirebaseDatabase mDatabase;
    DatabaseReference dRef;
    String Username,ChatroomId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_page);
        intializeUI();
        read();
//        if(!ChatroomId.toString().isEmpty()){
  //          Intent i = new Intent(this,Output_Chatroom.class);
    //        i.putExtra("Name",ChatroomId.toString());
      //      i.putExtra("User",Username);
        //    startActivity(i);

        //}

        mDatabase =FirebaseDatabase.getInstance();
        dRef =mDatabase.getReference();
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPgr.setVisibility(View.VISIBLE);
                createLink();
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinPgr.setVisibility(View.VISIBLE);
                connectLink();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userText.getText().toString().isEmpty())
                {userText.setEnabled(false);
                    userText.setTextColor(Color.parseColor("#C5A4EB"));}
                else Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void connectLink() {
        String ChannelName =joinText.getText().toString();
        String user;
        if(userText.getText().toString().isEmpty()){
            user="RandomPeopleUser";
        }else user=userText.getText().toString();
        if(!ChannelName.isEmpty()){
            Intent i = new Intent(this,Output_Chatroom.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("Name",ChannelName);
            i.putExtra("User",user);
            
            startActivity(i);

            writeData(user,ChannelName);

            finish();
            joinText.setTextColor(Color.parseColor("#C5A4EB"));
            joinText.setEnabled(false);
        }


    }

    private void createLink() {
        if(!createText.getText().toString().isEmpty()){
            Date date = new Date();
            dRef.child(createText.getText().toString()).child("Messages").child(date.toString()).setValue(new message("1","","admin","Welcome To Chatroom"));
            createPgr.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"Channel created",Toast.LENGTH_LONG).show();
            createText.setEnabled(false);
            createButton.setText("Reset");
            createText.setTextColor(Color.parseColor("#C5A4EB"));
        }
    }

    private void intializeUI() {
        createText =(EditText) findViewById(R.id.txtCreateLin);
        joinText =(EditText) findViewById(R.id.txtCreateLin);
        createButton=(Button) findViewById(R.id.btnCreate);
        joinButton =(Button) findViewById(R.id.btnJoin);
        createPgr =(ProgressBar) findViewById(R.id.pgrCreate);
        joinPgr =(ProgressBar) findViewById(R.id.pgrJoin);
        userText=(EditText) findViewById(R.id.txtUse);
        okButton=(Button) findViewById(R.id.btnOK);

    }
    //read and store data in String var
    public void read(){
        String a=getData();
        try {
            final JSONObject aa=new JSONObject(a);
            Username=aa.getString("Name");
            ChatroomId=aa.getString("Id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //File to string convertor
    public String getData(){
        String a="";
        try {
            FileInputStream io= openFileInput("X");

            int c;
            while((c=io.read())!=-1){
                a=a+ (char) c;
            }
            io.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }
    //JSonarray to file convertor
    public void writeData(String n,String i) {
        JSONObject j=new JSONObject();


        try {
            j.put("Name",n);
            j.put("Id",i);
            FileOutputStream fi= openFileOutput("X",MODE_PRIVATE);
            fi.write(j.toString().getBytes());
            fi.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }}


}