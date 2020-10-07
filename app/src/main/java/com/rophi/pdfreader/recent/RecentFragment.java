package com.rophi.pdfreader.recent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.rophi.pdfreader.R;
import com.rophi.pdfreader.databinding.FragmentMergedBinding;
import com.rophi.pdfreader.databinding.FragmentRecentBinding;

public class RecentFragment extends Fragment {

    private FragmentRecentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recent,container,false);

        return binding.getRoot();
    }
}
