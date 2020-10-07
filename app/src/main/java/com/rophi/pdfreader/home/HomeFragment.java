package com.rophi.pdfreader.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.rophi.pdfreader.R;
import com.rophi.pdfreader.databinding.FragmentHomeBinding;
import com.rophi.pdfreader.db.Pdf;
import com.rophi.pdfreader.merge.MergeActivity;
import com.rophi.pdfreader.merge.MergeCallback;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements GetPdfListener,View.OnClickListener{

    private FragmentHomeBinding binding;
    private final int MERGE_REQUEST = 200;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    private boolean isOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);


        if (checkPermissions()){
            searchDir(Environment.getExternalStorageDirectory());
        }else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},201);
        }


        binding.fabAdd.setOnClickListener(this);
        binding.fabMerge.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add:
                performAnimation();
                break;

            case R.id.fab_merge:
                startActivityForResult(new Intent(getContext(), MergeActivity.class),MERGE_REQUEST);
                break;
        }
    }

    private void performAnimation() {
        if (isOpen) {

            binding.llTransparent.setVisibility(View.GONE);

            binding.tvMerge.setVisibility(View.INVISIBLE);
            binding.fabAdd.setVisibility(View.INVISIBLE);

            binding.fabAdd.startAnimation(fab_close);
            binding.fabMerge.startAnimation(fab_close);
            binding.fabAdd.startAnimation(fab_anticlock);
            binding.fabMerge.setClickable(false);


            isOpen = false;
        } else {
            binding.llTransparent.setVisibility(View.VISIBLE);

            binding.tvMerge.setVisibility(View.VISIBLE);
            binding.fabMerge.startAnimation(fab_open);

            binding.fabAdd.startAnimation(fab_clock);
            binding.fabMerge.setClickable(true);

            isOpen = true;
        }
    }

    public void searchDir(File dir) {
        binding.progressBar.setVisibility(View.VISIBLE);

        GetPDFS getPDFS = new GetPDFS(this,dir);
        getPDFS.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                //getAllPDFFiles();
                searchDir(Environment.getExternalStorageDirectory());
            }else {
                Toast.makeText(getContext(), "Please provide required permissions", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please provide required permissions", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    @Override
    public void sendPdfList(List<Pdf> pdfList) {
        if (getContext() != null){
            binding.progressBar.setVisibility(View.GONE);

            binding.llMergeContainer.setVisibility(View.VISIBLE);

            if (pdfList.size() == 0){
                Toast.makeText(getContext(), "No pdf file exist", Toast.LENGTH_SHORT).show();
            }else {
                binding.rvPdf.setLayoutManager(new GridLayoutManager(getContext(),2));
                binding.rvPdf.setAdapter(new PdfAdapter(getContext(),pdfList));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MERGE_REQUEST && resultCode == RESULT_OK){
            // if file merged... select merged files fragment

            if (data != null && data.getBooleanExtra("files_merged",false)){
                MergeCallback mergeCallback = (MergeCallback) getActivity();
                mergeCallback.onMerged(data.getBooleanExtra("files_merged",false));
            }
        }
    }
}
