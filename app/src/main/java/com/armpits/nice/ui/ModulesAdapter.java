package com.armpits.nice.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
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
            holder.chkDownload.setChecked(isChecked);
            module.enableDownloads = isChecked;
            NiceDatabase.update(module);
        });

        holder.chkCalendar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) return;
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
