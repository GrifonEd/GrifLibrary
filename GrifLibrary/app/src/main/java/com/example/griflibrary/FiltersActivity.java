package com.example.griflibrary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.griflibrary.databinding.ActivityFiltersBinding;

public class FiltersActivity extends AppCompatActivity {

    private ActivityFiltersBinding binding;

    String categoryFilter="",authorFilter="",yearFilter="",volumeFilter="",subctractionFilter="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFiltersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.SearchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        binding.SubsctractionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subtractionPickDialog();
            }
        });

        binding.VolumeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolumePickDialog();
            }
        });
        binding.YearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YearPickDialog();
            }
        });
    }

    private void YearPickDialog() {
        final String[] categoriesArray = new String[]{"Любой","До 1900 года","До 1945 года","До 2000 года","До 2010 года","До 2020 года","Новинки"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите год издания")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        String sub = categoriesArray[i];
                        binding.YearEdit.setText(sub);

                    }
                })
                .show();
    }

    private void VolumePickDialog() {
        final String[] categoriesArray = new String[]{"100","300","Любой"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите максимальный объем")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String sub = categoriesArray[i];
                        binding.VolumeEdit.setText(sub);

                    }
                })
                .show();
    }

    private void subtractionPickDialog() {

        final String[] categoriesArray = new String[]{"Есть","Отсутствует","Не важно"};


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Есть/нет доступ по подписке")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String sub = categoriesArray[i];
                        binding.SubsctractionEdit.setText(sub);

                    }
                })
                .show();
    }

    private void validateData() {
        /*
        if(TextUtils.isEmpty(categoryFilter)){
            Toast.makeText(this,"Enter your category...",Toast.LENGTH_SHORT).show();
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
        */
        categoryFilter = binding.CategoryEdit.getText().toString().trim();
        authorFilter = binding.AuthorEdit.getText().toString().trim();
        yearFilter = binding.YearEdit.getText().toString().trim();
        volumeFilter = binding.VolumeEdit.getText().toString().trim();
        subctractionFilter = binding.SubsctractionEdit.getText().toString().trim();

        Intent intent1 = new Intent(FiltersActivity.this,DashboardUserActivity.class);
        intent1.putExtra("categoryFilter",categoryFilter);
        intent1.putExtra("authorFilter",authorFilter);
        intent1.putExtra("yearFilter",yearFilter);
        intent1.putExtra("volumeFilter",volumeFilter);
        intent1.putExtra("subctractionFilter",subctractionFilter);
        startActivity(intent1);

    }
}