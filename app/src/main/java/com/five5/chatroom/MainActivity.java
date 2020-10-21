package com.five5.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    RelativeLayout bottom;
    EditText txtPhone;
    ImageButton next;
    String phn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom =(RelativeLayout)findViewById(R.id.relBottom);
        txtPhone=(EditText)findViewById(R.id.txtPhone);
        next=(ImageButton)findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phn=txtPhone.getText().toString();
                if(phn.length()!=10){
                    txtPhone.setError("Invalid Number");
                    txtPhone.requestFocus();
                }else{
                Intent i=new Intent(getApplicationContext(),otp.class);
                i.putExtra("PhoneNumber",txtPhone.getText().toString());
                    finish();
                startActivity(i);
                }
            }
        });

    }


}