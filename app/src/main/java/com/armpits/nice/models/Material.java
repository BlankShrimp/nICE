package com.armpits.nice.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"moduleCode", "filename", "parentPath"})
public class Material {
    @NonNull public final String moduleCode;
    @NonNull public final String filename;
    @NonNull public final String parentPath;
    public Date dateDownloaded;
    public boolean shouldDownload;

    public Material( String moduleCode, String filename, String parentPath, Date dateDownloaded, boolean shouldDownload) {
        this.moduleCode = moduleCode;
        this.filename = filename;
        this.parentPath = parentPath;
        this.dateDownloaded = dateDownloaded;
        this.shouldDownload = shouldDownload;
    }
}
