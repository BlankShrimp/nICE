package com.armpits.nice.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(primaryKeys = {"moduleCode", "filename", "parentPath"})
public class Material {
    @NonNull public final String moduleCode;
    @NonNull public final String filename;
    @NonNull public final String parentPath;
    @NonNull public final String url;
    @NonNull public final String description;
    public final String moduleTitle;
    public Date dateDownloaded;
    public boolean shouldDownload;

    public Material(String moduleTitle, String moduleCode, String filename, String parentPath,
                    String url, String description, Date dateDownloaded, boolean shouldDownload) {
        this.moduleTitle = moduleTitle;
        this.moduleCode = moduleCode;
        this.filename = filename;
        this.parentPath = parentPath;
        this.url = url;
        this.description = description;
        this.dateDownloaded = dateDownloaded;
        this.shouldDownload = shouldDownload;
    }
}
