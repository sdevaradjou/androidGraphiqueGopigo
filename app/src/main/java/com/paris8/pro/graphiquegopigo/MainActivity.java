package com.paris8.pro.graphiquegopigo;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> seriesA;
    private LineGraphSeries<DataPoint> seriesB;
    private static final Random RANDOM = new Random();
    GraphView graph;
    Thread nouveauThreadA, nouveauThreadB;
    Button dessine, efface, pause;
    boolean stop = false;
    double a, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        graph = (GraphView) findViewById(R.id.graph);
        dessine = (Button) findViewById(R.id.bouton_dessine);
        efface = (Button) findViewById(R.id.bouton_efface);
        pause = (Button) findViewById(R.id.bouton_pause);

        construireGraph();

        dessine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dessinerGraph();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseGraph();
            }
        });

        efface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                effacerGraph();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void dessinerGraph() {
        if (!nouveauThreadA.isAlive() || seriesA.isEmpty()) {
            nouveauThreadA.start();
        }
    }

    private void pauseGraph() {
        /*if (stop) {
            nouveauThreadA.interrupt();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nouveauThreadB.run();
            stop = false;
        } else {
            nouveauThreadB.interrupt();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nouveauThreadA.run();
            stop = true;
        }*/
        if (!nouveauThreadB.isAlive() || !seriesA.isEmpty()) {
            nouveauThreadB.start();
        }
    }

    private void effacerGraph() {
        if (!seriesA.isEmpty() || !seriesB.isEmpty()) {
            graph.removeAllSeries();
        }
    }

    private void genDonnees() {
        double x, y;
        for (int i = 0; i < 50; i++) {
            y = RANDOM.nextDouble() * 5;
            seriesA.appendData(new DataPoint(a++, y), false, 50);
        }
    }

    private DataPoint[] genDonneesVide() {
        double x, y;
        DataPoint[] values = new DataPoint[50];
        for (int i = 0; i < 50; i++) {
            x = i;
            y = RANDOM.nextDouble() * 0;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }


    private void construireGraph() {
        a = 0;

        graph.setTitle("Distance de l'obstacle");
        graph.setTitleTextSize(80);
        graph.setTitleColor(Color.BLUE);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("temps");
        graph.getGridLabelRenderer().setVerticalAxisTitle("distance");

        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMaxY(10);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(50);
        viewport.setScrollable(false);
        viewport.setScalable(true);

        seriesA = new LineGraphSeries<>();
        seriesB = new LineGraphSeries<>();
        seriesA.setColor(Color.GREEN);
        seriesB.setColor(Color.CYAN);

        graph.addSeries(seriesA);
        graph.addSeries(seriesB);
    }

    @Override
    protected void onResume() {
        super.onResume();

        nouveauThreadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            b = RANDOM.nextDouble() * 5;
                            seriesA.appendData(new DataPoint(a++, b), false, 50);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        nouveauThreadB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seriesA.resetData(genDonneesVide());
                            //seriesB.resetData(genDonneesVide());
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}

