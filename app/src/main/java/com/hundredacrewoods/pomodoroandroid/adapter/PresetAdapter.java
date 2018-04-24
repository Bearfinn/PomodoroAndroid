package com.hundredacrewoods.pomodoroandroid.adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hundredacrewoods.pomodoroandroid.R;
import com.hundredacrewoods.pomodoroandroid.activities.MainActivity;
import com.hundredacrewoods.pomodoroandroid.databases.Preset;
import com.hundredacrewoods.pomodoroandroid.fragments.AddingPresetFragment;
import com.hundredacrewoods.pomodoroandroid.fragments.PresetFragment;

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
            final Preset current = mPresets.get(position);
            holder.presetName.setText(current.getPresetName());

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor("" + current.getPresetName());
            TextDrawable drawable = TextDrawable.builder().buildRoundRect(current.getPresetName().
                    substring(0, 1), color, 60);

            holder.circle.setImageDrawable(drawable);

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
                                case R.id.preset_popup_menu_delete:
                                    mPresets.remove(current);
                                    MainActivity mainActivity = (MainActivity) mContext;
                                    mainActivity.getPomodoroViewModel().
                                            delete(new Integer(current.getPresetID()));
                                    return true;
                                default: return false;
                            }
                        }
                    });
                    return true;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Preset currentPreset = mPresets.get(position);
                    Fragment addingPresetFragment = AddingPresetFragment.
                            newInstance(currentPreset);
                    FragmentManager fragmentManager =
                            ((MainActivity) mContext).getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_fragmentholder,
                            addingPresetFragment, AddingPresetFragment.TAG);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        } else {
            holder.presetName.setText("No preset");
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
        ImageView circle;
        View itemView;

        public ViewHolder (View itemView) {
            super(itemView);
            this.itemView = itemView;
            presetName = (TextView) itemView.findViewById(R.id.preset_name_text);
            circle = (ImageView) itemView.findViewById(R.id.primary_action);
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
