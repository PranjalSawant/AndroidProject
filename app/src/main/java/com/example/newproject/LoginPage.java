package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    EditText username, pass;
    Button login, signup;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        login.setOnClickListener(v -> loginUser());
        signup.setOnClickListener(v -> {
            Intent signIntent = new Intent(LoginPage.this, SignUpPage.class);
            startActivity(signIntent);
        });
    }

    void loginUser(){
        String email  = username.getText().toString();
        String pwd  = pass.getText().toString();


        boolean isValidated = validateData(email,pwd);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email,pwd);

    }
    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //login is success
                        //go to mainactivity
                        startActivity(new Intent(LoginPage.this,NotesPage.class));
                        finish();
//                    }else{
//                        Toast.makeText(LoginPage.this, "Email not verified", Toast.LENGTH_SHORT).show();
//                    }

                }else{
                    //login failed
                    Toast.makeText(LoginPage.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    void loginFB(String emailid, String pwd){
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.signInWithEmailAndPassword(emailid,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Intent gotomain = new Intent(LoginPage.this, NotesPage.class);
//                    startActivity(gotomain);
////                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
////                        Intent gotomain = new Intent(LoginPage.this, NotesPage.class);
////                        startActivity(gotomain);
////                    }else{
////                        Toast.makeText(LoginPage.this, "Email not  verified", Toast.LENGTH_SHORT).show();
////                    }
//                }else{
//                    Toast.makeText(LoginPage.this, "Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    boolean validateData(String email,String password){
        //validate the data that are input by user.

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            username.setError("Email is invalid");
            return false;
        }
        if(password.length()<6){
            pass.setError("Password length is invalid");
            return false;
        }
        return true;
    }
}
