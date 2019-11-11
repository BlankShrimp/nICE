package com.armpits.nice.models;

import java.util.Date;

public class Material {
    public final String filename;
    public final String parentPath;
    public final Date dateDownloaded;
    public final boolean shouldDownload;

    public Material(String filename, String parentPath, Date dateDownloaded, boolean shouldDownload) {
        this.filename = filename;
        this.parentPath = parentPath;
        this.dateDownloaded = dateDownloaded;
        this.shouldDownload = shouldDownload;
    }
}
