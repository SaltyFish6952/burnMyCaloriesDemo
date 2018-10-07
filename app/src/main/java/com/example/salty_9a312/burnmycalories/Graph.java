package com.example.salty_9a312.burnmycalories;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Graph {

    private GraphView graphView;
    private LineGraphSeries<DataPoint> dataSeries;

    public Graph(GraphView id){

        graphView = id;

        dataSeries = new LineGraphSeries<>();

        graphView.addSeries(dataSeries);

    }



}
