package com.armpits.nice.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.armpits.nice.R;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Module;


public class ModulesAdapter extends RecyclerView.Adapter<ModulesAdapter.MyViewHolder> {
    private Context mContext;
    private List<Module> modulesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle;
        public CheckBox chkDownload, chkNotifications, chkCalendar;

        public MyViewHolder(View view) {
            super(view);
            txtTitle        = view.findViewById(R.id.lblTitle);
            chkDownload     = view.findViewById(R.id.chkDownload);
            chkNotifications= view.findViewById(R.id.chkNotifications);
            chkCalendar     = view.findViewById(R.id.chkCalendar);
        }
    }


    public ModulesAdapter(Context mContext, List<Module> modulesList) {
        this.mContext = mContext;
        this.modulesList = modulesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_module, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // this is a single module card
        Module module = modulesList.get(position);

        // set title
        String title = module.code + " " + module.title;
        holder.txtTitle.setText(title);

        // set checkboxes
        holder.chkCalendar.setChecked(module.addDDLsToCalendar);
        holder.chkDownload.setChecked(module.enableDownloads);
        holder.chkNotifications.setChecked(module.enableNotifications);

        holder.chkDownload.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) return;
            // check & request permission
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    new AlertDialog.Builder(mContext)
                            .setTitle(R.string.title_file)
                            .setMessage(R.string.description_file)
                            .setPositiveButton(R.string.confirm_button, (dialog, which) ->
                                    ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1))
                            .show();
                }else{
                    ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
            }
            holder.chkDownload.setChecked(isChecked);
            module.enableDownloads = isChecked;
            NiceDatabase.update(module);
        });

        holder.chkCalendar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) return;
            // check & request permission
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,Manifest.permission.READ_CALENDAR)){
                    new AlertDialog.Builder(mContext)
                            .setTitle(R.string.title_calendar)
                            .setMessage(R.string.description_calendar)
                            .setPositiveButton(R.string.confirm_button, (dialog, which) ->
                                    ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.READ_CALENDAR}, 1))
                            .show();
                }else{
                    ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.READ_CALENDAR},1);
                }
            }
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,Manifest.permission.WRITE_CALENDAR)){
                    new AlertDialog.Builder(mContext)
                            .setTitle(R.string.title_calendar)
                            .setMessage(R.string.description_calendar)
                            .setPositiveButton(R.string.confirm_button, (dialog, which) ->
                                    ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.WRITE_CALENDAR}, 1))
                            .show();
                }else{
                    ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.WRITE_CALENDAR},1);
                }
            }
            holder.chkCalendar.setChecked(isChecked);
            module.addDDLsToCalendar = isChecked;
            NiceDatabase.update(module);
        });

        holder.chkNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) return;
            holder.chkNotifications.setChecked(isChecked);
            module.enableNotifications = isChecked;
            NiceDatabase.update(module);
        });
    }

    @Override
    public int getItemCount() {
        return modulesList.size();
    }
}
