package com.example.vehicleapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import javax.net.ssl.HttpsURLConnection;

public class addNewActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_add_new);

        final EditText vehicle_id = findViewById(R.id.editTextVehicle_id);
        final EditText make = findViewById(R.id.editTextMake);
        final EditText model = findViewById(R.id.editTextModel);
        final EditText year = findViewById(R.id.editTextYear);
        final EditText price = findViewById(R.id.editTextPrice);
        final EditText license_number = findViewById(R.id.editTextLicense);
        final EditText colour = findViewById(R.id.editTextColour);
        final EditText number_doors = findViewById(R.id.editTextNumDoors);
        final EditText transmission = findViewById(R.id.editTextTrans);
        final EditText mileage = findViewById(R.id.editTextMileage);
        final EditText fuel_type = findViewById(R.id.editTextFuel);
        final EditText engine_size = findViewById(R.id.editTextEngine);
        final EditText body_style = findViewById(R.id.editTextBody);
        final EditText condition = findViewById(R.id.editTextCondition);
        final EditText notes = findViewById(R.id.editTextNotes);


        final HashMap<String, String> params = new HashMap<>();

        Button addNewButton = findViewById(R.id.buttonAddVehicle);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();

                Vehicle vehicle = new Vehicle(Integer.parseInt(vehicle_id.getText().toString()), make.getText().toString(), model.getText().toString(), Integer.parseInt(year.getText().toString()), Integer.parseInt(price.getText().toString()), license_number.getText().toString(), colour.getText().toString(), Integer.parseInt(number_doors.getText().toString()), transmission.getText().toString(), Integer.parseInt(mileage.getText().toString()), fuel_type.getText().toString(), Integer.parseInt(engine_size.getText().toString()), body_style.getText().toString(), condition.getText().toString(), notes.getText().toString());
                String myJson = gson.toJson(vehicle);

                params.put("vehicle", myJson);
                String url = ("http://10.0.2.2:8005/vehicles/api");
                performPostCall(url, params);
            }
        });
    }

    public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";

        try {
            url = new URL(requestURL);

            //create connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write((postDataParams).get("vehicle"));
            writer.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.print(responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Toast.makeText(this, "Vehicle Saved 👍", Toast.LENGTH_SHORT).show();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                response = "";
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        System.out.print("response: " + response);
        return response;
    }

   }

