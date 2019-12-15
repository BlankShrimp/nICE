package com.armpits.nice.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.armpits.nice.R;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Log;
import com.armpits.nice.models.Module;

import java.util.Date;
import java.util.List;


public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Log> logsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // logs have a date and some text
        public TextView txtDate, txtContent;

        public MyViewHolder(View view) {
            super(view);
            txtDate     = view.findViewById(R.id.txt_log_date);
            txtContent  = view.findViewById(R.id.txt_log_message);
        }
    }


    public LogsAdapter(Context mContext, List<Log> logsList) {
        this.mContext = mContext;
        this.logsList = logsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_entry, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // this is a single log entry
        Log log = logsList.get(position);

        holder.txtDate.setText(log.date.toString().substring(0, 20));
        holder.txtContent.setText(log.message);
    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }
}
