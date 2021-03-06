package com.example.harjoitustyo2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainPage extends AppCompatActivity {
    TextView totalEmission, dailyWeight, bmiView, nameView, caffeineView, municipalityView, ageView;
    CardView climateButton, weightButton, caffeineButton;
    ImageButton logOut;
    Button profileBtn;
    Context context;

    String emission = null, caffeine = null, weight, username, name;
    double BMI;
    int age;

    JSONFileControl jsonFileControl;
    User user;
    DatabaseHelper databaseHelper;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        dailyWeight = findViewById(R.id.weightView);
        bmiView = findViewById(R.id.bmiView);
        climateButton = findViewById(R.id.climateCard);
        weightButton = findViewById(R.id.weightCard);
        caffeineButton = findViewById(R.id.caffeineCard);
        logOut = findViewById(R.id.logOutButton);
        nameView = findViewById(R.id.nameView);
        caffeineView = findViewById(R.id.caffeineView);
        municipalityView = findViewById(R.id.municipalityView);
        ageView = findViewById(R.id.ageView);
        profileBtn = findViewById(R.id.profileButton);

        totalEmission = findViewById(R.id.coEmission);
        context = MainPage.this;

        jsonFileControl = new JSONFileControl();

        username = getIntent().getStringExtra("Username");
        databaseHelper = new DatabaseHelper(this);

        user = new User(context, username);
        name = user.getName();

        nameView.setText(name);
        municipalityView.setText(user.getMunicipality());

        try {
            weight = jsonFileControl.readLog(context, name, "Weight");
            dailyWeight.setText("Current weight: " + weight + "kg");
        } catch (Exception e) {
            e.printStackTrace();
        } try {
            emission = jsonFileControl.readLog(context, name, "Total");
        } catch (Exception e) {
            e.printStackTrace();
        } try {
            caffeine = jsonFileControl.readLog(context, name, "Caffeine");
        } catch (Exception e){
            e.printStackTrace();
        }

        String emissionText;
        String caffeineText;



        if (emission != null){
            emissionText = "Total CO2 emission: " + modifyValue(emission) + " kg per year";
        } else {
            emissionText = "Calculate your CO2 emission!";
        }

        if (caffeine != null){
            caffeineText = "Today's caffeine consumption: " + modifyValue(caffeine) + " mg!";
        } else {
            caffeineText = "Calculate your caffeine consumption!";
        }

        totalEmission.setText(emissionText);
        caffeineView.setText(caffeineText);
        calculateAge();
        String ageText = "Age: " + age;
        ageView.setText(ageText);
        setBMI();


        // Buttons OnClickListeners
        climateButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, ClimateControl.class);
            intent.putExtra("Username", username);
            startActivity(intent);
        });

        weightButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, WeightControl.class);
            intent.putExtra("Username", username);
            startActivity(intent);
        });

        caffeineButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, CaffeineControl.class);
            intent.putExtra("Username", username);
            startActivity(intent);
        });

        profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, UpdatePassword.class);
            intent.putExtra("Username", username);
            startActivity(intent);
        });

        logOut.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, Login.class);
            startActivity(intent);
        });

    }

    // Sets BMI value to the dashboard
    public void setBMI(){
        bmiCalculator();
        DecimalFormat df = new DecimalFormat("0.00");
        String text = null;

        if (BMI < 18.5){
            text = "BMI: " + df.format(BMI) + " - Underweight";
        } else if (BMI >= 18.5 && BMI < 24.9){
            text = "BMI: " + df.format(BMI) + " - Normal weight";
        } else if (BMI >= 24.9 && BMI < 29.9){
            text = "BMI: " + df.format(BMI) + " - Overweight";
        } else if (BMI >= 29.9){
            text = "BMI: " + df.format(BMI) + " - Obese";
        }
        bmiView.setText(text);
    }


    // Calculates BMI value
    public void bmiCalculator(){
        String height = databaseHelper.getHeight(username);

        double heightDouble = Double.parseDouble(height)/100;
        try {
            double weightDouble = Double.parseDouble(weight);
            BMI = weightDouble / (heightDouble * heightDouble);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Rounding double value to integer
    private int round(double d){
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if(result<0.5){
            return d<0 ? -i : i;
        }else{
            return d<0 ? -(i+1) : i+1;
        }
    }

    // Modifying decimal number to integer
    private String modifyValue(String value) {
        double d = Double.parseDouble(value);
        int v = round(d);
        return String.valueOf(v);
    }

    // Calculates age
    private void calculateAge(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int birthyear = databaseHelper.getBirthyear(username);
        age = year - birthyear;
    }


}