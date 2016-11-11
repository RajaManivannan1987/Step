package com.imaginet.ventures.step.fragment;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.singleton.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepScoreFragment extends Fragment {
    private View view;
    private BarChart barChart;
    private TextView rankTextView, rankTextTextView, overAllTextView, strengthTextView, weaknessTextView;

    public StepScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_step_score, container, false);

        barChart = (BarChart) view.findViewById(R.id.stepScoreFragmentBarChart);
        rankTextView = (TextView) view.findViewById(R.id.stepScoreFragmentRankTextView);
        rankTextTextView = (TextView) view.findViewById(R.id.stepScoreFragmentRankTextTextView);
        overAllTextView = (TextView) view.findViewById(R.id.stepScoreFragmentOverallTextListTextView);
        strengthTextView = (TextView) view.findViewById(R.id.stepScoreFragmentStrengthTextListTextView);
        weaknessTextView = (TextView) view.findViewById(R.id.stepScoreFragmentWeaknessTextListTextView);


        return view;
    }

    @Override
    public void onResume() {
        update();
        super.onResume();
    }

    private void update() {
        if (UserData.getInstance().getRank() != null && !UserData.getInstance().getRank().equalsIgnoreCase("null"))
            rankTextView.setText(UserData.getInstance().getRank());

        rankTextTextView.setText(UserData.getInstance().getRankText());
        overAllTextView.setText(UserData.getInstance().getOverAllText());
        strengthTextView.setText(UserData.getInstance().getStrengthText());
        weaknessTextView.setText(UserData.getInstance().getWeaknessText());

        //Bar Chart Starts
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setDescription("");
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.DEFAULT);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        barChart.setData(data);
        barChart.animateXY(2000, 2000);
        barChart.invalidate();
        barChart.setEnabled(false);
        //Bar Chart Ends
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(UserData.getInstance().getOverAllLevel(), 0); // Yours
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(UserData.getInstance().getAverage(), 1);// Avg
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(12, 2);// Total
        valueSet1.add(v1e3);
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "");
        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.parseColor("#005CA2"));
        colorList.add(Color.parseColor("#18B2D4"));
        colorList.add(Color.parseColor("#17B2D3"));
        barDataSet1.setColors(colorList);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Yours");
        xAxis.add("Avg");
        xAxis.add("Total");
        return xAxis;
    }
}
