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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
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
import com.techbridge.smsreader.utils.BackgroundService;
import com.techbridge.smsreader.utils.Paths;
import com.techbridge.smsreader.utils.Utils;



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
    private TextView meterReading;
    private Paint f62p = new Paint();
    private ProgressDialog pDialog;
    private PermissionHelper permissionHelper;
    private Spinner spinner;
    private LinearLayout linearLayout;

    class C03024 implements OnItemSelectedListener {
        C03024() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            if (dbhelper.getTankNames().size() > 0) {
                String tankUniqueId = dbhelper.getTankUniqueIndex(position);
                Double percentage = dbhelper.getTankPercentageByUid(tankUniqueId);
                meterReading.setText(dbhelper.getLastTextTanklevel(tankUniqueId));
                UpdateTank(percentage);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

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
                loadSpinnerData();
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
                        intent = new Intent(context, TabActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, HistoricaldataActivity.class);
                        break;
                    case 4:
                        intent = new Intent(context, SettingActivity.class);
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
        loadSpinnerData();
        //startService(new Intent(context, BackgroundService.class));
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
        mToolbar.setTitle("Home");
        setSupportActionBar(mToolbar);
        dbhelper = new DBHelper(this);
        meterReading = (TextView) findViewById(R.id.meterreading);
        //batterytext = (TextView) findViewById(R.id.batterytext);
        spinner = (Spinner) findViewById(R.id.spinner);


        /*fab1 = (FloatingActionButton)findViewById(R.id.menu_item1);
        fab2 = (FloatingActionButton)findViewById(R.id.menu_item2);
        fab3 = (FloatingActionButton)findViewById(R.id.menu_item3);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);*/


        AccountHeader headerResult = new AccountHeaderBuilder().withActivity(this).withHeaderBackground((int) R.drawable.header).withOnAccountHeaderListener(new C05132()).build();
        Integer badgeColor = Integer.valueOf(getBaseContext().getResources().getColor(R.color.colorBlack));
        BadgeStyle badgeStyle = new BadgeStyle(badgeColor.intValue(), badgeColor.intValue()).withTextColor(getResources().getColor(R.color.colorRed));
        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withActivity(this);
        drawerBuilder.withToolbar(mToolbar);
        drawerBuilder.withAccountHeader(headerResult);
        drawerBuilder.withActionBarDrawerToggleAnimated(true);
        IDrawerItem[] iDrawerItemArr = new IDrawerItem[3];
        iDrawerItemArr[0] = (IDrawerItem) new PrimaryDrawerItem().withName("Graphs");
        iDrawerItemArr[1] = (IDrawerItem) new PrimaryDrawerItem().withName("Past Data");
        iDrawerItemArr[2] = (IDrawerItem) ((ExpandableDrawerItem) ((ExpandableDrawerItem) new ExpandableDrawerItem().withName("Settings")).withIdentifier(1)).withSubItems((IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(3)).withName("Tank Settings"), (IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(3)).withName("Controller Settings "), (IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(3)).withName("Alarm Settings "));
        drawerBuilder.addDrawerItems(iDrawerItemArr);
        drawerBuilder.withOnDrawerItemClickListener(new C05143());
        drawerBuilder.withHeaderDivider(false);
        drawerBuilder.withActionBarDrawerToggle(true);
        drawerBuilder.withTranslucentStatusBar(true);
        drawerBuilder.withHeaderDivider(false);
        drawerBuilder.build();
    }

    public void UpdateTank(Double level){
        linearLayout = (LinearLayout) findViewById(R.id.tankView);
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        FillableLoaderBuilder loaderBuilder = new FillableLoaderBuilder();
        fillableLoader = loaderBuilder.parentView(linearLayout)
                .svgPath(Paths.JAR)
                .layoutParams(params)
                .originalDimensions(700, 970)
                .strokeColor(Color.parseColor("#673AB7"))
                .fillColor(Color.parseColor("#E3F6FA"))
                .strokeDrawingDuration(0)
                .clippingTransform(new WavesClippingTransform())
                .fillDuration(2000)
                .percentage(Float.parseFloat(level.toString()))
                .build();
        fillableLoader.setSvgPath(Paths.JAR);
        fillableLoader.start();
    }

    public void loadSpinnerData() {
        if (dbhelper.getTankNames().size() > 0) {
            dataAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, dbhelper.getTankNames());
            dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            dataAdapter.notifyDataSetChanged();
            spinner.setAdapter(dataAdapter);
        }
        spinner.setOnItemSelectedListener(new C03024());
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
        //dataAdapter.notifyDataSetChanged();
        spinner.setAdapter(dataAdapter);
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
        }
    }
}
