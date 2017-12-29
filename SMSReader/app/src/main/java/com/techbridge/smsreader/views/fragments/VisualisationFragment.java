package com.techbridge.smsreader.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.techbridge.smsreader.db.DBHelper;
import com.techbridge.smsreader.R;

public class VisualisationFragment extends Fragment {
    private TextView batterytext;
    private ArrayAdapter<String> dataAdapter;
    private DBHelper dbhelper;
    private TextView meterReading;
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
    private Spinner spinner;
    String title;

    class Rghghgh implements OnItemSelectedListener {
        Rghghgh() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
            String tankUniqueId = dbhelper.getTankUniqueIndex(position);
            Double percentage = dbhelper.getTankPercentageByUid(tankUniqueId);
            meterReading.setText(dbhelper.getLastTextTanklevel(tankUniqueId)[2]);
            UpdateTank(percentage);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public static VisualisationFragment newInstance(String title) {
        VisualisationFragment fragment = new VisualisationFragment();
        new Bundle(1).putString("EXTRA_TITLE", title);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("EXTRA_TITLE");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualization, container, false);
        initWidgets(view);
        loadSpinnerData();
        return view;
    }

    private void initWidgets(View v) {
        meterReading = (TextView) v.findViewById(R.id.meterreading);
        percent100 = (ImageView) v.findViewById(R.id.percent100);
        percent90 = (ImageView) v.findViewById(R.id.percent90);
        percent80 = (ImageView) v.findViewById(R.id.percent80);
        percent70 = (ImageView) v.findViewById(R.id.percent70);
        percent60 = (ImageView) v.findViewById(R.id.percent60);
        percent50 = (ImageView) v.findViewById(R.id.percent50);
        percent40 = (ImageView) v.findViewById(R.id.percent40);
        percent30 = (ImageView) v.findViewById(R.id.percent30);
        percent20 = (ImageView) v.findViewById(R.id.percent20);
        percent10 = (ImageView) v.findViewById(R.id.percent10);
        batterytext = (TextView) v.findViewById(R.id.batterytext);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String tankUniqueId = dbhelper.getTankUniqueIndex(position);
                Double percentage = dbhelper.getTankPercentageByUid(tankUniqueId);
                meterReading.setText(dbhelper.getLastTextTanklevel(tankUniqueId)[2]);
                UpdateTank(percentage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void loadSpinnerData() {
        dbhelper = new DBHelper(getActivity());
        dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, dbhelper.getTankNames());
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        dataAdapter.notifyDataSetChanged();
        spinner.setAdapter(dataAdapter);
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

}
