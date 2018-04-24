package com.hundredacrewoods.pomodoroandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.databases.Preset;

import java.util.List;

public class PresetAdapter extends RecyclerView.Adapter<PresetAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private List<Preset> mPresets;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.preset_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (mPresets != null) {
            Preset current = mPresets.get(position);
            holder.presetName.setText(current.getPresetName());
            holder.presetID.setText("" + current.getPresetID());

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.d("Adapter", "view @ " + position + " is long pressed");
                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    MenuInflater menuInflater = popupMenu.getMenuInflater();
                    menuInflater.inflate(R.menu.preset_popup_menu, popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.preset_popup_menu_edit:
                                    return true;
                                case R.id.preset_popup_menu_delete:
                                    
                                    return true;
                                default: return false;
                            }
                        }
                    });
                    return true;
                }
            });
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
        View itemView;

        public ViewHolder (View itemView) {
            super(itemView);
            this.itemView = itemView;
            presetName = (TextView) itemView.findViewById(R.id.presetNameText);
            presetID = (TextView) itemView.findViewById(R.id.presetIDText);
        }
    }

    public PresetAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setPresets(List<Preset> presets) {
        mPresets = presets;
        notifyDataSetChanged();
    }
}
