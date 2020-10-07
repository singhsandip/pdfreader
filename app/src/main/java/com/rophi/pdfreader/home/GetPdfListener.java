package com.rophi.pdfreader.home;

import com.rophi.pdfreader.db.Pdf;

import java.util.List;

public interface GetPdfListener {
    void sendPdfList(List<Pdf> pdfList);
}
