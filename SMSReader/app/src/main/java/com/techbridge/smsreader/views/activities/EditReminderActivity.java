package com.techbridge.smsreader.views.activities;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.utils.AlarmReceiver;
import com.techbridge.smsreader.utils.Reminder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;

public class EditReminderActivity extends AppCompatActivity implements OnTimeSetListener, OnDateSetListener {
    public static final String EXTRA_REMINDER_ID = "Reminder_ID";
    private static final String KEY_ACTIVE = "active_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_TITLE = "title_key";
    private static final long milDay = 86400000;
    private static final long milHour = 3600000;
    private static final long milMinute = 60000;
    private static final long milMonth = 2592000000L;
    private static final long milWeek = 604800000;
    private String mActive;
    private AlarmReceiver mAlarmReceiver;
    private Calendar mCalendar;
    private String mDate;
    private String[] mDateSplit;
    private TextView mDateText;
    private int mDay;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private int mHour;
    private int mMinute;
    private int mMonth;
    private int mReceivedID;
    private Reminder mReceivedReminder;
    private String mRepeat;
    private String mRepeatNo;
    private TextView mRepeatNoText;
    private Switch mRepeatSwitch;
    private TextView mRepeatText;
    private long mRepeatTime;
    private String mRepeatType;
    private TextView mRepeatTypeText;
    private String mTime;
    private String[] mTimeSplit;
    private TextView mTimeText;
    private String mTitle;
    private EditText mTitleText;
    private Toolbar mToolbar;
    private int mYear;
    private DBHelper rb;



