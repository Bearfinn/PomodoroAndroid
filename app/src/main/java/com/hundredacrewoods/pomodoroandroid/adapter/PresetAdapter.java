package com.hundredacrewoods.pomodoroandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.databases.Preset;

import java.util.List;

public class PresetAdapter extends RecyclerView.Adapter<PresetAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Preset> mPresets;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.preset_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mPresets != null) {
            Preset current = mPresets.get(position);
            holder.presetName.setText(current.getPresetName());
            holder.presetID.setText("" + current.getPresetID());
        } else {
            holder.presetName.setText("No preset");
            holder.presetID.setText("No preset");
        }
    }

    @Override
    public int getItemCount() {
        if (mPresets != null) {
            return mPresets.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView presetName;
        TextView presetID;

        public ViewHolder (View itemView) {
            super(itemView);
            presetName = (TextView) itemView.findViewById(R.id.presetNameText);
            presetID = (TextView) itemView.findViewById(R.id.presetIDText);
        }
    }

    public PresetAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setPresets(List<Preset> presets) {
        mPresets = presets;
        notifyDataSetChanged();
    }
}
