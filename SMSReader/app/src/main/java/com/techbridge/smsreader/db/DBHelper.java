package com.techbridge.smsreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.opencsv.CSVWriter;
import com.techbridge.smsreader.R;
import com.techbridge.smsreader.models.Month;
import com.techbridge.smsreader.models.MonthDetails;
import com.techbridge.smsreader.models.Setting;
import com.techbridge.smsreader.models.TankLevel;
import com.techbridge.smsreader.utils.Reminder;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static java.util.Calendar.DAY_OF_MONTH;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db_tank";
    public static final int DB_VERSION = 1;
    public static final int DB_VERSION2 = 2;
    private static final String KEY_ACTIVE = "active";
    private static final String KEY_DATE = "date";
    private static final String KEY_ID = "id";
    private static final String KEY_REPEAT = "repeat";
    private static final String KEY_REPEAT_NO = "repeat_no";
    private static final String KEY_REPEAT_TYPE = "repeat_type";
    private static final String KEY_TIME = "time";
    private static final String KEY_TITLE = "title";
    private static final String TABLE_REMINDERS = "tbl_reminders";
    public static SQLiteDatabase db;
    private final String BASE_AREA = "base_area";
    private final String BOTTOM_HEIGHT = "bottom_height";
    private final String LOWER_THRESHOLD = "lower_threshold";
    private final String TABLE_TANK = "tbl_tank_level";
    private final String TABLE_TANK_SETTINGS = "tbl_tank_settings";
    private final String TANK_ID = "tnk_id";
    private final String TANK_LEVEL = "tank_level";
    private final String TANK_MSISDN = "tank_msisdn";
    private final String TANK_NAME = "tank_name";
    private final String TANK_TIME = "tank_time";
    private final String TANK_UNIQUE_ID = "tnk_uid";
    private final String TOP_HEIGHT = "top_height";
    private final String UPPER_THRESHOLD = "upper_threshold";
    private final Context context;
    private GraphicalView mChartView;
    private String[] monthNames = new String[]{"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 2);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_TANK_LEVEL = " CREATE  TABLE tbl_tank_level ( tnk_id INTEGER PRIMARY KEY AUTOINCREMENT, tnk_uid INTEGER , tank_level TEXT DEFAULT 0, tank_time TEXT   ); CREATE INDEX tnk_uid_tank_level_tank_time_idx ON tbl_tank_level (tnk_uid,tank_level,tank_time); CREATE INDEX tank_level_tank_time_idx ON tbl_tank_level (tank_level,tank_time); CREATE INDEX tnk_uid_tank_time_idx ON tbl_tank_level (tnk_uid,tank_time); CREATE INDEX tnk_uid_tank_level_idx ON tbl_tank_level (tnk_uid,tank_level); CREATE INDEX tank_level_idx ON tbl_tank_level (tank_level); CREATE INDEX tank_time_idx ON tbl_tank_level (tank_time);";
        String CREATE_TABLE_TANK_SETTINGS = " CREATE  TABLE tbl_tank_settings ( tnk_uid INTEGER PRIMARY KEY AUTOINCREMENT, tank_name TEXT , top_height TEXT DEFAULT 0, bottom_height INTEGER DEFAULT 0 , upper_threshold TEXT DEFAULT 0 , lower_threshold TEXT DEFAULT 0 , tank_msisdn TEXT  , base_area TEXT DEFAULT 0 )";
        String CREATE_REMINDERS_TABLE = "CREATE TABLE tbl_reminders(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,date TEXT,time INTEGER,repeat BOOLEAN,repeat_no INTEGER,repeat_type TEXT,active BOOLEAN)";
        db.beginTransaction();
        try {
            db.execSQL(CREATE_TABLE_TANK_LEVEL);
            db.execSQL(CREATE_TABLE_TANK_SETTINGS);
            db.execSQL(CREATE_REMINDERS_TABLE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<TankLevel> getWaterLevelData() {
        db = getReadableDatabase();
        ArrayList<TankLevel> dataTankItems = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_TANK, new String[]{TANK_LEVEL, TANK_TIME, TANK_UNIQUE_ID}, null, null, null, null, "tnk_id desc");
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    TankLevel tl = new TankLevel();
                    tl.setLevel(cursor.getString(0));
                    tl.setTimes(cursor.getString(1));
                    tl.setuId(cursor.getString(2));
                    dataTankItems.add(tl);
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dataTankItems;
    }

    public void addEntry(String uId, String tankLevel, String tankTime) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TANK_UNIQUE_ID, uId);
        values.put(TANK_LEVEL, tankLevel);
        values.put(TANK_TIME, tankTime);
        try {
            db.insert(TABLE_TANK, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void addSetting(String tankName, String tankTopHght, String tankUpperThshld, String tankLowerThshld, String Msisdn, String baseArea) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TANK_NAME, tankName);
        values.put(TOP_HEIGHT, tankTopHght);
        values.put(UPPER_THRESHOLD, tankUpperThshld);
        values.put(LOWER_THRESHOLD, tankLowerThshld);
        values.put(TANK_MSISDN, Msisdn);
        values.put(BASE_AREA, baseArea);
        try {
            db.insert(TABLE_TANK_SETTINGS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getTankNames() {
        db = getReadableDatabase();
        ArrayList<String> dataSettings = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_TANK_SETTINGS, new String[]{TANK_NAME}, null, null, null, null, "tnk_uid asc");
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    dataSettings.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dataSettings;
    }
    public void readInbox(String rawMsisdn)   {
        db = getWritableDatabase();
        String str1 = "+254" + StringUtils.substring(rawMsisdn, rawMsisdn.length() - 9, rawMsisdn.length());
        Cursor cursor = null;
        try
        {
            Uri uri = Uri.parse("content://sms/inbox");
            String str2 = "address = '" + str1 + "'";
            cursor = context.getContentResolver().query(uri, new String[] { "address", "person", "body", "date", "type" }, str2, null, "date asc");
            cursor.moveToFirst();
            if (cursor!=null)      {
                do
                {
                    String sms = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    String meterReading = StringUtils.substringBefore(sms , ";").replaceAll("[^0-9.]", "");
                    String strDate = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    long l1 = Long.parseLong(strDate);
                    long l2 = getLastTimeByMsisdn(rawMsisdn);
                    if ((l1 > l2) && (!meterReading.equals("") || meterReading != null) && !sms.toLowerCase().contains("ac power") || !sms.toLowerCase().contains("rtu power") || !sms.toLowerCase().contains("host arm") || !sms.toLowerCase().contains("daily sms report") || !sms.toLowerCase().contains("(y)"))
                    {
                        addEntry(getTankUidByMsisdn(str1), meterReading, strDate);
                    }

                } while (cursor.moveToNext());
            }
            if (cursor != null)
            {
                cursor.close();
                return;
            }
        }
        catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public String getTankUniqueIndex(int index) {
        db = getReadableDatabase();
        ArrayList<String> dataSettings = new ArrayList();
        String tankUid = "";
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_TANK_SETTINGS, new String[]{TANK_UNIQUE_ID}, null, null, null, null, "tnk_uid asc");
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                int i = 0;
                do {
                    dataSettings.add(i, String.valueOf(cursor.getInt(0)));
                    i++;
                } while (cursor.moveToNext());
            }
            tankUid = dataSettings.get(index).toString();
            cursor.close();
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tankUid;
    }

    public long getLastTimeByMsisdn(String rawMsisdn) {
        db = getReadableDatabase();
        long lastRecord = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(("SELECT MAX(tank_time) " + " FROM tbl_tank_level") + " WHERE tnk_uid = " + StringUtils.trim(getTankUidByMsisdn(rawMsisdn)), null);
            cursor.moveToFirst();
            lastRecord = cursor.getLong(0);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return lastRecord;
    }

    public Double getLastTanklevel(String uId) {
        db = getReadableDatabase();
        Double lastRecord = Double.valueOf(0.0d);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(("SELECT MAX(tank_time), tank_level" + " FROM tbl_tank_level") + " WHERE tnk_uid = " + StringUtils.trim(uId) + " LIMIT 1", null);
            cursor.moveToFirst();
            lastRecord = Double.valueOf(Double.parseDouble(cursor.getString(1)));
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return (lastRecord==null)?0.00d:lastRecord;
    }

    public String getTankName(String uId) {
        db = getReadableDatabase();
        String tankName = "";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(("SELECT tank_name" + " FROM tbl_tank_settings") + " WHERE tnk_uid = " + StringUtils.trim(uId) + " LIMIT 1", null);
            cursor.moveToFirst();
            tankName = cursor.getString(0);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return tankName;
    }

    public String getLastTextTanklevel(String uId) {
        db = getReadableDatabase();
        String result = "";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(("SELECT MAX(tank_time) ,tank_level" + " FROM tbl_tank_level") + " WHERE tnk_uid = " + StringUtils.trim(uId), null);
            cursor.moveToFirst();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+3"));
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            SimpleDateFormat sdf = new SimpleDateFormat("E dd/MM/yyyy  hh:mm a");
            DecimalFormat dcf = new DecimalFormat("#.00");
            Date netDate = new Date(Long.parseLong(cursor.getString(0)));
            String[] dayRange = getDayRange(0).split("-");
            result = String.valueOf("Water Level  = " + Double.parseDouble(cursor.getString(1)) + " Metres \nReading Time = " + sdf.format(netDate) + "\n Amount of water left = " + dcf.format(getTankVolumeByUid(uId)) + " Litres");
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public String getTankUidByMsisdn(String rawMsisdn) {
        db = getReadableDatabase();
        String lastRecord = "0";
        String parsedMsisdn = "0" + StringUtils.substring(rawMsisdn, rawMsisdn.length() - 9, rawMsisdn.length());
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(("SELECT tnk_uid" + " FROM tbl_tank_settings") + " WHERE tank_msisdn = '" + StringUtils.trim(parsedMsisdn) + "'", null);
            cursor.moveToFirst();
            lastRecord = cursor.getString(0);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lastRecord;
    }

    public Double getTankPercentageByUid(String uId) {
        db = getReadableDatabase();
        Double lastRecord = Double.valueOf(0.0d);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(("SELECT top_height" + " FROM tbl_tank_settings") + " WHERE tnk_uid = " + StringUtils.trim(uId), null);
            cursor.moveToFirst();
            lastRecord = Double.valueOf((getLastTanklevel(uId).doubleValue() / Double.parseDouble(cursor.getString(0))) * 100.0d);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lastRecord;
    }

    public Double getTankVolumeByUid(String uId) {
        db = getReadableDatabase();
        Double meters = Double.valueOf(0.0d);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(("SELECT base_area" + " FROM tbl_tank_settings") + " WHERE tnk_uid = " + StringUtils.trim(uId), null);
            cursor.moveToFirst();
            meters = Double.valueOf((getLastTanklevel(uId).doubleValue() * Double.parseDouble(cursor.getString(0))) * 1000.0d);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return (meters==null||meters.equals(""))?0.00d:meters;
    }

    public ArrayList<String> getAllTankMsisdns() {
        db = getReadableDatabase();
        ArrayList<String> dataMsisdns = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_TANK_SETTINGS, new String[]{TANK_MSISDN}, null, null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    dataMsisdns.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dataMsisdns;
    }

    public boolean updateTankReadings() {
        try {
            if (getAllTankMsisdns().size() != 0) {
                for (int i = 0; i < getAllTankMsisdns().size(); i++) {
                    readInbox(getAllTankMsisdns().get(i));
                }
            }
            return true;
        } catch (Exception e) {
            Log.e("Update Error", e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public LineData drawGraph(String uId, int dateInterval) {
        db = getReadableDatabase();
        List<Entry> entries = new ArrayList();
        LineData data = new LineData();
        Cursor cursor = null;
        int j = 0;
        try {
            String[] dayRange = getDayRange(dateInterval).split("-");
            cursor = db.query(TABLE_TANK, new String[]{TANK_LEVEL}, "tnk_uid = " + uId + " AND " + TANK_TIME + " BETWEEN " + dayRange[0] + " AND " + dayRange[1], null, null, null, "tank_time asc");
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    entries.add(new Entry((float) j, cursor.getFloat(0)));
                    j++;
                } while (cursor.moveToNext());
            }
            LineDataSet dataSet = new LineDataSet(entries, TimeChart.TYPE);
            dataSet.setFillAlpha(50);
            dataSet.setColor(-16777216);
            dataSet.setCircleColor(-16777216);
            dataSet.setCircleRadius(1.0f);
            dataSet.setDrawCircleHole(false);
            dataSet.setValueTextSize(5.0f);
            dataSet.setDrawFilled(true);
            if (dataSet.getValues().size() != 0) {
                data.addDataSet(dataSet);
            }
            cursor.close();
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return data;
    }


    public LineData drawWeekGraph(String uId, int dateInterval) {
        db = getReadableDatabase();
        List<Entry> entries = new ArrayList();
        LineData data = new LineData();
        Cursor cursor = null;
        int j = 0;
        try {
            String[] dayRange = getDayRange(dateInterval).split("-");
            cursor = db.query(TABLE_TANK, new String[]{TANK_LEVEL}, "tnk_uid = " + uId + " AND " + TANK_TIME + " BETWEEN " + dayRange[0] + " AND " + dayRange[1], null, null, null, "tank_time asc");
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    entries.add(new Entry((float) j, cursor.getFloat(0)));
                    j++;
                } while (cursor.moveToNext());
            }
            LineDataSet dataSet = new LineDataSet(entries, TimeChart.TYPE);
            dataSet.setFillAlpha(50);
            dataSet.setColor(-16777216);
            dataSet.setCircleColor(-16777216);
            dataSet.setCircleRadius(1.0f);
            dataSet.setDrawCircleHole(false);
            dataSet.setValueTextSize(5.0f);
            dataSet.setDrawFilled(true);
            if (dataSet.getValues().size() != 0) {
                data.addDataSet(dataSet);
            }
            cursor.close();
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return data;
    }

    public HashMap<Integer, String> LineDataXLabels(String uId, int dateInterval) {
        db = getReadableDatabase();
        Cursor cursor = null;
        HashMap<Integer, String> hashMap = new HashMap();
        int j = 0;
        try {
            String[] dayRange = getDayRange(dateInterval).split("-");
            cursor = db.query(TABLE_TANK, new String[]{TANK_TIME}, "tnk_uid = " + uId + " AND " + TANK_TIME + " BETWEEN " + dayRange[0] + " AND " + dayRange[1], null, null, null, "tank_time asc");
            cursor.moveToFirst();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            if (!cursor.isAfterLast()) {
                do {
                    hashMap.put(Integer.valueOf(j), sdf.format(Float.valueOf(cursor.getFloat(0))));
                    j++;
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return hashMap;
    }

    public GraphicalView drawGraph1(String uId, int dateInterval) {
        SimpleSeriesRenderer waterSeriesRenderer;
        XYMultipleSeriesDataset dataset;
        XYMultipleSeriesRenderer multiRenderer;
        Throwable th;
        SimpleSeriesRenderer simpleSeriesRenderer;
        db = getReadableDatabase();
        XYSeries xYSeries = new XYSeries("Water levels");
        Cursor cursor = null;
        multiRenderer = new XYMultipleSeriesRenderer();
        dataset = new XYMultipleSeriesDataset();
        waterSeriesRenderer = new SimpleSeriesRenderer();
        try {
            String[] dayRange = getDayRange(dateInterval).split("-");
            int j = 0;
            cursor = db.query(TABLE_TANK, new String[]{TANK_LEVEL, TANK_TIME}, "tnk_uid = " + uId + " AND " + TANK_TIME + " BETWEEN " + dayRange[0] + " AND " + dayRange[1], null, null, null, "tank_time asc");
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    Calendar.getInstance().setTime(new Date((long) cursor.getInt(1)));
                    xYSeries.add((double) j, (double) cursor.getFloat(0));
                    j++;
                } while (cursor.moveToNext());
            }
            multiRenderer.setXLabels(0);
            dataset.addSeries(xYSeries);
            waterSeriesRenderer.setColor(-1);
            //waterSeriesRenderer.setPointStyle(PointStyle.POINT);
            //waterSeriesRenderer.setFillPoints(false);
            //waterSeriesRenderer.setLineWidth(2.0f);
            waterSeriesRenderer.setChartValuesFormat(new DecimalFormat("#.00"));
            //waterSeriesRenderer.setDisplayChartValues(true);
            multiRenderer.setApplyBackgroundColor(true);
            multiRenderer.setShowGrid(true);
            multiRenderer.setBackgroundColor(R.color.colorWhite);
            int[] iArr = new int[4];
            multiRenderer.setMargins(new int[]{0, 0, 0, 2});
            multiRenderer.setYLabelsAlign(Align.RIGHT);
            multiRenderer.setMarginsColor(R.color.colorWhite);
            multiRenderer.setYAxisMax(5.0d);
            multiRenderer.setYAxisMin(0.0d);
            multiRenderer.setLabelsTextSize(20.0f);
            multiRenderer.setChartTitle("Water Levels Chart");
            multiRenderer.addSeriesRenderer(waterSeriesRenderer);
            this.mChartView = ChartFactory.getLineChartView(this.context, dataset, multiRenderer);
            if (cursor != null) {
                cursor.close();
            }

        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return this.mChartView;
    }

    public static String getDayRange(int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        int dayOfMonth = calendar.get(DAY_OF_MONTH);
        if (interval == 0) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth + interval);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        return String.valueOf(calendar.getTimeInMillis()) + "-" + String.valueOf(calendar2.getTimeInMillis());
    }

    public String getMonthRange(int mnth, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, mnth);
        calendar.set(DAY_OF_MONTH, calendar.getActualMinimum(Calendar.MONTH));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar calendar2 = (Calendar) calendar.clone();
        calendar2.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        calendar2.set(Calendar.YEAR, year);
        calendar2.set(Calendar.MONTH, mnth);
        calendar2.set(DAY_OF_MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar2.set(Calendar.HOUR, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        return String.valueOf(calendar.getTimeInMillis()) + "-" + String.valueOf(calendar2.getTimeInMillis());
    }

    public List<MonthDetails> getMonthDetails(String uId, int mnth, int year) {
        db = getReadableDatabase();
        List<MonthDetails> listDetails = new ArrayList();
        String[] mnthRange = getMonthRange(mnth, year).split("-");
        Cursor cursor = db.query(TABLE_TANK, new String[]{TANK_TIME, TANK_LEVEL}, "tnk_uid = " + uId + " AND " + TANK_TIME + " BETWEEN " + mnthRange[0] + " AND " + mnthRange[1], null, null, null, "tank_time desc");
        cursor.moveToFirst();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String prevDate = "";
        if (!cursor.isAfterLast()) {
            do {
                Date date = new Date(Long.parseLong(cursor.getString(0)));
                String dayReading = StringUtils.rightPad(String.valueOf(cursor.getFloat(1)), 4, "0") + " M";
                if (simpleDateFormat.format(date).equals(prevDate)) {
                    listDetails.add(new MonthDetails(StringUtils.SPACE, simpleDateFormat.format(date), dayReading));
                } else {
                    listDetails.add(new MonthDetails(simpleDateFormat.format(date) + "  ", simpleDateFormat.format(date), dayReading));
                }
                prevDate = simpleDateFormat.format(date);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return listDetails;
    }

    public List<Month> getMonthsList(String uId, int year) {
        List<Month> mnthList = new ArrayList();
        for (int i = 0; i < this.monthNames.length; i++) {
            mnthList.add(new Month(this.monthNames[i], getMonthDetails(uId, i, year)));
        }
        return mnthList;
    }

    public List<String> getYearsList() {
        List<String> yrList = new ArrayList();
        db = getReadableDatabase();
        String SQL = " SELECT MIN(tank_time) AS MINTIM , MAX(tank_time) AS MAXTIM " + " FROM  tbl_tank_level";
        Cursor cursor = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        try {
            cursor = db.rawQuery(SQL, null);
            cursor.moveToFirst();
            Date initDate = new Date(Long.parseLong(cursor.getString(0)));
            Date finalDate = new Date(Long.parseLong(cursor.getString(1)));
            int initYear = Integer.parseInt(sdf1.format(initDate));
            int finalYear = Integer.parseInt(sdf1.format(finalDate));
            if (initYear >= finalYear) {
                yrList.add(String.valueOf(initYear));
            } else {
                for (int i = initYear; i <= finalYear; i++) {
                    yrList.add(String.valueOf(i));
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return yrList;
    }

    public void exportDB(String uId, int mnth, int year) {
        db = getReadableDatabase();
        Cursor curCSV = null;
        CSVWriter csvWrite = null;
        String[] mnthRange = getMonthRange(mnth, year).split("-");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss a");
        simpleDateFormat = new SimpleDateFormat("MMMM_yyyy");
        String[] colNames = new String[]{"REGISTERED TIME", "TANK NAME", "TANK LEVELS"};
        String tankName = getTankName(uId).toLowerCase().replaceAll(StringUtils.SPACE, "");
        File file = new File(Environment.getExternalStorageDirectory() + "/MobiWater/", "");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = file;
        file = new File(file2, simpleDateFormat.format(new Date(Long.parseLong(mnthRange[0]))) + "_" + tankName + ".csv");
        try {
            file.createNewFile();
            csvWrite = new CSVWriter(new FileWriter(file));
            curCSV = db.query(TABLE_TANK, new String[]{TANK_UNIQUE_ID, TANK_TIME, TANK_LEVEL}, "tnk_uid = " + uId + " AND " + TANK_TIME + " BETWEEN " + mnthRange[0] + " AND " + mnthRange[1], null, null, null, "tank_time desc");
            csvWrite.writeNext(colNames);
            while (curCSV.moveToNext()) {
                String[] arrStr = new String[3];
                arrStr[0] = simpleDateFormat.format(new Date(Long.parseLong(curCSV.getString(1))));
                arrStr[1] = getTankName(curCSV.getString(0));
                arrStr[2] = curCSV.getString(2);
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (curCSV != null) {
                    curCSV.close();
                }
        }
    }

    public ArrayList<Setting> getSettingsData() {
        db = getReadableDatabase();
        ArrayList<Setting> dataSettingItems = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_TANK_SETTINGS, new String[]{TANK_UNIQUE_ID, TANK_NAME, TOP_HEIGHT, UPPER_THRESHOLD, LOWER_THRESHOLD, TANK_MSISDN}, null, null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    Setting st = new Setting();
                    st.setSettingId(cursor.getString(0));
                    st.setTankName(cursor.getString(1));
                    st.setTopHeight(cursor.getString(2));
                    st.setUpperLimit(cursor.getString(3));
                    st.setLowerLimit(cursor.getString(4));
                    st.setPhoneNumber(cursor.getString(5));
                    dataSettingItems.add(st);
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dataSettingItems;
    }

    public Setting getSettings(String sId) {
        db = getReadableDatabase();
        Cursor cursor = null;
        Setting setting = new Setting();
        try {
            cursor = db.query(TABLE_TANK_SETTINGS, new String[]{TANK_UNIQUE_ID, TANK_NAME, TOP_HEIGHT, UPPER_THRESHOLD, LOWER_THRESHOLD, TANK_MSISDN, BASE_AREA}, "tnk_uid = " + sId, null, null, null, null);
            cursor.moveToFirst();
            setting.setSettingId(cursor.getString(0));
            setting.setTankName(cursor.getString(1));
            setting.setTopHeight(cursor.getString(2));
            setting.setUpperLimit(cursor.getString(3));
            setting.setLowerLimit(cursor.getString(4));
            setting.setPhoneNumber(cursor.getString(5));
            setting.setBaseArea(cursor.getString(6));
            if (cursor != null) {
                cursor.close();
            }
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return setting;
    }

    public int updateSetting(Setting setting) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TANK_NAME, setting.getTankName());
        values.put(TOP_HEIGHT, setting.getTopHeight());
        values.put(LOWER_THRESHOLD, setting.getLowerLimit());
        values.put(UPPER_THRESHOLD, setting.getUpperLimit());
        values.put(TANK_MSISDN, setting.getPhoneNumber());
        values.put(BASE_AREA, setting.getBaseArea());
        try {
            db.update(TABLE_TANK_SETTINGS, values, "tnk_uid = ?", new String[]{String.valueOf(setting.getSettingId())});
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteSetting(String uId) {
        int update = 0;
        db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TANK, "tnk_uid=" + uId, null);
            db.delete(TABLE_TANK_SETTINGS, "tnk_uid=" + uId, null);
            db.setTransactionSuccessful();
            update = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return update;
    }

    public int addReminder(Reminder reminder) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, reminder.getTitle());
        values.put(KEY_DATE, reminder.getDate());
        values.put(KEY_TIME, reminder.getTime());
        values.put(KEY_REPEAT, reminder.getRepeat());
        values.put(KEY_REPEAT_NO, reminder.getRepeatNo());
        values.put(KEY_REPEAT_TYPE, reminder.getRepeatType());
        values.put(KEY_ACTIVE, reminder.getActive());
        long ID = db.insert(TABLE_REMINDERS, null, values);
        db.close();
        return (int) ID;
    }

    public Reminder getReminder(int id) {
        Cursor cursor = getReadableDatabase().query(TABLE_REMINDERS, new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_TIME, KEY_REPEAT, KEY_REPEAT_NO, KEY_REPEAT_TYPE, KEY_ACTIVE}, "id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return new Reminder(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> reminderList = new ArrayList();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT * FROM tbl_reminders", null);
        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setID(Integer.parseInt(cursor.getString(0)));
                reminder.setTitle(cursor.getString(1));
                reminder.setDate(cursor.getString(2));
                reminder.setTime(cursor.getString(3));
                reminder.setRepeat(cursor.getString(4));
                reminder.setRepeatNo(cursor.getString(5));
                reminder.setRepeatType(cursor.getString(6));
                reminder.setActive(cursor.getString(7));
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }

        return reminderList;
    }

    public int getRemindersCount() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM tbl_reminders", null);
        cursor.close();
        return cursor.getCount();
    }

    public int updateReminder(Reminder reminder) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, reminder.getTitle());
        values.put(KEY_DATE, reminder.getDate());
        values.put(KEY_TIME, reminder.getTime());
        values.put(KEY_REPEAT, reminder.getRepeat());
        values.put(KEY_REPEAT_NO, reminder.getRepeatNo());
        values.put(KEY_REPEAT_TYPE, reminder.getRepeatType());
        values.put(KEY_ACTIVE, reminder.getActive());
        return db.update(TABLE_REMINDERS, values, "id=?", new String[]{String.valueOf(reminder.getID())});
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_REMINDERS, "id=?", new String[]{String.valueOf(reminder.getID())});
        db.close();
    }
}
