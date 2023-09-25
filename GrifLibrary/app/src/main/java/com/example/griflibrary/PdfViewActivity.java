package com.example.griflibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.griflibrary.databinding.ActivityPdfViewBinding;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class PdfViewActivity extends AppCompatActivity {

    private ActivityPdfViewBinding binding;

    private String bookId,title;

    public static final long MAX_BYTES_PDF = 50000000;

    private static final String TAG = "PDF_VIEW_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        title = intent.getStringExtra("title");
        Log.d(TAG,"OnCreate: BookId: "+bookId);

        loadBookDetails();

        binding.BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadBookDetails() {
        Log.d(TAG,"LoadBookDetails: Get Pdf URL...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                       String pdfUrl = ""+snapshot.child("url").getValue();
                       String author = ""+snapshot.child("author").getValue();
                        Log.d(TAG,"LoadBookDetails: Get Pdf URL..."+pdfUrl);

                       loadBookFromUrl(pdfUrl);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private boolean increment=false;
    private void loadBookFromUrl(String pdfUrl) {
        Log.d(TAG,"LoadBookFromUrl: Get PDF from storage");
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        reference.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        binding.pdfVIew.fromBytes(bytes)
                                .swipeVertical(true)
                                .onPageChange(new OnPageChangeListener() {
                                    @Override
                                    public void onPageChanged(int page, int pageCount) {
                                        int currentPage = (page );
                                        binding.toolBarTitleTv.setText(title);
                                        binding.toolbarSubtitle.setText(currentPage+"/"+pageCount);
                                        Log.d(TAG,"OnPageChanged: "+currentPage+"/"+pageCount);
                                        if(currentPage==pageCount){
                                            increment=true;
                                        }
                                        if(increment==true){
                                            String userId = FirebaseAuth.getInstance().getUid();
                                            HashMap<String, Object> hashMap1 = new HashMap<>();
                                        hashMap1.put("booksReading", bookId);

                                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/booksReading");

                                        ref1.child(bookId)
                                                .setValue(hashMap1)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(Exception e) {

                                                    }
                                                });
                                        // if(bookId!=)

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+userId);
                                        ref.child("booksReading")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        String viewsCount = "" + (snapshot.getChildrenCount()+9);

                                                        if (viewsCount.equals("") || viewsCount.equals("null")) {
                                                            viewsCount = "0";
                                                        }



                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("booksCount", viewsCount);

                                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users/"+userId);
                                                        reference
                                                                .updateChildren(hashMap);

                                                        Long points = (snapshot.getChildrenCount() * 10 + 90);
                                                        HashMap<String, Object> hashMap2 = new HashMap<>();
                                                        hashMap2.put("points", points);

                                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users/"+userId);
                                                        reference1
                                                                .updateChildren(hashMap2);


                                                        String subscription;
                                                        if(points>=100)
                                                            subscription = "Есть";
                                                        else{
                                                            subscription = "Отсутствует";
                                                        }
                                                        HashMap<String, Object> hashMap3 = new HashMap<>();
                                                        hashMap3.put("subscription", subscription);

                                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Users/"+userId);
                                                        reference3
                                                                .updateChildren(hashMap3);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                    }
                                    }
                                })
                                .onError(new OnErrorListener() {
                                    @Override
                                    public void onError(Throwable t) {
                                        Toast.makeText(PdfViewActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .load();
                        binding.progressBar.setVisibility(View.GONE);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                });
    }
}