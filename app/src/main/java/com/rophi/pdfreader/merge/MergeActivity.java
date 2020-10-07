package com.rophi.pdfreader.merge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.rophi.pdfreader.R;
import com.rophi.pdfreader.databinding.ActivityMergeBinding;
import com.rophi.pdfreader.merge.adapter.FilesAdapter;
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility;
import com.tom_roush.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MergeActivity extends AppCompatActivity implements View.OnClickListener, PickiTCallbacks, FilesRemovedCallback,MergeCallback {

    private static final String TAG = MergeActivity.class.getSimpleName();
    private ActivityMergeBinding binding;
    private PDDocument pdfDocument;
    private boolean selectFirst = false;
    private boolean selectSecond = false;
    private PickiT pickiT;
    private String sourceFile;
    private FilesAdapter filesAdapter;
    private List<String> filesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_merge);

        filesList = new ArrayList<>();

        pickiT = new PickiT(this, this, this);

        pdfDocument = new PDDocument();

        binding.imgvSelectSource.setOnClickListener(this);
        binding.imgvMergeSource.setOnClickListener(this);
        binding.btnMerge.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgv_select_source:
                if (checkPermissions()){
                    dispatchSelectFilesIntent(200);
                }else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},200);
                }
                break;

            case R.id.imgv_merge_source:
                if (checkPermissions()){
                    dispatchSelectFilesIntent(201);
                }else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},200);
                }
                break;

            case R.id.btn_merge:
                OutputFileDialog outputFileDialog = new OutputFileDialog();
                outputFileDialog.show(getSupportFragmentManager(),OutputFileDialog.class.getSimpleName());
                break;
        }
    }

    private void dispatchSelectFilesIntent(int i) {

        PackageManager packageManager = getPackageManager();

        List<Intent> intentList = new ArrayList<>();

        Intent intentGallery = new Intent();
        intentGallery.setType("application/pdf");
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);//

        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(intentGallery, 0);

        for (ResolveInfo resolveInfo : listGallery) {
            Intent intent = new Intent(intentGallery);
            intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
            intent.setPackage(resolveInfo.activityInfo.packageName);
            intentList.add(intent);
        }

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(intentList.get(intentList.size() - 1), "Select source");

        intentList.remove(intentList.size() - 1);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[intentList.size()]));

        startActivityForResult(chooserIntent, i);
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchSelectFilesIntent(201);
        } else {
            Toast.makeText(this, "Please provide required permissions", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200){
            if (resultCode == RESULT_OK){
                selectFirst = true;
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
            }
        }

        if (requestCode == 201) {
            if (resultCode == RESULT_OK){
                selectSecond = true;
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
            }
        }
    }


    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
            if (selectFirst){
                sourceFile = path;
                binding.tvSource.setVisibility(View.VISIBLE);
                binding.tvSource.setText(new File(path).getName());

                binding.flMergeWith.setVisibility(View.VISIBLE);
                binding.btnMerge.setVisibility(View.VISIBLE);
                selectFirst = false;
            }

            if (selectSecond){
                addSelectedFile(path);
                selectSecond = false;
            }
    }

    private void addSelectedFile(String path) {
        if (filesAdapter == null){
            filesList.add(path);

            filesAdapter = new FilesAdapter(this,filesList,this);

            binding.rvFiles.setLayoutManager(new LinearLayoutManager(this));
            binding.rvFiles.setAdapter(filesAdapter);
        }else {
            filesAdapter.addFile(path);
        }

        binding.btnMerge.setEnabled(true);
    }

    @Override
    public void onFileRemoved() {
        binding.btnMerge.setEnabled(false);
    }

    @Override
    public void performMerge(String name) {

        File mergedDirectory = new File(Environment.getExternalStorageDirectory()+"/Rophi");

        if (!mergedDirectory.exists())
            mergedDirectory.mkdir();


        //set file name
        if (name.length() > 4){
           String extension =  name.substring(name.length() - 4);

           if (!extension.equalsIgnoreCase(".pdf"))
               name += ".pdf";
        }else {
            name += ".pdf";
        }


        File file = new File(mergedDirectory.getPath()+"/"+name);

        if (file.exists()){
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> list = new ArrayList<>();
        list.add(sourceFile);

        list.addAll(filesList);

        for (int i = 0; i < list.size(); i++) {
            //load each file and append
            FileInputStream loadedFile = null;
            try {
                loadedFile = new FileInputStream(list.get(i));
                PDDocument newDoc = PDDocument.load(loadedFile, true);

                PDFMergerUtility mu = new PDFMergerUtility();
                mu.appendDocument(pdfDocument,newDoc);
                pdfDocument.save(file);

                newDoc.close();

                loadedFile.close();

                //merged pdf created
                sendCallBack(true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMerged(boolean merged) {

    }

    private void sendCallBack(boolean merged) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("files_merged",merged);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        sendCallBack(false);
    }
}