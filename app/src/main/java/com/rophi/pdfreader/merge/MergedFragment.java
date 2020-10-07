package com.rophi.pdfreader.merge;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rophi.pdfreader.R;
import com.rophi.pdfreader.databinding.FragmentHomeBinding;
import com.rophi.pdfreader.databinding.FragmentMergedBinding;
import com.rophi.pdfreader.db.Pdf;
import com.rophi.pdfreader.home.GetPDFS;
import com.rophi.pdfreader.home.GetPdfListener;
import com.rophi.pdfreader.home.PdfAdapter;

import java.io.File;
import java.util.List;

public class MergedFragment extends Fragment implements GetPdfListener {

    private FragmentMergedBinding binding;
    private File mergedDirectory;
    private PdfAdapter pdfAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_merged,container,false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mergedDirectory = new File(Environment.getExternalStorageDirectory()+"/Rophi");

        if (!mergedDirectory.exists()){
            binding.llNoEntry.setVisibility(View.VISIBLE);
        }else {
            getMergedFiles(mergedDirectory);
        }
    }

    private void getMergedFiles(File dir) {
        binding.progressBar.setVisibility(View.VISIBLE);

        GetPDFS getPDFS = new GetPDFS(this,dir);
        getPDFS.execute();
    }

    public void reload() {
        getMergedFiles(mergedDirectory);
    }

    @Override
    public void sendPdfList(List<Pdf> pdfList) {
        if (getContext() != null){
            binding.progressBar.setVisibility(View.GONE);

            if (pdfList.size() == 0){
                binding.llNoEntry.setVisibility(View.VISIBLE);
            }else {
                if (pdfAdapter == null){
                    binding.rvMergedPdfs.setLayoutManager(new GridLayoutManager(getContext(),2, RecyclerView.VERTICAL,true));

                    pdfAdapter = new PdfAdapter(getContext(),pdfList);
                    binding.rvMergedPdfs.setAdapter(pdfAdapter);
                }else {
                    pdfAdapter.update(pdfList);
                    binding.rvMergedPdfs.scrollToPosition(0);
                }
            }
        }
    }
}
