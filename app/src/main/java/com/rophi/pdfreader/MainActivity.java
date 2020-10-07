package com.rophi.pdfreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.rophi.pdfreader.databinding.ActivityMainBinding;
import com.rophi.pdfreader.db.Pdf;
import com.rophi.pdfreader.home.HomeFragment;
import com.rophi.pdfreader.home.PdfAdapter;
import com.rophi.pdfreader.merge.MergeActivity;
import com.rophi.pdfreader.merge.MergeCallback;
import com.rophi.pdfreader.merge.MergedFragment;
import com.rophi.pdfreader.recent.RecentFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MergeCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private Fragment activeFragment;
    private RecentFragment recentFragment;
    private HomeFragment homeFragment;
    private MergedFragment mergedFragment;
    private boolean merged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        //set up fragments
        recentFragment = new RecentFragment();
        homeFragment = new HomeFragment();
        mergedFragment = new MergedFragment();

        activeFragment = homeFragment;

        //add fragments
        addFragments();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);

        binding.bottomNavigation.setSelectedItemId(R.id.home);
    }

    private void addFragments() {
        fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .add(R.id.fl_container,recentFragment,RecentFragment.class.getSimpleName())
                .hide(recentFragment)
                .commit();

        fragmentManager
                .beginTransaction()
                .add(R.id.fl_container,homeFragment,HomeFragment.class.getSimpleName())
                .commit();

        fragmentManager
                .beginTransaction()
                .add(R.id.fl_container,mergedFragment,MergedFragment.class.getSimpleName())
                .hide(mergedFragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recent:
                fragmentManager.beginTransaction().hide(activeFragment).show(recentFragment).commit();
                activeFragment = recentFragment;

                binding.tvTitle.setText("Recent");
                return true;

            case R.id.home:
                fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment = homeFragment;

                binding.tvTitle.setText("All Documents");
                return true;


            case R.id.merged:
                if (merged){
                    mergedFragment.reload();
                    merged = false;
                }
                fragmentManager.beginTransaction().hide(activeFragment).show(mergedFragment).commit();
                activeFragment = mergedFragment;

                binding.tvTitle.setText("Merged Documents");
                return true;
        }
        return false;
    }

    @Override
    public void performMerge(String name) {

    }

    @Override
    public void onMerged(boolean merged) {
        this.merged = merged;

        if (merged)
            binding.bottomNavigation.setSelectedItemId(R.id.merged);

    }
}