    class C03041 implements TextWatcher {
        C03041() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            EditReminderActivity.this.mTitle = s.toString().trim();
            EditReminderActivity.this.mTitleText.setError(null);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    class C03074 implements OnClickListener {
        C03074() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_add_reminder);
        this.mToolbar = (Toolbar) findViewById(R.id.toolbar);
        this.mTitleText = (EditText) findViewById(R.id.reminder_title);
        this.mDateText = (TextView) findViewById(R.id.set_date);
        this.mTimeText = (TextView) findViewById(R.id.set_time);
        this.mRepeatText = (TextView) findViewById(R.id.set_repeat);
        this.mRepeatNoText = (TextView) findViewById(R.id.set_repeat_no);
        this.mRepeatTypeText = (TextView) findViewById(R.id.set_repeat_type);
        this.mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        this.mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        this.mRepeatSwitch = (Switch) findViewById(R.id.repeat_switch);
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setTitle((int) R.string.title_activity_edit_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        this.mTitleText.addTextChangedListener(new C03041());
        this.mReceivedID = Integer.parseInt(getIntent().getStringExtra(EXTRA_REMINDER_ID));
        this.rb = new DBHelper(this);
        this.mReceivedReminder = this.rb.getReminder(this.mReceivedID);
        this.mTitle = this.mReceivedReminder.getTitle();
        this.mDate = this.mReceivedReminder.getDate();
        this.mTime = this.mReceivedReminder.getTime();
        this.mRepeat = this.mReceivedReminder.getRepeat();
        this.mRepeatNo = this.mReceivedReminder.getRepeatNo();
        this.mRepeatType = this.mReceivedReminder.getRepeatType();
        this.mActive = this.mReceivedReminder.getActive();
        this.mTitleText.setText(this.mTitle);
        this.mDateText.setText(this.mDate);
        this.mTimeText.setText(this.mTime);
        this.mRepeatNoText.setText(this.mRepeatNo);
        this.mRepeatTypeText.setText(this.mRepeatType);
        this.mRepeatText.setText("Every " + this.mRepeatNo + StringUtils.SPACE + this.mRepeatType + "(s)");
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            this.mTitleText.setText(savedTitle);
            this.mTitle = savedTitle;
            String savedTime = savedInstanceState.getString(KEY_TIME);
            this.mTimeText.setText(savedTime);
            this.mTime = savedTime;
            String savedDate = savedInstanceState.getString(KEY_DATE);
            this.mDateText.setText(savedDate);
            this.mDate = savedDate;
            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            this.mRepeatText.setText(saveRepeat);
            this.mRepeat = saveRepeat;
            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            this.mRepeatNoText.setText(savedRepeatNo);
            this.mRepeatNo = savedRepeatNo;
            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            this.mRepeatTypeText.setText(savedRepeatType);
            this.mRepeatType = savedRepeatType;
            this.mActive = savedInstanceState.getString(KEY_ACTIVE);
        }
        // Setup up active buttons
        if (mActive.equals("false")) {
            mFAB1.setVisibility(View.VISIBLE);
            mFAB2.setVisibility(View.GONE);

        } else if (mActive.equals("true")) {
            mFAB1.setVisibility(View.GONE);
            mFAB2.setVisibility(View.VISIBLE);
        }
        if (this.mRepeat.equals("false")) {
            this.mRepeatSwitch.setChecked(false);
            this.mRepeatText.setText(R.string.repeat_off);
        } else if (this.mRepeat.equals("true")) {
            this.mRepeatSwitch.setChecked(true);
        }
        this.mCalendar = Calendar.getInstance();
        this.mAlarmReceiver = new AlarmReceiver();
        this.mDateSplit = this.mDate.split("/");
        this.mTimeSplit = this.mTime.split(":");
        this.mDay = Integer.parseInt(this.mDateSplit[0]);
        this.mMonth = Integer.parseInt(this.mDateSplit[1]);
        this.mYear = Integer.parseInt(this.mDateSplit[2]);
        this.mHour = Integer.parseInt(this.mTimeSplit[0]);
        this.mMinute = Integer.parseInt(this.mTimeSplit[1]);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_TITLE, this.mTitleText.getText());
        outState.putCharSequence(KEY_TIME, this.mTimeText.getText());
        outState.putCharSequence(KEY_DATE, this.mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, this.mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, this.mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, this.mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, this.mActive);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void setTime(View v) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public void setDate(View v) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "Datepickerdialog");
    }
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        this.mHour = hourOfDay;
        this.mMinute = minute;
        if (minute < 10) {
            this.mTime = hourOfDay + ":0" + minute;
        } else {
            this.mTime = hourOfDay + ":" + minute;
        }
        this.mTimeText.setText(this.mTime);
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        this.mDay = dayOfMonth;
        this.mMonth = monthOfYear;
        this.mYear = year;
        this.mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        this.mDateText.setText(this.mDate);
    }

    public void selectFab1(View v) {
        this.mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        this.mFAB1.setVisibility(View.VISIBLE);
        this.mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        this.mFAB2.setVisibility(View.GONE);
        this.mActive = "true";
    }

    public void selectFab2(View v) {
        this.mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        this.mFAB2.setVisibility(View.GONE);
        this.mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        this.mFAB1.setVisibility(View.VISIBLE);
        this.mActive = "false";
    }

    public void onSwitchRepeat(View view) {
        if (((Switch) view).isChecked()) {
            this.mRepeat = "true";
            this.mRepeatText.setText("Every " + this.mRepeatNo + StringUtils.SPACE + this.mRepeatType + "(s)");
            return;
        }
        this.mRepeat = "false";
        this.mRepeatText.setText(R.string.repeat_off);
    }

    public void selectRepeatType(View v) {
        final String[] items = new String[]{"Minute", "Hour", "Day", "Week", "Month"};
        Builder builder = new Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                EditReminderActivity.this.mRepeatType = items[item];
                EditReminderActivity.this.mRepeatTypeText.setText(EditReminderActivity.this.mRepeatType);
                EditReminderActivity.this.mRepeatText.setText("Every " + EditReminderActivity.this.mRepeatNo + StringUtils.SPACE + EditReminderActivity.this.mRepeatType + "(s)");
            }
        });
        builder.create().show();
    }

    public void setRepeatNo(View v) {
        Builder alert = new Builder(this);
        alert.setTitle("Enter Number");
        final EditText input = new EditText(this);
        input.setInputType(2);
        alert.setView(input);
        alert.setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (input.getText().toString().length() == 0) {
                    EditReminderActivity.this.mRepeatNo = Integer.toString(1);
                    EditReminderActivity.this.mRepeatNoText.setText(EditReminderActivity.this.mRepeatNo);
                    EditReminderActivity.this.mRepeatText.setText("Every " + EditReminderActivity.this.mRepeatNo + StringUtils.SPACE + EditReminderActivity.this.mRepeatType + "(s)");
                    return;
                }
                EditReminderActivity.this.mRepeatNo = input.getText().toString().trim();
                EditReminderActivity.this.mRepeatNoText.setText(EditReminderActivity.this.mRepeatNo);
                EditReminderActivity.this.mRepeatText.setText("Every " + EditReminderActivity.this.mRepeatNo + StringUtils.SPACE + EditReminderActivity.this.mRepeatType + "(s)");
            }
        });
        alert.setNegativeButton("Cancel", new C03074());
        alert.show();
    }

    public void updateReminder() {
        this.mReceivedReminder.setTitle(this.mTitle);
        this.mReceivedReminder.setDate(this.mDate);
        this.mReceivedReminder.setTime(this.mTime);
        this.mReceivedReminder.setRepeat(this.mRepeat);
        this.mReceivedReminder.setRepeatNo(this.mRepeatNo);
        this.mReceivedReminder.setRepeatType(this.mRepeatType);
        this.mReceivedReminder.setActive(this.mActive);
        this.rb.updateReminder(this.mReceivedReminder);
        Calendar calendar = this.mCalendar;
        int i = this.mMonth - 1;
        this.mMonth = i;
        calendar.set(Calendar.MONTH, i);
        this.mCalendar.set(Calendar.YEAR, this.mYear);
        this.mCalendar.set(Calendar.DAY_OF_MONTH, this.mDay);
        this.mCalendar.set(Calendar.HOUR, this.mHour);
        this.mCalendar.set(Calendar.MINUTE, this.mMinute);
        this.mCalendar.set(Calendar.SECOND, 0);
        this.mAlarmReceiver.cancelAlarm(getApplicationContext(), this.mReceivedID);
        if (this.mRepeatType.equals("Minute")) {
            this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * 60000;
        } else if (this.mRepeatType.equals("Hour")) {
            this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * 3600000;
        } else if (this.mRepeatType.equals("Day")) {
            this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * 86400000;
        } else if (this.mRepeatType.equals("Week")) {
            this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * milWeek;
        } else if (this.mRepeatType.equals("Month")) {
            this.mRepeatTime = ((long) Integer.parseInt(this.mRepeatNo)) * milMonth;
        }
        if (this.mActive.equals("true")) {
            if (this.mRepeat.equals("true")) {
                this.mAlarmReceiver.setRepeatAlarm(getApplicationContext(), this.mCalendar, this.mReceivedID, this.mRepeatTime);
            } else if (this.mRepeat.equals("false")) {
                this.mAlarmReceiver.setAlarm(getApplicationContext(), this.mCalendar, this.mReceivedID);
            }
        }
        Toast.makeText(getApplicationContext(), "Edited", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            case R.id.discard_reminder:
                Toast.makeText(getApplicationContext(), "Changes Discarded", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return true;
            case R.id.save_reminder:
                this.mTitleText.setText(this.mTitle);
                if (this.mTitleText.getText().toString().length() == 0) {
                    this.mTitleText.setError("Reminder Title cannot be blank!");
                    return true;
                }
                updateReminder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
