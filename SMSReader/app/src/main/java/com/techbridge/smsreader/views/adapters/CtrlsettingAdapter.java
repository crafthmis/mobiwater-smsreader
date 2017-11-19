package com.techbridge.smsreader.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.models.Setting;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.views.activities.CtrldashboardActivity;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class CtrlsettingAdapter extends Adapter<CtrlsettingAdapter.ViewHolder> {
    private static final int PENDING_REMOVAL_TIMEOUT = 5000;
    private DBHelper dbHelper;
    private Handler handler = new Handler();
    private ArrayList<Setting> itemsPendingRemoval;
    private ArrayList<Setting> mSettings;
    private ArrayList<Setting> orig;
    HashMap<Setting, Runnable> pendingRunnables = new HashMap();

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public CardView cardView;
        private LinearLayout swipeLayout;
        private TextView txtDescription;
        private TextView txtHeader;
        private TextView txtUndo;

        public ViewHolder(View v) {
            super(v);
            this.cardView = (CardView) v.findViewById(R.id.tLcardView);
            this.txtHeader = (TextView) v.findViewById(R.id.txtHeader);
            this.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            this.swipeLayout = (LinearLayout) v.findViewById(R.id.swipeLayout);
            this.txtUndo = (TextView) v.findViewById(R.id.undo);
        }
    }

    public CtrlsettingAdapter(ArrayList<Setting> mySettings, Context context) {
        this.mSettings = mySettings;
        this.itemsPendingRemoval = new ArrayList();
        this.dbHelper = new DBHelper(context);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_row_item, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Setting data = (Setting) this.mSettings.get(position);
        if (this.itemsPendingRemoval.contains(data)) {
            holder.cardView.setVisibility(View.GONE);
            holder.swipeLayout.setVisibility(View.VISIBLE);
            holder.txtUndo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CtrlsettingAdapter.this.undoOpt(data);
                }
            });
            return;
        }
        holder.cardView.setVisibility(View.VISIBLE);
        holder.swipeLayout.setVisibility(View.GONE);
        holder.txtHeader.setText(((Setting) this.mSettings.get(position)).getTankName());
        holder.txtDescription.setText(((Setting) this.mSettings.get(position)).getPhoneNumber());
        holder.cardView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String rawMsisdn = ((Setting) CtrlsettingAdapter.this.mSettings.get(position)).getPhoneNumber();
                Prefs.writeString(holder.cardView.getContext(), "settingPhone", "254" + StringUtils.substring(rawMsisdn, rawMsisdn.length() - 9, rawMsisdn.length()).trim());
                Prefs.writeString(holder.cardView.getContext(), "settingTank", ((Setting) CtrlsettingAdapter.this.mSettings.get(position)).getTankName());
                holder.cardView.getContext().startActivity(new Intent(holder.cardView.getContext(), CtrldashboardActivity.class));
            }
        });
    }

    public int getItemCount() {
        return this.mSettings.size();
    }

    private void undoOpt(Setting customer) {
        Runnable pendingRemovalRunnable = (Runnable) this.pendingRunnables.get(customer);
        this.pendingRunnables.remove(customer);
        if (pendingRemovalRunnable != null) {
            this.handler.removeCallbacks(pendingRemovalRunnable);
        }
        this.itemsPendingRemoval.remove(customer);
        notifyItemChanged(this.mSettings.indexOf(customer));
    }

    public void pendingRemoval(int position) {
        final Setting data = (Setting) this.mSettings.get(position);
        if (!this.itemsPendingRemoval.contains(data)) {
            this.itemsPendingRemoval.add(data);
            notifyItemChanged(position);
            Runnable pendingRemovalRunnable = new Runnable() {
                public void run() {
                    CtrlsettingAdapter.this.remove(CtrlsettingAdapter.this.mSettings.indexOf(data));
                    CtrlsettingAdapter.this.dbHelper.deleteSetting(data.getSettingId());
                }
            };
            this.handler.postDelayed(pendingRemovalRunnable, 5000);
            this.pendingRunnables.put(data, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        Setting data = (Setting) this.mSettings.get(position);
        if (this.itemsPendingRemoval.contains(data)) {
            this.itemsPendingRemoval.remove(data);
        }
        if (this.mSettings.contains(data)) {
            this.mSettings.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        return this.itemsPendingRemoval.contains((Setting) this.mSettings.get(position));
    }
}
