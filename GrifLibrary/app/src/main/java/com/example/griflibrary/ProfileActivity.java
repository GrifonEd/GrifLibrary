package com.example.griflibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.griflibrary.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();

        binding.profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,ProfileEditActivity.class));
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = ""+snapshot.child("email").getValue();
                        String name = ""+snapshot.child("name").getValue();
                        String profileImage = ""+snapshot.child("profileImage").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();
                        String uid = ""+snapshot.child("uid").getValue();
                        String lovelyCategory = ""+snapshot.child("lovelyCategory").getValue();
                        String lovelyAuthor = ""+snapshot.child("lovelyAuthor").getValue();
                        String points = ""+snapshot.child("points").getValue();
                        if(points.equals("null"))
                            points="90";
                        String subscription = ""+snapshot.child("subscription").getValue();
                        if(subscription.equals("")||subscription.equals("null"))
                            subscription="Отсутствует";
                        String booksCount = ""+snapshot.child("booksCount").getValue();
                        if(booksCount.equals("null"))
                            booksCount="9";



                        String formattedData = MyApplication.formatTimestamp(Long.parseLong(timestamp));

                        binding.emailTv.setText(email);
                        binding.nameTv.setText(name);
                        binding.memberData.setText(formattedData);
                        binding.lovelyAuthor.setText(lovelyAuthor);
                        binding.lovelyCategory.setText(lovelyCategory);
                        binding.havePodpiska.setText(subscription);
                        binding.numberBall.setText(points);
                        binding.booksReaded.setText(booksCount);

                        Glide.with(ProfileActivity.this)
                        .load(profileImage)
                                .placeholder(R.drawable.ic_person_grey)
                                .into(binding.profileTv);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}