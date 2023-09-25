package com.example.griflibrary;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;

public class MyApplication  extends Application {

    private static final String TAG_DOWNLOAD = "DOWNLOAD_TAG";
    public static final long MAX_BYTES_PDF = 50000000;

    public static void loadPdfFromUrl(String s, String s1, PDFView pdfView, ProgressBar progressBar) {

    }

    public static void loadPdfFromUrlSinglePage(String pdfUrl, final String ptfTitle, final PDFView pdfView, final ProgressBar progressBar) {
        final String TAG = "PDF_LOAD_SINGLE_TAG";

        //String pdfUrl = model.getUrl();
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d(TAG, "onSuccess: " + ptfTitle + " successfully got the file");

                        pdfView.fromBytes(bytes)
                                .pages(0)
                                .swipeVertical(false)
                                .enableSwipe(false)
                                .onError(new OnErrorListener() {
                                    @Override
                                    public void onError(Throwable t) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d(TAG, "onError: " + t.getMessage());
                                    }
                                })
                                .onLoad(new OnLoadCompleteListener() {
                                    @Override
                                    public void loadComplete(int nbPages) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d(TAG, "LoadComplete: pdf loaded");
                                    }
                                })
                                .load();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "onFailure: failed getting file  from url due to" + e.getMessage());

                    }
                });


    }


    public static void loadPdfSize(String pdfUrl, final String pdfTitle, final TextView sizeTv) {
        final String TAG = "PDF_SIZE_TAG";

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getMetadata()
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        double bytes = storageMetadata.getSizeBytes();
                        Log.d(TAG, "onSuccess: " + pdfTitle + " " + bytes);

                        double kb = bytes / 1024;
                        double mb = kb / 1024;
                        if (mb >= 1) {
                            sizeTv.setText(String.format("%.2f", mb) + " MB");
                        } else if (kb >= 1) {
                            sizeTv.setText(String.format("%.2f", kb) + " KB");

                        } else {
                            sizeTv.setText(String.format("%.2f", bytes) + " bytes");

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });

    }

    public static void loadCategory(String category, final TextView categoryTv,final TextView ratingTv) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(category)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String category = "" + snapshot.child("category").getValue();

                        Random a = new Random();
                        Double a1 = (a.nextDouble()+4);
                        String rating = a1.toString();
                        ratingTv.setText(rating);
                        categoryTv.setText(category);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public static void incrementBookViewCount(final String bookId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String viewsCount = "" + snapshot.child("viewsCount").getValue();

                        if (viewsCount.equals("") || viewsCount.equals("null")) {
                            viewsCount = "0";
                        }

                        long newViewsCount = Long.parseLong(viewsCount) + 1;

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("viewsCount", newViewsCount);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Books");
                        reference.child(bookId)
                                .updateChildren(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public static void incrementBooksReaded(final String userId, final String bookId) {


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
                        String viewsCount = "" + snapshot.getChildrenCount();

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





    public static void downloadBook(final Context context, final String bookId, String bookTitle, String bookUrl){

        final String nameWithExtension = bookTitle +".pdf";
        final ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setTitle("Please wait");

        progressDialog.setMessage("Downloading "+nameWithExtension +"...");

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(bookUrl);
        storageReference.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        saveDownloadedBook(context,progressDialog,bytes,nameWithExtension,bookId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context,"Failed to download due to "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static void saveDownloadedBook(Context context, ProgressDialog progressDialog, byte[] bytes, String nameWithExtension, String bookId) {
        try {
            File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            downloadFolder.mkdirs();

            String filepath = downloadFolder.getPath()+"/" + nameWithExtension;

            FileOutputStream out = new FileOutputStream(filepath);
            out.write(bytes);
            out.close();

            Toast.makeText(context,"Saved to Download Fold",Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        }
        catch (Exception e){
            Toast.makeText(context,"Failed saving to Download Folder due to"+e.getMessage(),Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

        }
    }


    public void onCreate(){
        super.onCreate();
    }

    public static  final  String formatTimestamp(long timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        return date;
       // String date = DateFormat.
    }


}
