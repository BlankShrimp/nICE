package com.armpits.nice.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.Date;

@Entity
public class Material {
    @ColumnInfo(name = "module_code") public final String moduleCode;
    @ColumnInfo(name = "filename") public final String filename;
    @ColumnInfo(name = "parent_path") public final String parentPath;
    @ColumnInfo(name = "date_downloaded") public final Date dateDownloaded;
    @ColumnInfo(name = "should_download") public final boolean shouldDownload;

    public Material( String moduleCode, String filename, String parentPath, Date dateDownloaded, boolean shouldDownload) {
        this.moduleCode = moduleCode;
        this.filename = filename;
        this.parentPath = parentPath;
        this.dateDownloaded = dateDownloaded;
        this.shouldDownload = shouldDownload;
    }
}
