package com.rophi.pdfreader.merge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rophi.pdfreader.R;
import com.rophi.pdfreader.merge.FilesRemovedCallback;
import com.rophi.pdfreader.merge.holder.FilesHolder;

import java.io.File;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesHolder> {

    private Context context;
    private List<String> list;
    private FilesRemovedCallback filesRemoveCallback;
    private LayoutInflater inflater;

    public FilesAdapter(Context context, List<String> list, FilesRemovedCallback filesRemoveCallback) {
        this.context = context;
        this.list = list;
        this.filesRemoveCallback = filesRemoveCallback;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FilesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilesHolder(inflater.inflate(R.layout.item_selected_file,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilesHolder holder, final int position) {
        holder.tvName.setText(new File(list.get(position)).getName());

        holder.imgvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }

    public void addFile(String name){
        list.add(name);
        notifyItemInserted(list.size());
    }

    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();

        if (list.size() == 0)
            filesRemoveCallback.onFileRemoved();
    }
}
