package com.example.harjoitustyo2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class JSONRequest {

    public void readJSON(String diet, String beef, String fish, String pork, String dairy, String cheese, String rice, String egg, String salad, String restaurant){
        String json = getJSON(diet, beef, fish, pork, dairy, cheese, rice, egg, salad, restaurant);

    }


    // Uses Climate Diet API
    public String getJSON(String diet, String bLevel, String fLevel, String pLevel, String dLevel, String cLevel, String rLevel, String eLevel, String sLevel, String rSpendind){
        String response = null;
        try {
            URL url = new URL("https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/FoodCalculator?query.diet=" + diet + "&query.beefLevel=" + bLevel + "&query.fishLevel=" + fLevel + "&query.porkPoultryLevel=" + pLevel + "&query.dairyLevel=" + dLevel + "&query.cheeseLevel=" + cLevel + "&query.riceLevel=" + rLevel + "&query.eggLevel=" + eLevel + "&query.winterSaladLevel=+" + sLevel + "&query.restaurantSpending=" + rSpendind);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            response = sb.toString();
            in.close();
            br.close();

        }catch (ProtocolException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("***GETJSON TOIMII***");
        }

        return response;
    }
}