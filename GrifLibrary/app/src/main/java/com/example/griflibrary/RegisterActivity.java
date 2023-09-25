package com.example.griflibrary;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.griflibrary.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateData();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private  String name = "",email = "",password = "";
    private void validateData(){
     name = binding.nameEt.getText().toString().trim();
     email = binding.emailEt.getText().toString().trim();
     password = binding.passwordEt.getText().toString().trim();
     String cPassword = binding.cPasswordEt.getText().toString().trim();

     if(TextUtils.isEmpty(name)){
         Toast.makeText(this,"Enter your name...",Toast.LENGTH_SHORT).show();
     }
     else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
         Toast.makeText(this,"Invalid email adress!",Toast.LENGTH_SHORT).show();

     }
     else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter password...",Toast.LENGTH_SHORT).show();

        }
     else if(TextUtils.isEmpty(cPassword)){
         Toast.makeText(this,"Confirm password!",Toast.LENGTH_SHORT).show();

     }
     else if(!password.equals((cPassword))){
         Toast.makeText(this,"Password doesn't match!",Toast.LENGTH_SHORT).show();

     }
     else {
         createUserAccount();
     }
    }

    private void createUserAccount() {

        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            updateUserInfo();
                        }
                        else
                            {
                                String message = task.getException().toString();
                            Toast.makeText(RegisterActivity.this,"Error"+message,Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                        }
                    }
                });

/*

/*
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

 */




    }

    private void updateUserInfo() {
        progressDialog.setMessage("Saving user info...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();

        String uid = firebaseAuth.getCurrentUser().getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("profileImage", "");
        hashMap.put("userType", "user");
        hashMap.put("timestamp", timestamp);
        hashMap.put("lovelyCategory","");
        hashMap.put("lovelyAuthor","");
        hashMap.put("points",0);
        hashMap.put("subscription","Нет");
        hashMap.put("booksCount",0);



        ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this,"Account created...",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,DashboardUserActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}