package com.andrew.ecommute;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLogActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fbAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_log);

    fAuth =  FirebaseAuth.getInstance();

    fbAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null){
                Intent intent = new Intent(DriverLogActivity.this, DriverMapActivity.class);
                startActivity(intent);
                finish();
            }

        }
    };

    mEmail = (EditText)findViewById(R.id.email);
    mPassword = (EditText)findViewById(R.id.password);

    Button logIn = (Button) findViewById(R.id.login);
    Button drRegister = (Button) findViewById(R.id.register);

    drRegister.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(DriverLogActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(DriverLogActivity.this, "The email or password is wrong", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String user_id= fAuth.getCurrentUser().getUid();
                        DatabaseReference curr_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child("ID").child(user_id);
                        curr_user_db.setValue(email);
                    }

                }
            });
        }
    });
    logIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLogActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(DriverLogActivity.this, "The email or password is wrong", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    });

    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fbAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fAuth.removeAuthStateListener(fbAuthListener);
    }
}
