package com.rophi.pdfreader.db;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pdfs")
public class Pdf {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "path")
    public String filePath;

    @ColumnInfo(name = "name")
    public String fileName;

    @ColumnInfo(name = "size")
    public int fileSize;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "display_name")
    public String displayName;

    @ColumnInfo(name = "date_added")
    public long dateAdded;

    @ColumnInfo(name = "date_modified")
    public long dateModified;

    public Bitmap thumbnail;

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Pdf() {
    }

    public Pdf(String filePath, String fileName, int fileSize, String title, String displayName, long dateAdded, long dateModified) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.title = title;
        this.displayName = displayName;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }
}
