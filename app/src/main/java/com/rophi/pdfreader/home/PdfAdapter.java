package com.rophi.pdfreader.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rophi.pdfreader.R;
import com.rophi.pdfreader.db.Pdf;
import com.rophi.pdfreader.utils.FileUtils;
import com.rophi.pdfreader.viewpdf.ViewPdfActivity;

import java.io.File;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfHolder> {
    private Context context;
    private List<Pdf> pdfList;
    private LayoutInflater inflater;

    public PdfAdapter(Context context, List<Pdf> pdfList) {
        this.context = context;
        this.pdfList = pdfList;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PdfHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PdfHolder(inflater.inflate(R.layout.item_pdf,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PdfHolder holder, int position) {
        final Pdf pdf = pdfList.get(position);

        holder.imgvThumbnail.setImageBitmap(pdf.getThumbnail());

        holder.tvName.setText(pdf.getFileName());

        try {
            holder.tvSize.setText(FileUtils.getFileSize(new File(pdf.getFilePath())));
        }catch (IllegalArgumentException e){

        }

        holder.cvPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewPdfActivity.class);
                intent.putExtra("file_path",pdf.getFilePath());

                context.startActivity(intent);
            }
        });
    }

    public void update(List<Pdf> pdfList){
        this.pdfList = pdfList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return (pdfList == null) ? 0 : pdfList.size();
    }
}
