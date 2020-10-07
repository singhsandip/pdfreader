package com.rophi.pdfreader.viewpdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;

import com.rophi.pdfreader.R;
import com.rophi.pdfreader.databinding.ActivityViewPdfBinding;

import java.io.File;
import java.util.ArrayList;

public class ViewPdfActivity extends AppCompatActivity {

    private ActivityViewPdfBinding binding;
    private PagerSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private int snapPosition = RecyclerView.NO_POSITION;
    private boolean isPdfLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_pdf);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isPdfLoaded){
            binding.tvHeader.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);

            pdfToBitmap(new File(getIntent().getStringExtra("file_path")));
        }
    }

    private void pdfToBitmap(File pdfFile) {
        LoadPdf loadPdf = new LoadPdf(pdfFile);
        loadPdf.execute();
    }

    private void setUpPdf(ArrayList<Bitmap> bitmaps) {
        PdfViewAdapter pdfViewAdapter = new PdfViewAdapter(this,bitmaps);

        binding.rvPdf.setLayoutManager(new LinearLayoutManager(this));

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        snapHelper = new PagerSnapHelper();

        binding.rvPdf.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(binding.rvPdf);

        binding.rvPdf.setAdapter(pdfViewAdapter);


         binding.rvPdf.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
             public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                 super.onScrolled(recyclerView, dx, dy);

                 maybeNotifySnapPositionChange(recyclerView);
             }
         });

        binding.progressBar.setVisibility(View.GONE);
        binding.tvHeader.setVisibility(View.VISIBLE);
        isPdfLoaded = true;
    }

    private void maybeNotifySnapPositionChange(RecyclerView recyclerView) {
        View snapView = snapHelper.findSnapView(recyclerView.getLayoutManager());

        int snapPosition = layoutManager.getPosition(snapView);

        boolean snapPositionChanged = this.snapPosition != snapPosition;

        if (snapPositionChanged) {

            binding.tvHeader.setText(String.valueOf(snapPosition + 1));

            this.snapPosition = snapPosition;
        }
    }

    private class  LoadPdf extends AsyncTask<Void,Void,Void> {

        private File pdfFile;
        private  ArrayList<Bitmap> bitmaps;

        public LoadPdf(File pdfFile) {
            this.pdfFile = pdfFile;
            bitmaps = new ArrayList<>();;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

                Bitmap bitmap;
                final int pageCount = renderer.getPageCount();
                for (int i = 0; i < pageCount; i++) {
                    PdfRenderer.Page page = renderer.openPage(i);

                    bitmap = Bitmap.createBitmap(page.getWidth() * 2,page.getHeight() * 2, Bitmap.Config.ARGB_8888);

                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                    bitmaps.add(bitmap);

                    page.close();
                }

                // close the renderer
                renderer.close();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //setup pdf
            setUpPdf(bitmaps);
        }
    }
}