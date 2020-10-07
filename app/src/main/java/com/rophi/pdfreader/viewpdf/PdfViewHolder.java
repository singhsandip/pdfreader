package com.rophi.pdfreader.viewpdf;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rophi.pdfreader.R;

public class PdfViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgv;

    public PdfViewHolder(@NonNull View itemView) {
        super(itemView);

        imgv = itemView.findViewById(R.id.imgv);
    }
}
