package com.techbridge.smsreader.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
import com.techbridge.smsreader.utils.Utils;
import com.techbridge.smsreader.views.fragments.DayFragment;
import com.techbridge.smsreader.views.fragments.WeekFragment;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {
    private Context context = this;
    private boolean doubleBackToExitPressedOnce = false;
    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    class C03244 implements Runnable {
        C03244() {
        }

        public void run() {
            TabActivity.this.doubleBackToExitPressedOnce = false;
        }
    }


    class C03221 implements View.OnClickListener {
        C03221() {
        }

        public void onClick(View view) {
            startActivity(new Intent(context, DashboardActivity.class));
        }
    }


    class C05201 implements OnTabSelectedListener {
        C05201() {
        }

        public void onTabSelected(Tab tab) {
            int position = tab.getPosition();
        }

        public void onTabUnselected(Tab tab) {
        }

        public void onTabReselected(Tab tab) {
            int position = tab.getPosition();
        }
    }

    class C05212 implements OnAccountHeaderListener {
        C05212() {
        }

        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
            return false;
        }
    }

    class C05223 implements OnDrawerItemClickListener {
        C05223() {
        }

        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            Intent intent = null;
            if (drawerItem != null) {
                switch (position) {
                    case 1:
                        intent = new Intent(TabActivity.this.context, MainActivity.class);
                        break;
                    case 5:
                        intent = new Intent(TabActivity.this.context, SettingActivity.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
            return false;
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public Fragment getItem(int position) {
            return (Fragment) this.mFragmentList.get(position);
        }

        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return (CharSequence) this.mFragmentTitleList.get(position);
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.tab_layout);
        initWidgets();
        //initNavbar();
    }

    private void initWidgets() {
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_action_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Graphs");
        mToolbar.setNavigationOnClickListener(new C03221());
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(this.viewPager);
        this.tabLayout = (TabLayout) findViewById(R.id.tabs);
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.tabLayout.addOnTabSelectedListener(new C05201());
    }

    private void initNavbar() {
        AccountHeader headerResult = new AccountHeaderBuilder().withActivity(this).withHeaderBackground(R.drawable.header).withOnAccountHeaderListener(new C05212()).build();
        Integer badgeColor = Integer.valueOf(getBaseContext().getResources().getColor(R.color.colorBlack));
        BadgeStyle badgeStyle = new BadgeStyle(badgeColor.intValue(), badgeColor.intValue()).withTextColor(getResources().getColor(R.color.colorRed));
        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withActivity(this);
        drawerBuilder.withToolbar(this.mToolbar);
        drawerBuilder.withAccountHeader(headerResult);
        drawerBuilder.withActionBarDrawerToggleAnimated(true);
        IDrawerItem[] iDrawerItemArr = new IDrawerItem[4];
        iDrawerItemArr[0] = (IDrawerItem) new PrimaryDrawerItem().withName("Home");
        iDrawerItemArr[1] = (IDrawerItem) new PrimaryDrawerItem().withName("Tanks");
        iDrawerItemArr[2] = (IDrawerItem) new PrimaryDrawerItem().withName("About Us");
        //iDrawerItemArr[3] = (IDrawerItem) ((ExpandableDrawerItem) ((ExpandableDrawerItem) new ExpandableDrawerItem().withName("Settings")).withIdentifier(1)).withSubItems((IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(3)).withName("App Setting"), (IDrawerItem) ((SecondaryDrawerItem) ((SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(5)).withLevel(3)).withName("Controller Settings "));
        drawerBuilder.addDrawerItems(iDrawerItemArr);
        drawerBuilder.withOnDrawerItemClickListener(new C05223());
        drawerBuilder.withHeaderDivider(false);
        drawerBuilder.withActionBarDrawerToggle(true);
        drawerBuilder.withTranslucentStatusBar(true);
        drawerBuilder.withHeaderDivider(false);
        drawerBuilder.build();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DayFragment(), "DAILY VIEW");
        adapter.addFragment(new WeekFragment(), "WEEK VIEW");
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
    }

    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Utils.showShortToast(this, "Please click BACK again to exit").show();
        new Handler().postDelayed(new C03244(), 2000);
    }

    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
