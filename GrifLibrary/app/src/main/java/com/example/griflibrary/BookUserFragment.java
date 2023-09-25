package com.example.griflibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.griflibrary.databinding.FragmentBookUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookUserFragment extends Fragment {
    private String categoryFilter1="";
    private String authorFilter1="";
    private String yearFilter1="";
    private int yearFilterMax;
    private int yearFilterMin;

    private String volumeFilter1="";
    private int volumeFilter2=0;
    private String subctractionFilter1="";
    private String subctractionFilter2="";
   private String categoryId;
    private String category;
    private String uid;
    private ArrayList<ModelPdf> pdfArrayList;
    private AdapterPdfUser adapterPdfUser;

    private FragmentBookUserBinding binding;

    private  static final  String TAG = "BOOKS_USER_TAG";

    public BookUserFragment() {
        // Required empty public constructor
    }


    public static BookUserFragment newInstance(String categoryId, String category,String uid,String categoryFilter,String authorFilter,String yearFilter,String volumeFilter,String subctractionFilter) {
        BookUserFragment fragment = new BookUserFragment();
        Bundle args = new Bundle();
        args.putString("categoryFilter",categoryFilter);
        args.putString("authorFilter",authorFilter);
        args.putString("yearFilter",yearFilter);
        args.putString("volumeFilter",volumeFilter);
        args.putString("subctractionFilter",subctractionFilter);
        args.putString("categoryId", categoryId);
        args.putString("category", category);
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            category = getArguments().getString("category");
            uid = getArguments().getString("uid");
            categoryFilter1 = getArguments().getString("categoryFilter");
            authorFilter1 = getArguments().getString("authorFilter");
            yearFilter1 = getArguments().getString("yearFilter");
            if(yearFilter1.equals("Любой")) {
                yearFilterMax = 10000;
                yearFilterMin = 0;
            }
            if(yearFilter1.equals("До 1900 года")) {
                yearFilterMax = 1900;
                yearFilterMin = 0;
            }
            if(yearFilter1.equals("До 1945 года")) {
                yearFilterMax = 1945;
                yearFilterMin=1900;
            }
            if(yearFilter1.equals("До 2000 года")) {
                yearFilterMax = 2000;
                yearFilterMin = 1945;
            }
            if(yearFilter1.equals("До 2010 года")) {
                yearFilterMax = 2010;
                yearFilterMin = 2000;
            }
            if(yearFilter1.equals("До 2020 года")) {
                yearFilterMax = 2020;
                yearFilterMin = 2010;
            }
            if(yearFilter1.equals("Новинки")) {
                yearFilterMax = 2022;
                yearFilterMin = 2020;
            }
            if(yearFilter1.equals("")) {
                yearFilterMax = 10000;
                yearFilterMin = 0;
            }

            volumeFilter1 = getArguments().getString("volumeFilter");
            if(volumeFilter1.equals("100"))
                volumeFilter2 = 100;
            if(volumeFilter1.equals("300"))
                volumeFilter2 = 300;
            if(volumeFilter1.equals("Любой"))
                volumeFilter2 = 100000;
            if(volumeFilter1.equals(""))
                volumeFilter2 = 100000;
            subctractionFilter1 = getArguments().getString("subctractionFilter");
            if(subctractionFilter1.equals(""))
                subctractionFilter1 ="Не важно";

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentBookUserBinding.inflate(LayoutInflater.from(getContext()),container,false);

        Log.d(TAG,"onCreateView: "+category);
        if (category.equals("All")){
            loadAllBooks();
        }
        else {
            loadCategorizedBooks();
        }

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try{
                    adapterPdfUser.getFilter().filter(charSequence);
                }
                catch (Exception e){
                    Log.d(TAG,"onTetChanged: search"+e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getContext(),FiltersActivity.class);
                //intent1.putExtra("url",bookId);
                startActivity(intent1);
            }
        });

        return binding.getRoot();
    }

    private void loadAllBooks() {
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pdfArrayList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelPdf model = ds.getValue(ModelPdf.class);

                    if(subctractionFilter1.equals("Не важно"))
                        if(model.category.toUpperCase().contains(categoryFilter1.toUpperCase()) &&
                                model.author.toUpperCase().contains(authorFilter1.toUpperCase())&&
                                Integer.parseInt(model.year)>=yearFilterMin&&
                                Integer.parseInt(model.year)<=yearFilterMax&&
                                Integer.parseInt(model.volume)<volumeFilter2)
                            pdfArrayList.add(model);
                        else;
                    else
                    if((model.category.toUpperCase().contains(categoryFilter1.toUpperCase()) && model.author.toUpperCase().contains(authorFilter1.toUpperCase())&& model.subscription.toUpperCase().equals(subctractionFilter1.toUpperCase())&&Integer.parseInt(model.year)>=yearFilterMin&&Integer.parseInt(model.year)<=yearFilterMax&&Integer.parseInt(model.volume)<volumeFilter2))
                    pdfArrayList.add(model);

                }
                adapterPdfUser = new AdapterPdfUser(getContext(),pdfArrayList);

                binding.booksRv.setAdapter(adapterPdfUser);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private  void loadCategorizedBooks(){
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.orderByChild("category").equalTo(category)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                pdfArrayList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelPdf model = ds.getValue(ModelPdf.class);

                    pdfArrayList.add(model);
                }
                adapterPdfUser = new AdapterPdfUser(getContext(),pdfArrayList);

                binding.booksRv.setAdapter(adapterPdfUser);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}