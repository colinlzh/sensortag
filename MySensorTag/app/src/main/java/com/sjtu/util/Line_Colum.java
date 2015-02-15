package com.sjtu.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ies.mysensortag.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.renderer.ComboLineColumnChartRenderer;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;


public class Line_Colum extends android.support.v4.app.Fragment {
    private ComboLineColumnChartView chart;
    private ComboLineColumnChartData data;

    private int numberOfLines = 1;
    private int maxNumberOfLines = 1;
    private int numberOfPoints = 96;
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasPoints = false;
    private boolean hasLines = true;
    private boolean isCubic = false;
    private boolean hasLabels = true;

    public Line_Colum() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_line__colum, container, false);

        chart = (ComboLineColumnChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        generateValues_mine();
        generateData();

        return rootView;
    }

    private void generateValues_mine(){
        //生成线图的点的值
        for (int i = 0; i <96 ; i++) {
            if(i<8) randomNumbersTab[0][i]= 1.0f;
            else if(i<16) randomNumbersTab[0][i]= 3.0f;
            else if(i<24) randomNumbersTab[0][i]= 2.0f;
            else if(i<32) randomNumbersTab[0][i]= 2.0f;
            else if(i<40) randomNumbersTab[0][i]= 3.0f;
            else if(i<48) randomNumbersTab[0][i]= 4.0f;
            else if(i<56) randomNumbersTab[0][i]= 5.0f;
            else if(i<64) randomNumbersTab[0][i]= 5.0f;
            else if(i<72) randomNumbersTab[0][i]= 6.0f;
            else if(i<80) randomNumbersTab[0][i]= 6.0f;
            else if(i<86) randomNumbersTab[0][i]= 7.0f;
            else randomNumbersTab[0][i]= 8.0f;

        }
    }

    private void reset() {
        numberOfLines = 1;

        hasAxes = true;
        hasAxesNames = true;
        hasLines = true;
        hasPoints = false;
        hasLabels = true;
        isCubic = false;

    }
    private List<AxisValue> generateAxisValue(){
        List<AxisValue> values = new ArrayList<AxisValue>();
        AxisValue axisValue;
        for(int i=0;i<96;i++){
            switch(i) {
                case 0:
                    axisValue = new AxisValue(i,"0".toCharArray());
                    values.add(axisValue);
                    break;
                case 8:
                    axisValue = new AxisValue(i,"1".toCharArray());
                    values.add(axisValue);
                    break;
                case 16:
                    axisValue = new AxisValue(i,"2".toCharArray());
                    values.add(axisValue);
                    break;
                case 24:
                    axisValue = new AxisValue(i,"3".toCharArray());
                    values.add(axisValue);
                    break;
                case 32:
                    axisValue = new AxisValue(i,"4".toCharArray());
                    values.add(axisValue);
                    break;
                case 40:
                    axisValue = new AxisValue(i,"5".toCharArray());
                    values.add(axisValue);
                    break;
                case 48:
                    axisValue = new AxisValue(i,"6".toCharArray());
                    values.add(axisValue);
                    break;
                case 56:
                    axisValue = new AxisValue(i,"7".toCharArray());
                    values.add(axisValue);
                    break;
                case 64:
                    axisValue = new AxisValue(i,"8".toCharArray());
                    values.add(axisValue);
                    break;
                case 72:
                    axisValue = new AxisValue(i,"9".toCharArray());
                    values.add(axisValue);
                    break;
                case 80:
                    axisValue = new AxisValue(i,"10".toCharArray());
                    values.add(axisValue);
                    break;
                case 88:
                    axisValue = new AxisValue(i,"11".toCharArray());
                    values.add(axisValue);
                    break;
                default:
                    axisValue = new AxisValue(i,"".toCharArray());
                    values.add(axisValue);
                    break;
            }
        }
        return values;
    }
    private void generateData() {
        // Chart looks the best when line data and column data have similar maximum viewports.
        data = new ComboLineColumnChartData(generateColumnData(), generateLineData());

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("时间");
                axisX.setValues(generateAxisValue());
                axisY.setName("温度（摄氏度）");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {

            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setComboLineColumnChartData(data);
    }

    private LineChartData generateLineData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
//            line.setColor(ChartUtils.COLORS[i]);
            line.setColor(ChartUtils.COLOR_GREEN);
            line.setCubic(isCubic);
            line.setHasLabels(hasLabels);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }

        LineChartData lineChartData = new LineChartData(lines);
        return lineChartData;

    }

    private ColumnChartData generateColumnData() {
        int numSubcolumns = 1;
        int numColumns = 96;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(18.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(18.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(17.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(16.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(5.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(6.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(7.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(6.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(9.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(18.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(18.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(17.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(16.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(5.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(6.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(7.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(6.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(9.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(18.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(18.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(17.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(16.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(5.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(6.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(7.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(6.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(9.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(18.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(18.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(17.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(16.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(19.0f, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(5.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(6.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(7.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(6.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(9.0f, ChartUtils.COLOR_ORANGE));
        columns.add(new Column(values));
        values = new ArrayList<SubcolumnValue>();
        values.add(new SubcolumnValue(0, ChartUtils.COLOR_BLUE));
        columns.add(new Column(values));
        ColumnChartData columnChartData = new ColumnChartData(columns);
        return columnChartData;
    }


    private class ValueTouchListener implements ComboLineColumnChartOnValueSelectListener {

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            //Toast.makeText(getActivity(), "Selected column: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value) {
            //Toast.makeText(getActivity(), "Selected line point: " + value, Toast.LENGTH_SHORT).show();
        }

    }
}