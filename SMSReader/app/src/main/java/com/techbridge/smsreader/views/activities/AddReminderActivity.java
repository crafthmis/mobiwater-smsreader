package com.techbridge.smsreader.views.activities;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;

public class AddReminderActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
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
    private Calendar mCalendar;
    private String mDate;
    private TextView mDateText;
    private int mDay;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private int mHour;
    private int mMinute;
    private int mMonth;
    private String mRepeat;
    private String mRepeatNo;
    private TextView mRepeatNoText;
    private TextView mRepeatText;
    private long mRepeatTime;
    private String mRepeatType;
    private TextView mRepeatTypeText;
    private String mTime;
    private TextView mTimeText;
    private String mTitle;
    private EditText mTitleText;
    private Toolbar mToolbar;
    private int mYear;



    class C02901 implements TextWatcher {
        C02901() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mTitle = s.toString().trim();
            mTitleText.setError(null);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    class C02934 implements OnClickListener {
        C02934() {
        }

        public void onClick(DialogInterface dialog, int whichButton) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_add_reminder);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mTitleText = (EditText)findViewById(R.id.reminder_title);
        mDateText = (TextView)findViewById(R.id.set_date);
        mTimeText = (TextView)findViewById(R.id.set_time);
        mRepeatText = (TextView)findViewById(R.id.set_repeat);
        mRepeatNoText = (TextView)findViewById(R.id.set_repeat_no);
        mRepeatTypeText = (TextView)findViewById(R.id.set_repeat_type);
        mFAB1 = (FloatingActionButton)findViewById(R.id.starred1);
        mFAB2 = (FloatingActionButton)findViewById(R.id.starred2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle((int) R.string.title_activity_add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Day";
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;
        mTitleText.addTextChangedListener(new C02901());
        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        mRepeatText.setText("Every " + mRepeatNo + StringUtils.SPACE + mRepeatType + "(s)");
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;
            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;
            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;
            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
            mRepeat = saveRepeat;
            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;
            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            mRepeatTypeText.setText(savedRepeatType);
            mRepeatType = savedRepeatType;
            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }
        if (mActive.equals("false")) {
            mFAB1.setVisibility(View.VISIBLE);
            mFAB2.setVisibility(View.INVISIBLE);
        } else if (mActive.equals("true")) {
            mFAB1.setVisibility(View.INVISIBLE);
            mFAB2.setVisibility(View.VISIBLE);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
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
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.setText(mTime);
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        mDateText.setText(mDate);
    }

    public void selectFab1(View v) {
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.INVISIBLE);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = "true";
    }

    public void selectFab2(View v) {
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.INVISIBLE);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = "false";
    }

    public void onSwitchRepeat(View view) {
        if (((Switch) view).isChecked()) {
            mRepeat = "true";
            mRepeatText.setText("Every " + mRepeatNo + StringUtils.SPACE + mRepeatType + "(s)");
            return;
        }
        mRepeat = "false";
        mRepeatText.setText(R.string.repeat_off);
    }

    public void selectRepeatType(View v) {
        final String[] items = new String[]{"Minute", "Hour", "Day", "Week", "Month"};
        Builder builder = new Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText("Every " + mRepeatNo + StringUtils.SPACE + mRepeatType + "(s)");
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
                    mRepeatNo = Integer.toString(1);
                    mRepeatNoText.setText(mRepeatNo);
                    mRepeatText.setText("Every " + mRepeatNo + StringUtils.SPACE + mRepeatType + "(s)");
                    return;
                }
                mRepeatNo = input.getText().toString().trim();
                mRepeatNoText.setText(mRepeatNo);
                mRepeatText.setText("Every " + mRepeatNo + StringUtils.SPACE + mRepeatType + "(s)");
            }
        });
        alert.setNegativeButton("Cancel", new C02934());
        alert.show();
    }

    public void saveReminder() {
        int ID = new DBHelper(this).addReminder(new Reminder(mTitle, mDate, mTime, mRepeat, mRepeatNo, mRepeatType, mActive));
        Calendar calendar = mCalendar;
        int i = mMonth - 1;
        mMonth = i;
        calendar.set(Calendar.MONTH, i);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);
        if (mRepeatType.equals("Minute")) {
            mRepeatTime = ((long) Integer.parseInt(mRepeatNo)) * 60000;
        } else if (mRepeatType.equals("Hour")) {
            mRepeatTime = ((long) Integer.parseInt(mRepeatNo)) * 3600000;
        } else if (mRepeatType.equals("Day")) {
            mRepeatTime = ((long) Integer.parseInt(mRepeatNo)) * 86400000;
        } else if (mRepeatType.equals("Week")) {
            mRepeatTime = ((long) Integer.parseInt(mRepeatNo)) * milWeek;
        } else if (mRepeatType.equals("Month")) {
            mRepeatTime = ((long) Integer.parseInt(mRepeatNo)) * milMonth;
        }
        if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                new AlarmReceiver().setRepeatAlarm(getApplicationContext(), mCalendar, ID, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);
            }
        }
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
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

            // On clicking the back arrow
            // Discard any changes
            case android.R.id.home:
                onBackPressed();
                return true;

            // On clicking save reminder button
            // Update reminder
            case R.id.save_reminder:
                mTitleText.setText(mTitle);

                if (mTitleText.getText().toString().length() == 0)
                    mTitleText.setError("Reminder Title cannot be blank!");

                else {
                    saveReminder();
                }
                return true;

            // On clicking discard reminder button
            // Discard any changes
            case R.id.discard_reminder:
                Toast.makeText(getApplicationContext(), "Discarded",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
