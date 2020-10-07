package com.rophi.pdfreader.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rophi.pdfreader.R;

public class PdfHolder extends RecyclerView.ViewHolder {

    public CardView cvPdf;
    public TextView tvName,tvSize;
    public ImageView imgvThumbnail;

    public PdfHolder(@NonNull View itemView) {
        super(itemView);

        cvPdf = itemView.findViewById(R.id.cv_pdf);

        imgvThumbnail = itemView.findViewById(R.id.imgv_thumbnail);

        tvName = itemView.findViewById(R.id.tv_name);
        tvSize = itemView.findViewById(R.id.tv_size);
    }
}
