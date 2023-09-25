package com.example.griflibrary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.griflibrary.databinding.ActivityRowPdfUserBinding;
import com.github.barteksc.pdfviewer.PDFView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class AdapterPdfUser extends  RecyclerView.Adapter<AdapterPdfUser.HolderPdfUser> implements Filterable {

    private Context context;
    public ArrayList<ModelPdf> pdfArrayList, filterList;

    private  FilterPdfUser filter;


    private ActivityRowPdfUserBinding binding;



    private  static final  String TAG = "ADAPTER_PDF_USER_TFG";

    public AdapterPdfUser(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filterList =pdfArrayList;
    }

    @NonNull
    @Override
    public HolderPdfUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ActivityRowPdfUserBinding.inflate(LayoutInflater.from(context),parent,false);

        return new HolderPdfUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfUser holder, int position) {
        ModelPdf model = pdfArrayList.get(position);
        final String bookId = model.getId();
        String title = model.getTitle();
        String description = model.getDescription();
        String category = model.getCategory();
        String categoryId = model.getCategoryId();
        String pdfUrl = model.getUrl();
        String author = model.getAuthor();
        String year = model.getYear();
        String volume = model.getVolume();

        long timestamp = model.getTimestamp();

      // String date =
        DecimalFormat df = new DecimalFormat("#.#");
        Random a = new Random();
        Double a1 = (a.nextDouble()+4);
        //String rating1 = String.format("%.1f", a1.toString());
        holder.ratingTv.setText(df.format(a1));
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dataTv.setText(author);
        holder.categoryTv.setText(category);
        //holder.titleTv.setText(title);


        MyApplication.loadPdfFromUrlSinglePage(""+pdfUrl,""+title,holder.pdfView,holder.progressBar);
        //MyApplication.loadCategory(""+category,holder.categoryTv);
        MyApplication.loadPdfSize(""+pdfUrl,""+title,holder.sizeTv);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PdfDetailActivity.class) ;
                intent.putExtra("bookId", bookId);
                intent.putExtra("rating", df.format(a1));
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter = new FilterPdfUser(filterList,this);
        }
        return filter;
    }

    class HolderPdfUser extends RecyclerView.ViewHolder{


        TextView titleTv, descriptionTv,categoryTv,sizeTv,dataTv,ratingTv;
        ProgressBar progressBar;
        PDFView pdfView;
        public HolderPdfUser(@NonNull View itemView) {
            super(itemView);

            ratingTv =binding.rating;
            titleTv = binding.titleTv;
            descriptionTv = binding.decriptionTv;
            categoryTv = binding.categoryTv;
            sizeTv = binding.sizeTv;
            dataTv = binding.dataTv;
            pdfView = binding.pdfView;
            progressBar = binding.progressBar;
        }
    }

}

