package com.rophi.pdfreader.viewpdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rophi.pdfreader.R;

import java.util.ArrayList;

public class PdfViewAdapter extends RecyclerView.Adapter<PdfViewHolder> {

    private Context context;
    private ArrayList<Bitmap> bitmapArrayList;
    private LayoutInflater inflater;

    public PdfViewAdapter(Context context, ArrayList<Bitmap> bitmapArrayList) {
        this.context = context;
        this.bitmapArrayList = bitmapArrayList;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PdfViewHolder(inflater.inflate(R.layout.item_pdf_page,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        holder.imgv.setImageBitmap(bitmapArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return (bitmapArrayList == null) ? 0 : bitmapArrayList.size();
    }
}
