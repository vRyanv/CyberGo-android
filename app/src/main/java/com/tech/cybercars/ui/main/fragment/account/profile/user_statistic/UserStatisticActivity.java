package com.tech.cybercars.ui.main.fragment.account.profile.user_statistic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivityUserStatisticBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.ui.main.fragment.account.profile.ProfileViewModel;

import java.util.ArrayList;

public class UserStatisticActivity extends BaseActivity<ActivityUserStatisticBinding, UserStatisticViewModel> {
    private final String[] labels = new String[] { "Moto", "Car", "Truck" };
    private final int[] colors = new int[]{Color.rgb(241, 95, 43), Color.rgb(143, 117, 221), Color.rgb(8, 117, 190)};
    @NonNull
    @Override
    protected UserStatisticViewModel InitViewModel() {
        return new ViewModelProvider(this).get(UserStatisticViewModel.class);
    }

    @Override
    protected ActivityUserStatisticBinding InitBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_statistic);
        binding.setViewModel(view_model);
        return binding;
    }

    @Override
    protected void InitFirst() {

    }

    @Override
    protected void InitView() {
        InitPieChart();
        InitBarChart();


    }

    @Override
    protected void InitObserve() {

    }

    @Override
    protected void InitCommon() {

    }

    @Override
    protected void OnBackPress() {
        finish();
    }
    private void InitPieChart(){
        PieDataSet data_set = new PieDataSet(DataPieChartValue(), "");
        data_set.setColors(colors);

        data_set.setFormSize(22f);

        // size of value text
        data_set.setValueTextSize(12f);
        data_set.setValueTextColor(Color.WHITE);

        Legend legend = binding.revenueByVehicleChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        PieData pie_data = new PieData(data_set);
        binding.revenueByVehicleChart.setData(pie_data);
        binding.revenueByVehicleChart.getDescription().setEnabled(false);
        binding.revenueByVehicleChart.setCenterText("Revenue of vehicle");
        binding.revenueByVehicleChart.invalidate();
    }
    private void InitBarChart() {
        String data_label = getString(R.string.trip_quantity);
        BarDataSet data_set = new BarDataSet(DataValue(), data_label);
        data_set.setValueFormatter(new CustomIndexAxisValueFormatter());

        // move column name to bottom
        XAxis xAxis = binding.tripByVehicleChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //colum name

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setAvoidFirstLastClipping(true);

        //size of column name
        xAxis.setTextSize(12f);

        // size of value text
        data_set.setValueTextSize(12f);

        //colum color
        data_set.setColors(colors);

        //remove description data
        binding.tripByVehicleChart.getDescription().setEnabled(false);

        //remove right Y axis
        YAxis right_axis = binding.tripByVehicleChart.getAxisRight();
        right_axis.setDrawLabels(false);
        right_axis.setDrawAxisLine(false);
        right_axis.setDrawGridLines(false);

        //disable zoom
        binding.tripByVehicleChart.setPinchZoom(false);
        binding.tripByVehicleChart.setDoubleTapToZoomEnabled(false); //

        // move legend to top
        Legend legend = binding.tripByVehicleChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);


        BarData barData = new BarData();
        barData.addDataSet(data_set);
        binding.tripByVehicleChart.setData(barData);
        binding.tripByVehicleChart.invalidate();
    }

    private ArrayList<BarEntry> DataValue(){
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(0,3));
        data.add(new BarEntry(1,6));
        data.add(new BarEntry(2,10));
        return data;
    }

    private ArrayList<PieEntry> DataPieChartValue(){
        ArrayList<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(20,"Moto"));
        data.add(new PieEntry(20,"Car"));
        data.add(new PieEntry(60,"truck"));
        return data;
    }

    class CustomIndexAxisValueFormatter extends IndexAxisValueFormatter{
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }

}