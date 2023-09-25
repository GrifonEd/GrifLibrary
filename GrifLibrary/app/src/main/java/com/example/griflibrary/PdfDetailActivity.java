package com.example.griflibrary;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import com.example.griflibrary.databinding.ActivityPdfDetailBinding;
import com.example.griflibrary.databinding.ActivityPdfViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PdfDetailActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    private ActivityPdfDetailBinding binding;

    private boolean podpiskaUser=false;
    private boolean podpiskaBook=false;
    String bookId,bookTitle,bookUrl,rating;
    private static final int REQUEST_WRITE_PHONE_STATE = 10001;
    private  static final String TAG_Download = "DOWNLOAD_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        rating = intent.getStringExtra("rating");

        binding.downloadBookBtn.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        loadBookDetails();

        MyApplication.incrementBookViewCount(bookId);

        binding.backBtn3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                onBackPressed();
            }
        });

        binding.readBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("Users");
                reference4.child(firebaseAuth.getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 if( snapshot.child("subscription").getValue().equals("Есть")) {
                                     podpiskaUser = true;
                                 }
                                else {
                                    podpiskaUser = false;
                                }
                            }
                            @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                DatabaseReference reference5 = FirebaseDatabase.getInstance().getReference("Books");
                reference5.child(bookId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String what = "" + snapshot.child("subscription").getValue().toString();
                                String what1 = "" + snapshot.child("subscription").getValue().toString();
                                if( snapshot.child("subscription").getValue().equals("Есть")) {
                                    podpiskaBook = true;
                                    if(podpiskaUser == true) {
                                        Intent intent1 = new Intent(PdfDetailActivity.this, PdfViewActivity.class);
                                        intent1.putExtra("bookId", bookId);
                                        intent1.putExtra("title",bookTitle );
                                        //intent1.putExtra("url",bookId);
                                        startActivity(intent1);
                                    }
                                    else if(podpiskaUser == false && podpiskaBook == true)
                                        Toast.makeText(PdfDetailActivity.this,"У вас нет подписки",Toast.LENGTH_SHORT).show();
                                    else if(podpiskaBook == false && podpiskaUser == false){
                                        Intent intent1 = new Intent(PdfDetailActivity.this, PdfViewActivity.class);
                                        intent1.putExtra("bookId", bookId);
                                        intent1.putExtra("title",bookTitle );
                                        //intent1.putExtra("url",bookId);
                                        startActivity(intent1);
                                    }
                                }
                                else {
                                    podpiskaBook = false;
                                    if(podpiskaUser == true) {
                                        Intent intent1 = new Intent(PdfDetailActivity.this, PdfViewActivity.class);
                                        intent1.putExtra("bookId", bookId);
                                        intent1.putExtra("title",bookTitle );
                                        //intent1.putExtra("url",bookId);
                                        startActivity(intent1);
                                    }
                                    else if(podpiskaUser == false && podpiskaBook == true)
                                        Toast.makeText(PdfDetailActivity.this,"У вас нет подписки",Toast.LENGTH_SHORT).show();
                                    else if(podpiskaBook == false && podpiskaUser == false){
                                        Intent intent1 = new Intent(PdfDetailActivity.this, PdfViewActivity.class);
                                        intent1.putExtra("bookId", bookId);
                                        intent1.putExtra("title",bookTitle );
                                        //intent1.putExtra("url",bookId);
                                        startActivity(intent1);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



            }
        });

        if (isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
          // Toast.makeText(this, "Разрешения есть, можно работать", Toast.LENGTH_SHORT).show();
        } else {
            // иначе запрашиваем разрешение у пользователя
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 10001);
        }




        binding.downloadBookBtn.setOnClickListener(new View.OnClickListener() {





            @Override
            public void onClick(View view) {

                DatabaseReference reference7 = FirebaseDatabase.getInstance().getReference("Users");
                reference7.child(firebaseAuth.getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if( snapshot.child("subscription").getValue().equals("Есть")) {
                                    podpiskaUser = true;
                                }
                                else {
                                    podpiskaUser = false;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                DatabaseReference reference8 = FirebaseDatabase.getInstance().getReference("Books");
                reference8.child(bookId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String what = "" + snapshot.child("subscription").getValue().toString();
                                String what1 = "" + snapshot.child("subscription").getValue().toString();
                                if( snapshot.child("subscription").getValue().equals("Есть")) {
                                    podpiskaBook = true;
                                    if(podpiskaUser == true) {
                                        Intent intent1 = new Intent(PdfDetailActivity.this, PdfViewActivity.class);
                                        intent1.putExtra("bookId", bookId);
                                        intent1.putExtra("title",bookTitle );
                                        //intent1.putExtra("url",bookId);
                                        startActivity(intent1);
                                    }
                                    else if(podpiskaUser == false && podpiskaBook == true)
                                        Toast.makeText(PdfDetailActivity.this,"У вас нет подписки",Toast.LENGTH_SHORT).show();
                                    else if(podpiskaBook == false && podpiskaUser == false){
                                        Intent intent1 = new Intent(PdfDetailActivity.this, PdfViewActivity.class);
                                        intent1.putExtra("bookId", bookId);
                                        intent1.putExtra("title",bookTitle );
                                        //intent1.putExtra("url",bookId);
                                        startActivity(intent1);
                                    }
                                }
                                else {
                                    podpiskaBook = false;
                                    if(podpiskaUser == true) {
                                        if(ContextCompat.checkSelfPermission(PdfDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                                            MyApplication.downloadBook(PdfDetailActivity.this,""+bookId,""+bookTitle,""+bookUrl);
                                        }
                                        else{
                                            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                        }
                                    }
                                    else if(podpiskaUser == false && podpiskaBook == true)
                                        Toast.makeText(PdfDetailActivity.this,"У вас нет подписки",Toast.LENGTH_SHORT).show();
                                    else if(podpiskaBook == false && podpiskaUser == false){
                                        if(ContextCompat.checkSelfPermission(PdfDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                                            MyApplication.downloadBook(PdfDetailActivity.this,""+bookId,""+bookTitle,""+bookUrl);
                                        }
                                        else{
                                            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });




            }
        });



        /*
        binding.readBookBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent1 = new Intent(PdfDetailActivity.this,PdfViewActivity.class);
            }
        });


 */
    }

        private void requestPermission(String permission, int requestCode) {
            // запрашиваем разрешение
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }

    private boolean isPermissionGranted(String permission) {
        // проверяем разрешение - есть ли оно у нашего приложения
        int permissionCheck = ActivityCompat.checkSelfPermission(this, permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }








private ActivityResultLauncher<String> requestPermissionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
            if(isGranted){
                MyApplication.downloadBook(this,""+bookId,""+bookTitle,""+bookUrl);
            }
            else{
                Toast.makeText(this,"Permission was denied...",Toast.LENGTH_SHORT).show();
            }
        });

    private void loadBookDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookTitle = ""+snapshot.child("title").getValue();
                        String description = ""+snapshot.child("description").getValue();
                        String categoryId = ""+snapshot.child("categoryId").getValue();
                        String category = ""+snapshot.child("category").getValue();
                        String author = ""+snapshot.child("author").getValue();
                        String year = ""+snapshot.child("year").getValue();
                        String volume = ""+snapshot.child("volume").getValue();
                        String viewsCount = ""+snapshot.child("viewsCount").getValue();
                        if(viewsCount.equals("null")) {
                            viewsCount = "0";
                        }
                        String downloadsCount = ""+snapshot.child("downloadsCount").getValue();
                        bookUrl = ""+snapshot.child("url").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();

                        binding.downloadBookBtn.setVisibility(View.VISIBLE);

                        MyApplication.loadPdfFromUrlSinglePage(""+bookUrl,""+bookTitle,binding.pdfView,binding.progressBar2);
                       // MyApplication.loadCategory(""+category,binding.categoryTv);
                        MyApplication.loadPdfSize(""+bookUrl,""+bookTitle,binding.sizeTv1);

                        binding.rating.setText(rating);
                        binding.titleTv.setText(bookTitle);
                        binding.categoryTv.setText(category);
                        binding.dateTv.setText(year);
                        binding.authorTv.setText(author);
                        binding.volumeTv.setText(volume);
                        binding.viewsTv.setText(viewsCount);

                        binding.descriptionTv.setText(description);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkUser(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        //if(firebaseUser.getUid()==ref.child("subtraction"))
    }
}