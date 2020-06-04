package com.example.vehicleapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {

    //defining the Arrays and lists before the onCreate so they will be global variables
    private String[] vehicleNames;
    private ListView vehicleList;

    private ArrayList<Vehicle> allVehicles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        vehicleList = findViewById(R.id.vehicleList);

        HttpURLConnection urlConnection;
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        try {
            //define the url for connecting
            URL url = new URL("http://10.0.2.2:8005/vehicles/api");
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        String response = convertStreamToString(in);
        System.out.println("Server response=" + response);

        try {
            //start a jsonArray to hold the vehicle makes and models
            JSONArray jsonArray = new JSONArray(response);
            vehicleNames = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {

                //create a new vehicle for the information obtained and add it to a new list
                int vehicleID = Integer.parseInt(jsonArray.getJSONObject(i).get("vehicle_id").toString());
                String make = jsonArray.getJSONObject(i).get("make").toString();
                String model = jsonArray.getJSONObject(i).get("model").toString();
                int year =   Integer.parseInt(jsonArray.getJSONObject(i).get("year").toString());
                int price =   Integer.parseInt(jsonArray.getJSONObject(i).get("price").toString());
                String license_number = jsonArray.getJSONObject(i).get("license_number").toString();
                String colour =jsonArray.getJSONObject(i).get("colour").toString();
                int number_doors =  Integer.parseInt(jsonArray.getJSONObject(i).get("number_doors").toString());
                String transmission = jsonArray.getJSONObject(i).get("transmission").toString();
                int mileage =  Integer.parseInt(jsonArray.getJSONObject(i).get("mileage").toString());
                String fuel_type = jsonArray.getJSONObject(i).get("fuel_type").toString();
                int engine_size =  Integer.parseInt(jsonArray.getJSONObject(i).get("engine_size").toString());
                String body_style = jsonArray.getJSONObject(i).get("body_style").toString();
                String condition = jsonArray.getJSONObject(i).get("condition").toString();
                String notes = jsonArray.getJSONObject(i).get("notes").toString();

                vehicleNames[i] = make + " " + model + " (" + year + ")";

                Vehicle vehicle = new Vehicle(vehicleID, make, model, year, price,license_number, colour, number_doors, transmission, mileage, fuel_type, engine_size, body_style, condition, notes);

                allVehicles.add(vehicle);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Array adapter used in order to set the name of the list items
        ArrayAdapter vehicleListAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, (vehicleNames) );
        vehicleList.setAdapter(vehicleListAdapter);

        //Set an item click listener in order to start a function when clicked
        vehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "you pressed " + allVehicles.get(i).getMake(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                Vehicle vehicle = allVehicles.get(i);
                //add the vehicle to the intent in order to start the next feature
                intent.putExtra("vehicle", vehicle);
                startActivity(intent);
            }
        });


    }

    //Heres where we add the new taskbar so we can add a new vehicle
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    //this is where we add the instructions for when a user chooses to add a new vehicle
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNew:
                Toast.makeText(MainActivity.this, "you pressed to add a new vehicle",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), addNewActivity.class);
                startActivity(intent);
        }
        return true;
    }

    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    }


