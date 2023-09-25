package com.example.griflibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.View;

import com.example.griflibrary.databinding.ActivityDashboardUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class DashboardUserActivity extends AppCompatActivity {

    public ArrayList<ModelCategory> categoryArrayList;
    public ViewPagerAdapter viewPagerAdapter;

    private ActivityDashboardUserBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments  = getIntent().getExtras();
        String  categoryFilter = getIntent().getStringExtra("categoryFilter");
        String  authorFilter = getIntent().getStringExtra("authorFilter");
        String  yearFilter = getIntent().getStringExtra("yearFilter");
        String  volumeFilter = getIntent().getStringExtra("volumeFilter");
        String  subctractionFilter = getIntent().getStringExtra("subctractionFilter");

        if(categoryFilter==null)
            categoryFilter="";
        if(authorFilter==null)
            authorFilter="";
        if(yearFilter==null)
            yearFilter="";
        if(volumeFilter==null)
            volumeFilter="";
        if(subctractionFilter==null)
            subctractionFilter="Не важно";



        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        setupViewPagerAdapter(binding.viewPager,categoryFilter,authorFilter,yearFilter,volumeFilter,subctractionFilter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardUserActivity.this,ProfileActivity.class));
            }
        });

    }

    private void setupViewPagerAdapter(final ViewPager viewPager,String categoryFilter,String authorFilter,String yearFilter,String volumeFilter,String subctractionFilter){
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this);

        categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                categoryArrayList.clear();

                ModelCategory modelAll = new ModelCategory("01","All","",1);
                categoryArrayList.add(modelAll);
                viewPagerAdapter.addFragment(BookUserFragment.newInstance(
                        ""+modelAll.getId(),
                        ""+modelAll.getCategory(),
                        ""+modelAll.getUid(),categoryFilter,authorFilter,yearFilter,volumeFilter,subctractionFilter
                ),modelAll.getCategory());

                viewPagerAdapter.notifyDataSetChanged();

                for(DataSnapshot ds:snapshot.getChildren()){
                    ModelCategory model = ds.getValue(ModelCategory.class);
                    categoryArrayList.add(model);

                    viewPagerAdapter.addFragment(BookUserFragment.newInstance(""+model.getId(),""+model.getCategory(),""+model.getUid(),categoryFilter,authorFilter,yearFilter,volumeFilter,subctractionFilter),""+model.getCategory());

                    viewPagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


         viewPager.setAdapter(viewPagerAdapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<BookUserFragment> fragmentList = new ArrayList<>();

        private Context context;

        private ArrayList<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior,Context context) {
            super(fm, behavior);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        private void addFragment(BookUserFragment fragment,String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);

        }
    }

    private void checkUser(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else{
            String email = firebaseUser.getEmail() ;

            binding.subTitleTv.setText(email);
        }
    }


}