package com.five5.chatroom;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otp_new extends AppCompatActivity {
    private static final String TAG = "";
    ImageButton btnBack;
    String phn;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseAuth mAuth;
    String verificationId;
    EditText otp;
    ImageButton ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phn = getIntent().getStringExtra("PhoneNumber");
        mAuth = FirebaseAuth.getInstance();
        ok = (ImageButton) findViewById(R.id.btnNew);


        otp = (EditText) findViewById(R.id.txtOtp);
        setContentView(R.layout.activity_otp);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();

                if (code != null) {
                    otp.setText(code);
                    // verifyNumber(code);
                }
                Log.e("Code", code);

                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("Failed", e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                Log.d("Verify", s);
                Log.d("tokem", forceResendingToken.toString());
                Log.d("Tt", "onCodeSent: s - " + verificationId + " : t - " + forceResendingToken);

            }
        };
        sendVerification("+91" + phn);




        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i= new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(i);
                verifyNumber(otp.getText().toString());

            }
        });

    }

    private void sendVerification(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks

        );
    }

    private void verifyNumber(String num) {
        PhoneAuthCredential ph = PhoneAuthProvider.getCredential(verificationId, num);
        mAuth.signInWithCredential(ph);
        mAuth.signInWithCredential(ph).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), LinkPage.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Login Unsuccesful", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("Phone",phn);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        phn=savedInstanceState.getString("Phone");
    }
}
