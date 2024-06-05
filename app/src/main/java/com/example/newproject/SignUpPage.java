package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPage extends AppCompatActivity {
EditText emailid, pwd, cpwd;
Button signup, loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailid = findViewById(R.id.emailid);
        pwd = findViewById(R.id.pwd);
        cpwd = findViewById(R.id.cpwd);
        signup = findViewById(R.id.signup);
        loginLink = findViewById(R.id.loginlink);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpPage.this,LoginPage.class);
                startActivity(i);
            }
        });
        signup.setOnClickListener(v -> createAccount());

    }
    void createAccount(){
        String email = emailid.getText().toString();
        String passw = pwd.getText().toString();
        String confirmpass = cpwd.getText().toString();
        boolean isValidated = validateData(email,passw,confirmpass);
        if(!isValidated) return;

        createAccountInFB(email,passw);
    }
    void createAccountInFB(String email,String passw){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,passw).addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpPage.this, "Account Created! Check Email", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }else{
                    Toast.makeText(SignUpPage.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //method to validate user info
    boolean validateData(String email,String passw,String confirmpass){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailid.setError("Email Invalid");
            return false;
        }
        if(passw.length()<6){
            pwd.setError("Password should only contain 6 characters");
            return false;
        }
        if(!passw.equals(confirmpass)){
            cpwd.setError("Password not matched");
            return false;
        }
        return true;
    }
}