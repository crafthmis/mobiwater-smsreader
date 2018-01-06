package com.techbridge.smsreader.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.FillableLoaderBuilder;
import com.github.jorgecastillo.clippingtransforms.WavesClippingTransform;
import com.master.permissionhelper.PermissionHelper;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeader.OnAccountHeaderListener;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.Paths;
import com.techbridge.smsreader.utils.Prefs;
import com.techbridge.smsreader.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import static java.lang.Math.floor;


public class DashboardActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static DashboardActivity activity;
    private TextView batterytext;
    private Context context = this;
    public ArrayAdapter<String> dataAdapter;
    private FillableLoader fillableLoader;
    private DBHelper dbhelper;
    private boolean doubleBackToExitPressedOnce = false;
    private Toolbar mToolbar;
    private TextView meterReading,meterReading2,meterReading3,tankText,waterPercentage;
    private Paint f62p = new Paint();
    private ProgressDialog pDialog;
    private PermissionHelper permissionHelper;
    private Spinner spinner;
    private LinearLayout linearLayout;
    private Button btnBack, btnNext;
    private int i = 0;



    class C03035 implements Runnable {
        C03035() {
        }

        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        DBHelper dbHelper;

        private AsyncCallWS() {
            dbHelper = new DBHelper(DashboardActivity.this);
        }

        protected void onPreExecute() {
            pDialog = new ProgressDialog(DashboardActivity.this);
            pDialog.setMessage("Updating...");
            pDialog.setIndeterminate(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... params) {
            dbHelper.updateTankReadings();
            return null;
        }

        protected void onPostExecute(Void result) {
            if (pDialog != null || pDialog.isShowing()) {
                pDialog.dismiss();
                Utils.showShortToast(context, "Successfully updated").show();
                String tankUniqueId = Prefs.readString(context, "tuId", "").equalsIgnoreCase("")?dbhelper.getTankUniqueIndex(0):Prefs.readString(context, "tuId", "");
                Double percentage = dbhelper.getTankPercentageByUid(tankUniqueId);
                String[] result2 = dbhelper.getLastTextTanklevel(tankUniqueId);
                meterReading.setText(result2[0]);
                meterReading3.setText(result2[1]);
                meterReading2.setText(result2[2]);
                waterPercentage.setText(convertPercentage(percentage));
                tankText.setText(dbhelper.getTankName(tankUniqueId));
                UpdateTank(percentage);
            }
        }
    }


    class C05132 implements OnAccountHeaderListener {
        C05132() {
        }

        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
            return false;
        }
    }

    class C05143 implements OnDrawerItemClickListener {
        C05143() {
        }

        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            Intent intent = null;
            if (drawerItem != null) {
                switch (position) {
                    case 1:
                        intent = new Intent(context, SettingActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, TabActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, HistoricaldataActivity.class);
                        break;
                    case 5:
                        intent = new Intent(context, CtrlsettingsActivity.class);
                        break;
                    case 6:
                        intent = new Intent(context, AlarmActivity.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
            return false;
        }
    }

    public static DashboardActivity instance() {
        return activity;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        permissionEnable();
        initWidgets();
    }

    public void permissionEnable() {
        permissionHelper = new PermissionHelper((Activity) this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_SMS", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS", "android.permission.VIBRATE"}, 100);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Log.d(TAG, "onPermissionGranted() called");
            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {
            }

            @Override
            public void onPermissionDenied() {
            }

            @Override
            public void onPermissionDeniedBySystem() {
            }
        });
    }

    private void initWidgets() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        dbhelper = new DBHelper(this);
        meterReading = (TextView) findViewById(R.id.meterreading);
        meterReading2 = (TextView) findViewById(R.id.meterreading2);
        meterReading3 = (TextView) findViewById(R.id.meterreading3);
        waterPercentage = (TextView) findViewById(R.id.waterPercentage);
        tankText = (TextView) findViewById(R.id.tank_text);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        if (dbhelper.getTankNames().size() > 0) {
            String tankUniqueId = Prefs.readString(context, "tuId", "").equalsIgnoreCase("")?dbhelper.getTankUniqueIndex(0):Prefs.readString(context, "tuId", "");
            Prefs.writeString(context, "tuId", dbhelper.getTankUniqueIndex(0));
            Double percentage = dbhelper.getTankPercentageByUid(tankUniqueId);
            String[] result2 = dbhelper.getLastTextTanklevel(tankUniqueId);
            meterReading.setText(result2[0]);
            meterReading3.setText(result2[1]);
            meterReading2.setText(result2[2]);
            waterPercentage.setText(convertPercentage(percentage));
            tankText.setText(dbhelper.getTankName(tankUniqueId));
            UpdateTank(percentage);
        }

        AccountHeader headerResult = new AccountHeaderBuilder().withActivity(this).withHeaderBackground((int) R.drawable.header).withOnAccountHeaderListener(new C05132()).build();
        Integer badgeColor = Integer.valueOf(getBaseContext().getResources().getColor(R.color.colorBlack));
        BadgeStyle badgeStyle = new BadgeStyle(badgeColor.intValue(), badgeColor.intValue()).withTextColor(getResources().getColor(R.color.colorRed));
        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withActivity(this);
        drawerBuilder.withToolbar(mToolbar);
        drawerBuilder.withAccountHeader(headerResult);
        drawerBuilder.withActionBarDrawerToggleAnimated(true);
        IDrawerItem[] iDrawerItemArr = new IDrawerItem[4];
        iDrawerItemArr[0] = (IDrawerItem) new PrimaryDrawerItem().withName("Add New Tank").withIcon(getResources().getDrawable(R.mipmap.ic_tank));;
        iDrawerItemArr[1] = (IDrawerItem) new PrimaryDrawerItem().withName("Water Use History").withIcon(getResources().getDrawable(R.mipmap.ic_graph));
        iDrawerItemArr[2] = (IDrawerItem) new PrimaryDrawerItem().withName("Water Logs").withIcon(getResources().getDrawable(R.mipmap.ic_data_log));
        iDrawerItemArr[3] = (IDrawerItem) ((ExpandableDrawerItem)((ExpandableDrawerItem) new ExpandableDrawerItem().withName("Settings")).withIdentifier(1)).withSubItems((IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(4)).withName("Controller Settings ").withIcon(getResources().getDrawable(R.mipmap.ic_ctrl_settings)), (IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(4).withIcon(getResources().getDrawable(R.mipmap.ic_reminders))).withName("Reminders")).withIcon(getResources().getDrawable(R.mipmap.ic_settings));;
        drawerBuilder.addDrawerItems(iDrawerItemArr);
        drawerBuilder.withOnDrawerItemClickListener(new C05143());
        drawerBuilder.withHeaderDivider(false);
        drawerBuilder.withActionBarDrawerToggle(true);
        drawerBuilder.withTranslucentStatusBar(true);
        drawerBuilder.withHeaderDivider(false);
        drawerBuilder.build();
    }

    public void UpdateTank(Double level) {
        linearLayout = (LinearLayout) findViewById(R.id.tankView);
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        FillableLoaderBuilder loaderBuilder = new FillableLoaderBuilder();
        fillableLoader = loaderBuilder.parentView(linearLayout)
                .svgPath(Paths.DRINKING_BOTTLE)
                .layoutParams(params)
                .originalDimensions(970, 1790)
                .strokeColor(Color.parseColor("#FFFFFF"))
                .fillColor(Color.parseColor("#E3F6FA"))
                .strokeDrawingDuration(0)
                .strokeWidth(6)
                .clippingTransform(new WavesClippingTransform())
                .fillDuration(2000)
                .percentage(Float.parseFloat(level.toString())>100?100:Float.parseFloat(level.toString()))
                .build();
        fillableLoader.setSvgPath(Paths.DRINKING_BOTTLE);
        fillableLoader.start();
    }



    public String convertPercentage(Double percentage){
        return (percentage==0)?"0":Integer.parseInt(StringUtils.substringBefore(String.valueOf(floor(percentage)),"."))>100?"100":StringUtils.substringBefore(String.valueOf(floor(percentage)),".")+"%";
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_messages);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_messages:
                new AsyncCallWS().execute(new Void[0]);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Utils.showShortToast(this, "Please click back again to exit").show();
        new Handler().postDelayed(new C03035(), 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_item1:
                startActivity(new Intent(context, SettingActivity.class));
                break;
            case R.id.menu_item2:
                startActivity(new Intent(context, CtrlsettingsActivity.class));
                break;
            case R.id.menu_item3:
                startActivity(new Intent(context, AlarmActivity.class));
                break;
            case R.id.btn_back:
                i--;
                if (i >= 0) {
                    //Utils.showShortToast(context,i+"").show();
                    String tankUniqueId = dbhelper.getTankUniqueIndex(i);
                    Prefs.writeString(context, "tuId", dbhelper.getTankUniqueIndex(i));
                    Double percentage = dbhelper.getTankPercentageByUid(tankUniqueId);
                    String[] result2 = dbhelper.getLastTextTanklevel(tankUniqueId);
                    meterReading.setText(result2[0]);
                    meterReading3.setText(result2[1]);
                    meterReading2.setText(result2[2]);
                    waterPercentage.setText(convertPercentage(percentage));
                    tankText.setText(dbhelper.getTankName(tankUniqueId));
                    UpdateTank(percentage);
                } else {
                    i = 0;
                    Utils.showShortToast(context, "First Tank").show();
                }
                break;
            case R.id.btn_next:
                i++;
                if (i < dbhelper.getTankCount()) {
                    //Utils.showShortToast(context,i+"").show();
                    String tankUniqueId = dbhelper.getTankUniqueIndex(i);
                    Prefs.writeString(context, "tuId", dbhelper.getTankUniqueIndex(i));
                    Double percentage = dbhelper.getTankPercentageByUid(tankUniqueId);
                    String[] result2 = dbhelper.getLastTextTanklevel(tankUniqueId);
                    meterReading.setText(result2[0]);
                    meterReading3.setText(result2[1]);
                    meterReading2.setText(result2[2]);
                    waterPercentage.setText(convertPercentage(percentage));
                    tankText.setText(dbhelper.getTankName(tankUniqueId));
                    UpdateTank(percentage);
                    break;
                } else {
                    i = dbhelper.getTankCount()-1;
                    Utils.showShortToast(context, "Last Tank").show();
                }
                break;
            default:
                return;
        }
    }
}
