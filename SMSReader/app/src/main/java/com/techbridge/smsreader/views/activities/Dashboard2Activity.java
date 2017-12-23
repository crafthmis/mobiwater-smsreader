package com.techbridge.smsreader.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.master.permissionhelper.PermissionHelper;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeader.OnAccountHeaderListener;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.Utils;


public class Dashboard2Activity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private static Dashboard2Activity activity;
    private TextView batterytext;
    private Context context = this;
    public ArrayAdapter<String> dataAdapter;
    private DBHelper dbhelper;
    private boolean doubleBackToExitPressedOnce = false;
    private Toolbar mToolbar;
    private TextView meterReading;
    private Paint f62p = new Paint();
    private ProgressDialog pDialog;
    private ImageView percent10;
    private ImageView percent100;
    private ImageView percent20;
    private ImageView percent30;
    private ImageView percent40;
    private ImageView percent50;
    private ImageView percent60;
    private ImageView percent70;
    private ImageView percent80;
    private ImageView percent90;
    private PermissionHelper permissionHelper;
    private Spinner spinner;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;



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
            dbHelper = new DBHelper(Dashboard2Activity.this);
        }

        protected void onPreExecute() {
            pDialog = new ProgressDialog(Dashboard2Activity.this);
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
                        intent = new Intent(context, GraphActivity.class);
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

    public static Dashboard2Activity instance() {
        return activity;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_dashboard);
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
        percent100 = (ImageView) findViewById(R.id.percent100);
        percent90 = (ImageView) findViewById(R.id.percent90);
        percent80 = (ImageView) findViewById(R.id.percent80);
        percent70 = (ImageView) findViewById(R.id.percent70);
        percent60 = (ImageView) findViewById(R.id.percent60);
        percent50 = (ImageView) findViewById(R.id.percent50);
        percent40 = (ImageView) findViewById(R.id.percent40);
        percent30 = (ImageView) findViewById(R.id.percent30);
        percent20 = (ImageView) findViewById(R.id.percent20);
        percent10 = (ImageView) findViewById(R.id.percent10);
        batterytext = (TextView) findViewById(R.id.batterytext);
        spinner = (Spinner) findViewById(R.id.spinner);

        fab1 = (FloatingActionButton)findViewById(R.id.menu_item1);
        fab2 = (FloatingActionButton)findViewById(R.id.menu_item2);
        fab3 = (FloatingActionButton)findViewById(R.id.menu_item3);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);


        AccountHeader headerResult = new AccountHeaderBuilder().withActivity(this).withHeaderBackground((int) R.drawable.header).withOnAccountHeaderListener(new C05132()).build();
        Integer badgeColor = Integer.valueOf(getBaseContext().getResources().getColor(R.color.colorBlack));
        BadgeStyle badgeStyle = new BadgeStyle(badgeColor.intValue(), badgeColor.intValue()).withTextColor(getResources().getColor(R.color.colorRed));
        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withActivity(this);
        drawerBuilder.withToolbar(mToolbar);
        drawerBuilder.withAccountHeader(headerResult);
        drawerBuilder.withActionBarDrawerToggleAnimated(true);
        IDrawerItem[] iDrawerItemArr = new IDrawerItem[3];
        iDrawerItemArr[0] = (IDrawerItem) new PrimaryDrawerItem().withName("History");
        iDrawerItemArr[1] = (IDrawerItem) new PrimaryDrawerItem().withName("Past Data");
        //iDrawerItemArr[2] = (IDrawerItem) ((ExpandableDrawerItem) ((ExpandableDrawerItem) new ExpandableDrawerItem().withName("Settings")).withIdentifier(1)).withSubItems((IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(3)).withName("Tank Settings"), (IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(3)).withName("Controller Settings "), (IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(3)).withName("Alarm Settings "));
        drawerBuilder.addDrawerItems(iDrawerItemArr);
        drawerBuilder.withOnDrawerItemClickListener(new C05143());
        drawerBuilder.withHeaderDivider(false);
        drawerBuilder.withActionBarDrawerToggle(true);
        drawerBuilder.withTranslucentStatusBar(true);
        drawerBuilder.withHeaderDivider(false);
        drawerBuilder.build();
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

    private void UpdateTank(Double level) {
        int i = View.INVISIBLE;
        int finalLevel = (int) Math.floor(level.doubleValue());
        percent100.setVisibility(View.INVISIBLE);
        percent90.setVisibility(View.INVISIBLE);
        percent80.setVisibility(View.INVISIBLE);
        percent70.setVisibility(View.INVISIBLE);
        percent60.setVisibility(View.INVISIBLE);
        percent50.setVisibility(View.INVISIBLE);
        percent40.setVisibility(View.INVISIBLE);
        percent30.setVisibility(View.INVISIBLE);
        percent20.setVisibility(View.INVISIBLE);
        percent10.setVisibility(View.INVISIBLE);
        ImageView imageView;
        if (level.doubleValue() <= 100.0d && level.doubleValue() > 90.0d) {
            imageView = percent100;
            if (percent100.getVisibility() != View.VISIBLE) {
                i = View.VISIBLE;
            }
            imageView.setVisibility(i);
        } else if (level.doubleValue() <= 90.0d && level.doubleValue() > 80.0d) {
            imageView = percent90;
            if (percent90.getVisibility() != View.VISIBLE) {
                i = View.VISIBLE;
            }
            imageView.setVisibility(i);
        } else if (level.doubleValue() <= 80.0d && level.doubleValue() > 70.0d) {
            imageView = percent80;
            if (percent80.getVisibility() != View.VISIBLE) {
                i = View.VISIBLE;
            }
            imageView.setVisibility(i);
        } else if (level.doubleValue() <= 70.0d && level.doubleValue() > 60.0d) {
            imageView = percent70;
            if (percent70.getVisibility() != View.VISIBLE) {
                i = View.VISIBLE;
            }
            imageView.setVisibility(i);
        } else if (level.doubleValue() <= 60.0d && level.doubleValue() > 50.0d) {
            imageView = percent60;
            if (percent60.getVisibility() != View.VISIBLE) {
                i = View.VISIBLE;
            }
            imageView.setVisibility(i);
        } else if (level.doubleValue() <= 50.0d && level.doubleValue() > 40.0d) {
            imageView = percent50;
            if (percent50.getVisibility() != View.VISIBLE) {
                i = View.VISIBLE;
            }
            imageView.setVisibility(i);
        } else if (level.doubleValue() <= 40.0d && level.doubleValue() > 30.0d) {
            imageView = percent40;
            if (percent40.getVisibility() != View.VISIBLE) {
                i = View.VISIBLE;
            }
            imageView.setVisibility(i);
        } else if (level.doubleValue() <= 30.0d && level.doubleValue() > 20.0d) {
            imageView = percent30;
            if (percent30.getVisibility() != View.VISIBLE) {
                i = View.VISIBLE;
            }
            imageView.setVisibility(i);
        } else if (level.doubleValue() <= 20.0d && level.doubleValue() > 10.0d) {
            imageView = percent20;
            if (percent20.getVisibility() != View.VISIBLE) {
                i = View.VISIBLE;
            }
            imageView.setVisibility(i);
        } else if (level.doubleValue() <= 10.0d && level.doubleValue() > 0.0d) {
            imageView = percent10;
            if (percent10.getVisibility() != View.VISIBLE) {
                i = View.INVISIBLE;
            }
            imageView.setVisibility(i);
        }
        batterytext.setText(String.valueOf(finalLevel) + " %");
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
        dataAdapter.notifyDataSetChanged();
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
        switch (v.getId()){
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
