package com.example.griflibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.griflibrary.databinding.ActivityPdfAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;

public class PdfAddActivity extends AppCompatActivity {


    private ActivityPdfAddBinding binding;

    private FirebaseAuth firebaseAuth;

    private  static final  int PDF_PICK_CODE = 1000;

    private Uri pdfUri = null;
    private ProgressDialog progressDialog;

    private ArrayList<ModelCategory> categoryArrayList;

    private static  final  String TAG = "ADD_PDF_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadPdfCategories();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfPickIntent();
            }
        });

        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryPickDialog();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private  String title = "", description ="", category = "",avtor= "",data= "",volume= "";
    boolean podpiska1 = false ;

    private void validateData() {

        Log.d(TAG,"ValodateData : validating Data...");

        title = binding.titleTil2.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();
        category = binding.categoryTv.getText().toString().trim();
        avtor = binding.avtor.getText().toString().trim();
        data = binding.data.getText().toString().trim();
        volume = binding.volume.getText().toString().trim();
        Switch podpiska2 = findViewById(R.id.podpiska);
        podpiska1 = false;
        podpiska2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    podpiska1 = true;
                } else {
                    podpiska1 = false;
                }
            }
        });

        if(TextUtils.isEmpty(title))
        {
            Toast.makeText(this,"Enter Title", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description))
        {
            Toast.makeText(this,"Enter description", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(category))
        {
            Toast.makeText(this,"Enter category", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(avtor))
        {
            Toast.makeText(this,"Enter avtor", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(data))
        {
            Toast.makeText(this,"Enter data", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(volume))
        {
            Toast.makeText(this,"Enter volume", Toast.LENGTH_SHORT).show();

        }
        else if(pdfUri==null)
        {
            Toast.makeText(this,"pdfUri странный", Toast.LENGTH_SHORT).show();

        }
        else
            {
            uploadPdfToStorage();
        }

    }

    private void uploadPdfToStorage() {

        Log.d(TAG,"ValodateData : validating Data...");

        progressDialog.setMessage("Uploading Pdf...");
        progressDialog.show();

        final long timestamp = System.currentTimeMillis();

        String filePfthAndName = "Books/"+timestamp;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePfthAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG,"onSuccess: PDF uploaded to storage...");
                        Log.d(TAG,"onSuccess: getting pdf url...");

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadedPdfUrl = ""+uriTask.getResult();

                    uploadPdfInfoToDb(uploadedPdfUrl,timestamp);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"OnFailure : PDF upload failed due to"+e.getMessage());
                        Toast.makeText(PdfAddActivity.this,"PDF upload failed due to"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPdfInfoToDb(String uploadedPdfUrl, long timestamp) {
        Log.d(TAG,"uploadPdfToStorage : uploading Pdf info to firebase db...");

        progressDialog.setMessage("Uploading pdf info..");

    String uid = firebaseAuth.getUid();
String podpiska="";
    if(podpiska1==false)
        podpiska="Отсутствует";
    if(podpiska1==true)
        podpiska="Есть";
        HashMap<String,Object>hashMap = new HashMap<>();
        hashMap.put("uid",""+uid);
        hashMap.put("id",""+timestamp);
        hashMap.put("title",""+title);
        hashMap.put("description",""+description);
        hashMap.put("category",""+category);
        hashMap.put("author",""+avtor);
        hashMap.put("year",""+data);
        hashMap.put("volume",""+volume);
        hashMap.put("subscription",""+podpiska);
        hashMap.put("url",uploadedPdfUrl);
        hashMap.put("timestamp",timestamp);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Books");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onSuccess: Successfully uploaded...");
                        Toast.makeText(PdfAddActivity.this,"onSuccess: Successfully uploaded...",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onFailure: Failed to upload to db due to"+e.getMessage());
                        Toast.makeText(PdfAddActivity.this,"onFailure: Failed to upload to db due to",Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void loadPdfCategories() {
        Log.d(TAG,"LoadingCategories : Loading pdf categories...");
        categoryArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelCategory model = ds.getValue(ModelCategory.class);

                    categoryArrayList.add(model);

                    Log.d(TAG,"OnDataChange: "+model.getCategory());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void categoryPickDialog() {
        Log.d(TAG,"categoryPickDialog: showing category pick dialog");

        final String[] categoriesArray = new String[categoryArrayList.size()];
        for(int i =0;i<categoryArrayList.size();i++){
            categoriesArray[i] = categoryArrayList.get(i).getCategory();

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String category = categoriesArray[i];
                        binding.categoryTv.setText(category);

                        Log.d(TAG,"OnClick: Selected Category : "+category);
                    }
                })
        .show();
    }

    private void pdfPickIntent() {
        Log.d(TAG, "pdfPickIntent : starting pdf pick intent");

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_PICK_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == PDF_PICK_CODE)
            {
                Log.d(TAG,"OnActivityResult:PDF picker");

                pdfUri = data.getData();

                Log.d(TAG,"OnActivityResult: URI: "+pdfUri);
            }
        }
        else{
            Log.d(TAG,"OnActivityResult: cancelled picking pdf");
            Toast.makeText(this,"cancelled picking pdf",Toast.LENGTH_SHORT).show();

        }

    }
}