package com.haocang.commonlib.curve;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.haocang.commonlib.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 创建时间：2019/1/22
 * 编 写 人：ShenC
 * 功能描述：
 */

public class LineChartMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue0;
    private TextView tvValue1;
    private TextView tvValue2;
    private TextView tvValue3;
    private TextView tvValue4;

    private LinearLayout lin_value0;
    private LinearLayout lin_value1;
    private LinearLayout lin_value2;
    private LinearLayout lin_value3;
    private LinearLayout lin_value4;



    private IAxisValueFormatter xAxisValueFormatter;
    DecimalFormat df = new DecimalFormat("0.00");

    public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.layout_markview);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvDate =  findViewById(R.id.tv_date);
       // tvDate.setVisibility(GONE);
        tvValue0 =  findViewById(R.id.tv_value0);
        tvValue1 =  findViewById(R.id.tv_value1);
        tvValue2 =  findViewById(R.id.tv_value2);
        tvValue3 =  findViewById(R.id.tv_value3);
        tvValue4 =  findViewById(R.id.tv_value4);

        lin_value0 =  findViewById(R.id.lin_value0);
        lin_value1 =  findViewById(R.id.lin_value1);
        lin_value2 =  findViewById(R.id.lin_value2);
        lin_value3 =  findViewById(R.id.lin_value3);
        lin_value4 =  findViewById(R.id.lin_value4);

    }



    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Chart chart = getChartView();
        if (chart instanceof LineChart) {
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();

            if(dataSetList.size() == 1){
                lin_value0.setVisibility(VISIBLE);
                lin_value1.setVisibility(GONE);
                lin_value2.setVisibility(GONE);
                lin_value3.setVisibility(GONE);
                lin_value4.setVisibility(GONE);
            }else if(dataSetList.size() == 2){
                lin_value0.setVisibility(VISIBLE);
                lin_value1.setVisibility(VISIBLE);
                lin_value2.setVisibility(GONE);
                lin_value3.setVisibility(GONE);
                lin_value4.setVisibility(GONE);
            }else if(dataSetList.size() == 3){
                lin_value0.setVisibility(VISIBLE);
                lin_value1.setVisibility(VISIBLE);
                lin_value2.setVisibility(VISIBLE);
                lin_value3.setVisibility(GONE);
                lin_value4.setVisibility(GONE);
            }else if(dataSetList.size() == 4){
                lin_value0.setVisibility(VISIBLE);
                lin_value1.setVisibility(VISIBLE);
                lin_value2.setVisibility(VISIBLE);
                lin_value3.setVisibility(VISIBLE);
                lin_value4.setVisibility(GONE);
            }else if(dataSetList.size() == 5){
                lin_value0.setVisibility(VISIBLE);
                lin_value1.setVisibility(VISIBLE);
                lin_value2.setVisibility(VISIBLE);
                lin_value3.setVisibility(VISIBLE);
                lin_value4.setVisibility(VISIBLE);
            }

            for (int i = 0; i < dataSetList.size(); i++) {

                try {
                    LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                    //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                    float y = dataSet.getValues().get((int) e.getX()).getY();

                    if (i == 0) {
                        tvValue0.setVisibility(VISIBLE);
                        tvValue0.setText(dataSet.getLabel() + "：" + df.format(y) );
                    }
                    if (i == 1) {
                        tvValue1.setVisibility(VISIBLE);
                        tvValue1.setText(dataSet.getLabel() + "：" + df.format(y) );
                    }
                    if (i == 2) {
                        tvValue2.setVisibility(VISIBLE);
                        tvValue2.setText(dataSet.getLabel() + "：" + df.format(y) );
                    }
                    if (i == 3) {
                        tvValue3.setVisibility(VISIBLE);
                        tvValue3.setText(dataSet.getLabel() + "：" + df.format(y) );
                    }
                    if (i == 4) {
                        tvValue4.setVisibility(VISIBLE);
                        tvValue4.setText(dataSet.getLabel() + "：" + df.format(y) );
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (i == 0) {
                        tvValue0.setVisibility(GONE);
                    }
                    if (i == 1) {
                        tvValue1.setVisibility(GONE);
                    }
                    if (i == 2) {
                        tvValue2.setVisibility(GONE);
                    }
                    if (i == 3) {
                        tvValue3.setVisibility(GONE);
                    }
                    if (i == 4) {
                        tvValue3.setVisibility(GONE);
                    }
                }
            }

                tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null) );
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}