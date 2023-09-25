package com.example.griflibrary;

import android.view.LayoutInflater;
import android.widget.Filter;



import java.util.ArrayList;

public class FilterCategory extends Filter {

    ArrayList<ModelCategory> filterList;

    AdapterCategory adapterCategory;


    public FilterCategory(ArrayList<ModelCategory> filterList,AdapterCategory adapterCategory) {
        this.adapterCategory = adapterCategory;
        this.filterList = filterList;
    }


    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        if(charSequence!= null && charSequence.length()>0){


            charSequence = charSequence.toString().toUpperCase();
            ArrayList<ModelCategory> filterModels = new ArrayList<>();
            for(int i =0;i<filterList.size();i++){
                if(filterList.get(i).getCategory().toUpperCase().contains(charSequence)){
                    filterModels.add(filterList.get(i));
                }
            }
            results.count=filterModels.size();
            results.values = filterModels;
        }
        else{
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapterCategory.categoryArrayList = (ArrayList<ModelCategory>)filterResults.values;

        adapterCategory.notifyDataSetChanged();
    }
}
