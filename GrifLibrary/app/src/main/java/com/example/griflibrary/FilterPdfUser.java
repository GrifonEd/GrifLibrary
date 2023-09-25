package com.example.griflibrary;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterPdfUser extends Filter {

    ArrayList<ModelPdf> filterList;

    AdapterPdfUser adapterPdfUser;

    public
    FilterPdfUser(ArrayList<ModelPdf> filterList, AdapterPdfUser adapterPdfUser) {
        this.filterList = filterList;
        this.adapterPdfUser = adapterPdfUser;
    }


    //,String categoryFilter,String authorFilter,String yearFilter,String volumeFilter,String subctractionFilter
    //&&filterList.get(i).getAuthor().toUpperCase().contains(authorFilter)&&filterList.get(i).getCategory().toUpperCase().contains(categoryFilter)&&filterList.get(i).getVolume().toUpperCase().contains(volumeFilter)&&filterList.get(i).getYear().toUpperCase().contains(yearFilter)&&filterList.get(i).getSubscription().toUpperCase().contains(subctractionFilter)
    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        if(charSequence!= null || charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<ModelPdf> filteredModels = new ArrayList<>();
            for(int i = 0;i<filterList.size();i++){
                if(filterList.get(i).getTitle().toUpperCase().contains(charSequence)){
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;

        }
        else{
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }




    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapterPdfUser.pdfArrayList = (ArrayList<ModelPdf>)filterResults.values;

        adapterPdfUser.notifyDataSetChanged();
    }
}
