package com.tech.cybercars.ui.main.fragment.account.profile.user_statistic;

import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

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
import com.tech.cybercars.R;
import com.tech.cybercars.databinding.ActivityUserStatisticBinding;
import com.tech.cybercars.ui.base.BaseActivity;
import com.tech.cybercars.utils.DateTimePicker;
import com.tech.cybercars.utils.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class UserStatisticActivity extends BaseActivity<ActivityUserStatisticBinding, UserStatisticViewModel> {
    private PieDataSet pie_data_set;
    private BarDataSet bar_data_set;
    private DateTimePicker date_time_picker;
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

        binding.headerPrimary.btnOutScreen.setOnClickListener(view -> {
            finish();
        });

        InitDatePicker();
    }

    private void InitDatePicker() {
        date_time_picker = new DateTimePicker(
                getSupportFragmentManager(),
                DateTimePicker.M_D_Y
        );

        binding.inputStartDate.getEditText().setOnClickListener(view -> {
            date_time_picker.SetOnDateTimePicked((calendar, date_time_format) -> {
                view_model.start_date.setValue(DateUtil.YMDFormat(calendar));
                view_model.HandleGetStatistic();
            });
            date_time_picker.Run();
        });

        binding.inputEndDate.getEditText().setOnClickListener(view -> {
            date_time_picker.SetOnDateTimePicked((calendar, date_time_format) -> {
                view_model.end_date.setValue(DateUtil.YMDFormat(calendar));
                view_model.HandleGetStatistic();
            });
            date_time_picker.Run();
        });
    }

    @Override
    protected void InitObserve() {
        view_model.revenue_of_vehicle.observe(this, this::UpdatePieChart);
        view_model.trip_quantity_of_vehicle.observe(this, this::UpdateBarChart);

        view_model.is_loading.observe(this, is_loading -> {
            if(is_loading){
                binding.skeletonLoadingBarChart.startShimmerAnimation();
                binding.skeletonLoadingPieChart.startShimmerAnimation();
            } else {
                binding.skeletonLoadingBarChart.stopShimmerAnimation();
                binding.skeletonLoadingPieChart.stopShimmerAnimation();
            }
        });

        view_model.error_call_server.observe(this, this::ShowErrorDialog);
    }

    @Override
    protected void InitCommon() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        String start_date = DateUtil.YMDFormat(calendar);
        view_model.start_date.setValue(start_date);

        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DATE, month == 2 ? 28 : 30);
        String end_date =  DateUtil.YMDFormat(calendar);
        view_model.end_date.setValue(end_date);

        view_model.HandleGetStatistic();
    }

    @Override
    protected void OnBackPress() {
        finish();
    }

    private void InitPieChart(){
        pie_data_set = new PieDataSet(new ArrayList<>(), "");
        pie_data_set.setColors(colors);
        pie_data_set.setFormSize(22f);
        // size of value text
        pie_data_set.setValueTextSize(12f);
        pie_data_set.setValueTextColor(Color.WHITE);

        Legend legend = binding.revenueByVehicleChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        PieData pie_data = new PieData(pie_data_set);

        binding.revenueByVehicleChart.setData(pie_data);
        binding.revenueByVehicleChart.getDescription().setEnabled(false);
        binding.revenueByVehicleChart.setCenterText("Revenue of vehicle");
        binding.revenueByVehicleChart.invalidate();
    }

    private void InitBarChart() {
        String data_label = getString(R.string.trip_quantity);
        bar_data_set = new BarDataSet(new ArrayList<>(), data_label);
        bar_data_set.setValueFormatter(new CustomIndexAxisValueFormatter());

        // move column name to bottom
        XAxis xAxis = binding.tripByVehicleChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //colum name

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setAvoidFirstLastClipping(true);

        //size of column name
        xAxis.setTextSize(12f);

        // size of value text
        bar_data_set.setValueTextSize(12f);

        //colum color
        bar_data_set.setColors(colors);

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

        barData.addDataSet(bar_data_set);
        binding.tripByVehicleChart.setData(barData);
        binding.tripByVehicleChart.invalidate();
    }
    private void UpdatePieChart(ArrayList<PieEntry> pie_entries){
        pie_data_set.setValues(pie_entries);
        binding.revenueByVehicleChart.invalidate();
    }
    private void UpdateBarChart(ArrayList<BarEntry> bar_entries){
        String data_label = getString(R.string.trip_quantity);
        bar_data_set = new BarDataSet(bar_entries, data_label);
        bar_data_set.setValueFormatter(new CustomIndexAxisValueFormatter());
        // size of value text
        bar_data_set.setValueTextSize(12f);

        //colum color
        bar_data_set.setColors(colors);
        BarData barData = new BarData();
        barData.addDataSet(bar_data_set);
        binding.tripByVehicleChart.setData(barData);
        binding.tripByVehicleChart.invalidate();
    }
    static class CustomIndexAxisValueFormatter extends IndexAxisValueFormatter{
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }

}