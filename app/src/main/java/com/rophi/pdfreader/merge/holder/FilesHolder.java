package com.rophi.pdfreader.merge.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rophi.pdfreader.R;


public class FilesHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
    public ImageView imgvClose;

    public FilesHolder(@NonNull View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.tv_name);
        imgvClose = itemView.findViewById(R.id.imgv_close);

    }
}
