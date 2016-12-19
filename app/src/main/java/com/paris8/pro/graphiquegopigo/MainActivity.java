package com.paris8.pro.graphiquegopigo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> series;
    private static final Random RANDOM = new Random();
    private int lastX = 0;
    double x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x = 0.0;

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Distance de l'obstacle");
        graph.setTitleTextSize(80);
        graph.setTitleColor(Color.BLUE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("temps");
        graph.getGridLabelRenderer().setVerticalAxisTitle("distance");

        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        //viewport.setMinY(-3);
        viewport.setMaxY(10);

        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(100);

        viewport.setScrollable(true);

        series = new LineGraphSeries<DataPoint>();
        series.setColor(Color.MAGENTA);

        graph.addSeries(series);
    }

    private void entreeAleat(){
        y = RANDOM.nextDouble() * 5;
        series.appendData(new DataPoint(x++,y),false, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<=100;i++){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            entreeAleat();
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
