package com.rophi.pdfreader.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;

import com.rophi.pdfreader.db.Pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetPDFS extends AsyncTask<Void, Void, Void> {

    private GetPdfListener getPdfListener;
    private File dir;
    private List<Pdf> pdfList;

    public GetPDFS(GetPdfListener listener, File dir) {
        this.getPdfListener = listener;
        this.dir = dir;

        pdfList = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        search_Dir(dir);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        getPdfListener.sendPdfList(pdfList);

    }

    private void search_Dir(File dir) {
        String pdfPattern = ".pdf";

        File files[] = dir.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                if (files[i].isDirectory()) {
                    search_Dir(files[i]);
                } else {
                    File file = files[i];
                    if (file.getName().endsWith(pdfPattern)) {
                        //here you have that file.

                        Pdf pdf = new Pdf();
                        pdf.setFilePath(file.getPath());
                        pdf.setThumbnail(getThumbnail(file));
                        pdf.setDateModified(file.lastModified());
                        pdf.setFileName(file.getName());

                        pdfList.add(pdf);

                    }
                }
            }
        }
    }

    private Bitmap getThumbnail(File pdfFile) {
        PdfRenderer renderer = null;
        Bitmap bitmap = null;
        try {
            renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

            if (renderer != null){
                PdfRenderer.Page page = renderer.openPage(0);

                bitmap = Bitmap.createBitmap(200,200, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




        return bitmap;
    }

}
