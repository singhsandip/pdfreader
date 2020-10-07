package com.rophi.pdfreader.merge;

public interface MergeCallback {
    void performMerge(String name);

    void onMerged(boolean merged);
}
