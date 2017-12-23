package com.techbridge.smsreader.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.techbridge.smsreader.R;

public class LicencesActivity extends BaseActivity {
    private Toolbar mToolbar;

    class C03161 implements OnClickListener {
        C03161() {
        }

        public void onClick(View view) {
            LicencesActivity.this.onBackPressed();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.licenses);
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_licenses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        this.mToolbar.setNavigationOnClickListener(new C03161());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 82) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
