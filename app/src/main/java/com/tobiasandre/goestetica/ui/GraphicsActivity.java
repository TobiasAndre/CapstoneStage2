package com.tobiasandre.goestetica.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.tobiasandre.goestetica.R;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.database.model.ResultItem;

import java.util.ArrayList;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class GraphicsActivity extends AppCompatActivity {

    private PieChart mChart;
    private ArrayList<PieEntry> entries;
    private ArrayList<ResultItem> resultArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);

        mChart = (PieChart)findViewById(R.id.chart);

        Bundle bundle = getIntent().getExtras();

        loadGraphics(bundle);

    }

    private void loadGraphics(Bundle bundle){

        String mDescription = "";
        Description ds = new Description();
        ds.setText(mDescription);

        mChart.setDescription(ds);
        mChart.setUsePercentValues(true);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setDrawCenterText(true);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(7);
        //mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(e==null){
                    return;
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        loadData(bundle);
    }

    private void loadData(Bundle bundle){

        String dataInicial = "";
        String dataFinal = "";
        if(bundle!=null){
            dataInicial = bundle.getString("dtInit");
            dataFinal = bundle.getString("dtFin");
        }
        String selection = GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_DATE + " >= '"+dataInicial+"' and "+
                GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_DATE+" <= '"+dataFinal+"'";

        entries = new ArrayList<PieEntry>();
        resultArray = new ArrayList<>();

        Cursor cursor = getContentResolver().query(GoEsteticaContract.ScheduleEntry.CONTENT_URI, null, selection, null, null);
        try {
            while (cursor.moveToNext()) {

                if(resultArray.size()==0){
                    resultArray.add(new ResultItem(cursor.getInt(cursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CUSTOMER_ID)),
                            null,cursor.getDouble(cursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_PRICE))));
                }else{
                    for(int i=0;i<resultArray.size();i++){
                        if(resultArray.get(i).getIdResult()==cursor.getInt(cursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CUSTOMER_ID))){
                            resultArray.get(i).setDsResult(getCustomer(cursor.getInt(cursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CUSTOMER_ID))));
                            resultArray.get(i).setVlResult(resultArray.get(i).getVlResult()+cursor.getDouble(cursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_PRICE)));
                        }else{
                            resultArray.add(new ResultItem(cursor.getInt(cursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CUSTOMER_ID)),
                                    getCustomer(cursor.getInt(cursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_CUSTOMER_ID))),
                                    cursor.getDouble(cursor.getColumnIndexOrThrow(GoEsteticaContract.ScheduleEntry.COLUMN_SCHEDULE_PRICE))));
                        }
                    }
                }
            }
        } finally {
            cursor.close();
        }

        for(int i=0;i<resultArray.size();i++) {
            entries.add(new PieEntry(Float.valueOf(resultArray.get(i).getVlResult().toString()),resultArray.get(i).getDsResult()));
        }


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(5);
        dataSet.setSelectionShift(5);

        //region cores
        ArrayList<Integer> mColors = new ArrayList<Integer>();

        mColors.add(rgb("#673AB7")); //Deep purple
        mColors.add(rgb("#FFEB3B")); //yeallow
        mColors.add(rgb("#4CAF50")); //green
        mColors.add(rgb("#607D8B")); //deep orange
        mColors.add(rgb("#2196F3")); //blue
        mColors.add(rgb("#F44336")); //red
        mColors.add(rgb("#009688")); //teal

        dataSet.setColors(mColors);
        //endregion

        PieData data = new PieData();
        data.setDataSet(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTextSize(11f);
        mChart.setDrawEntryLabels(true);

        mChart.setData(data);
        mChart.invalidate();

    }

    private String getCustomer(Integer mIdCustomer){
        String nmCustomer = "";
        if(mIdCustomer>0){
            Uri contentUri = GoEsteticaContract.CustomerEntry.CONTENT_URI;
            String selection = GoEsteticaContract.CustomerEntry._ID + " = ?";
            String[] selectionArguments = new String[]{String.valueOf(mIdCustomer)};

            Cursor c = getContentResolver().query(contentUri, null, selection, selectionArguments, null);
            if (c != null) {
                while (c.moveToNext()) {
                    nmCustomer = c.getString(c.getColumnIndexOrThrow(GoEsteticaContract.CustomerEntry.COLUMN_CUSTOMER_NAME));
                }
                c.close();
            }
        }
        return nmCustomer;
    }



}
