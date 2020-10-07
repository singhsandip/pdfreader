package com.rophi.pdfreader.merge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.rophi.pdfreader.R;
import com.rophi.pdfreader.databinding.DialogMergedFileNameBinding;

public class OutputFileDialog extends DialogFragment implements View.OnClickListener {

    private DialogMergedFileNameBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_merged_file_name,container,false);

        setCancelable(false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvCancel.setOnClickListener(this);
        binding.tvOkay.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getDialog() != null){
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            window.setAttributes(wlp);

            window.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.bg_transparent));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;

            case R.id.tv_okay:
                //callback to activity
                if (binding.etFileName.getText().toString().isEmpty()){
                    binding.etFileName.setError("Please enter file name");
                }else {
                    MergeCallback mergeCallback = (MergeCallback) getActivity();
                    mergeCallback.performMerge(binding.etFileName.getText().toString());
                    dismiss();
                }
                break;
        }
    }
}
