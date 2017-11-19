package com.techbridge.smsreader.views.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.models.TankLevel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class TankAdapter extends Adapter<TankAdapter.ViewHolder> {
    private ArrayList<TankLevel> mLevels;
    private ArrayList<TankLevel> orig;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public CardView cardView;
        private TextView txtDescription;
        private TextView txtHeader;

        public ViewHolder(View v) {
            super(v);
            this.cardView = (CardView) v.findViewById(R.id.tLcardView);
            this.txtHeader = (TextView) v.findViewById(R.id.txtHeader);
            this.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
        }
    }

    public TankAdapter(ArrayList<TankLevel> myLevels) {
        this.mLevels = myLevels;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tank_level_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtHeader.setText(String.valueOf(new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(new Date(Long.parseLong(((TankLevel) this.mLevels.get(position)).getTimes())))) + StringUtils.SPACE + ((TankLevel) this.mLevels.get(position)).getTimes());
        holder.txtDescription.setText(((TankLevel) this.mLevels.get(position)).getLevel() + StringUtils.SPACE + ((TankLevel) this.mLevels.get(position)).getuId());
    }

    public int getItemCount() {
        return this.mLevels.size();
    }
}
