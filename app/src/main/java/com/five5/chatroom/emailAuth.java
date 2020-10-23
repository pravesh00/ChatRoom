package com.five5.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.five5.chatroom.Data.SubChannel;
import com.five5.chatroom.Data.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class emailAuth extends AppCompatActivity {
    TextView txtRegister;
    EditText txtPass,txtEmail;
    ImageButton btnLogin;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);
        intializeUI();
        mAuth=FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        FirebaseUser mcur= mAuth.getCurrentUser();
        if(!(mcur ==null)){
            startN();

        }
    }

    private void register() {
        String pass=txtPass.getText().toString();
        String email=txtEmail.getText().toString();
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Registered Succesfully",Toast.LENGTH_LONG).show();
                    addUser();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void addUser() {
        //HashMap<String,SubChannel> subChannels = new HashMap<>();
        //subChannels.put("yytdtuuu",new SubChannel("ChatRoom"));
        user newUser =new user();
        newUser.setEmail(txtEmail.getText().toString());
        newUser.setPass(txtPass.getText().toString());
        //newUser.setChnls(subChannels);
        final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mRef.push().setValue(newUser);
        Query q=mRef.orderByChild("email").equalTo(newUser.getEmail());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s:snapshot.getChildren()){
                    mRef.child(s.getKey()).child("chnls").push().setValue(new SubChannel(""));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void login() {
        String pass=txtPass.getText().toString();
        String email=txtEmail.getText().toString();
        if(!pass.isEmpty() && !email.isEmpty()){
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Login Succesfull",Toast.LENGTH_LONG).show();
                        startN();

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        else{
            txtEmail.setError("wrong email");
            txtPass.setError("wrong password");
        }
    }

    private void startN() {
        Intent i= new Intent(this,subscribedChannels.class);
        i.putExtra("email",mAuth.getCurrentUser().getEmail());
        startActivity(i);
        finish();
    }

    private void intializeUI() {
        txtEmail =(EditText)findViewById(R.id.txtEmail);
        txtPass=(EditText)findViewById(R.id.txtPass);
        btnLogin=(ImageButton)findViewById(R.id.btnLogin);
        txtRegister=(TextView)findViewById(R.id.txtRegister);
    }
}