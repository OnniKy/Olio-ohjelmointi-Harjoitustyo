package com.example.harjoitustyo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class WeightControl extends AppCompatActivity {

    EditText addWeight;
    Button dailyWeightButton, back;
    Context context;
    String username, name;
    Double d;

    DatabaseHelper databaseHelper;
    JSONFileControl jsonFileControl;
    Graphs graphs;

    LineGraphSeries<DataPoint> series;
    GraphView weightGraph;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_control);

        addWeight = findViewById(R.id.addWeight);
        dailyWeightButton = findViewById(R.id.dailyWeightButton);
        back = findViewById(R.id.backC);

        context = WeightControl.this;
        username = getIntent().getStringExtra("Username");

        databaseHelper = new DatabaseHelper(this);
        graphs = new Graphs();
        jsonFileControl = new JSONFileControl();

        weightGraph = findViewById(R.id.graph);
        weightGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        weightGraph.getViewport().setXAxisBoundsManual(true);
        weightGraph.getViewport().setMinX(0);




        try {
            name = databaseHelper.getName(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        series = graphs.createGraph(context, name, "Weight");
        weightGraph.addSeries(series);
        series.setColor(getResources().getColor(R.color.green));

        d = series.getHighestValueX();
        weightGraph.getViewport().setMaxX(d.intValue());


        dailyWeightButton.setOnClickListener(v -> {
            String addWeight1 = addWeight.getText().toString();
            //Sorting inappropriate input out
            if (addWeight1.isEmpty() ){
                Toast.makeText(this, "Enter your weight first!", Toast.LENGTH_SHORT).show();
            } else {
                if (!addWeight1.matches("\\d+(?:\\.\\d+)?")) {
                    Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
                } else {
                    jsonFileControl.writeLog(addWeight1, context, name, "Weight");
                    series = graphs.createGraph(context, name, "Weight");
                    try {
                        weightGraph.removeAllSeries();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    d = series.getHighestValueX();
                    weightGraph.getViewport().setMaxX(d.intValue());
                    weightGraph.addSeries(series);
                    series.setColor(getResources().getColor(R.color.green));
                    Toast.makeText(this, "Weight updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(WeightControl.this, MainPage.class);
            intent.putExtra("Username", username);
            startActivity(intent);
        });

    }

}