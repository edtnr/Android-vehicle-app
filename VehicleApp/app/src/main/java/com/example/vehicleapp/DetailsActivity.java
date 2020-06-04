package com.example.vehicleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DetailsActivity extends AppCompatActivity implements Serializable {

    private TextView nameView;
    private TextView idView;
    private TextView priceView;
    private TextView yearView;
    private TextView licenseView;
    private TextView colourView;
    private TextView numberDoorsView;
    private TextView transView;
    private TextView mileageView;
    private TextView fuelView;
    private TextView engineView;
    private TextView styleView;
    private TextView condView;
    private TextView notesView;
    private Button removeButton;
    private Vehicle theVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();
        removeButton = findViewById(R.id.buttonDelete);
        //Get the vehicle from the previous page
        theVehicle = (Vehicle) extras.get("vehicle");

        //Create a new vehicle object from the object we've received
        final int vehicleID = theVehicle.getVehicle_id();
        String vehicleMake = theVehicle.getMake();
        String vehicleModel = theVehicle.getModel();
        int year = theVehicle.getYear();
        int price = theVehicle.getPrice();
        String licenseNumber = theVehicle.getLicense_number();
        String colour = theVehicle.getColour();
        int numberDoors = theVehicle.getNumber_doors();
        String transmission = theVehicle.getTransmission();
        int mileage = theVehicle.getMileage();
        String fuelType = theVehicle.getFuel_type();
        int engineSize = theVehicle.getEngine_size();
        String bodyStyle = theVehicle.getBody_style();
        String condition = theVehicle.getCondition();
        String notes = theVehicle.getNotes();

        nameView = findViewById(R.id.nameView);
        idView = findViewById(R.id.IDView);
        priceView = findViewById(R.id.priceView);
        yearView = findViewById(R.id.yearView);
        licenseView = findViewById(R.id.licenseView);
        colourView = findViewById(R.id.colourView);
        numberDoorsView = findViewById(R.id.numberDoorsView);
        transView = findViewById(R.id.transView);
        mileageView = findViewById(R.id.mileageView);
        fuelView = findViewById(R.id.fuelView);
        engineView = findViewById(R.id.engineView);
        styleView = findViewById(R.id.styleView);
        condView = findViewById(R.id.condView);
        notesView = findViewById(R.id.notesView);

        //set the texts to display the information from the vehicle
        nameView.setText(vehicleMake + " " + vehicleModel);
        idView.setText("Vehicle ID: "+vehicleID);
        yearView.setText("Year of Manufacture: " + year);
        colourView.setText("Colour: " + colour);
        numberDoorsView.setText("Number of Doors: " + numberDoors);
        transView.setText("Transmission: " + transmission);
        mileageView.setText("Mileage: " + mileage);
        fuelView.setText("Fuel Type: " +fuelType);
        engineView.setText("Engine Size: " + engineSize);
        styleView.setText("Style: " + bodyStyle);
        condView.setText("Condition: " + condition);
        notesView.setText("Notes: " + notes);

        licenseView.setText(licenseNumber);
        priceView.setText("£"+price);

        //create a hashmap to send over for the delete function
        final HashMap<String, Integer> params = new HashMap<>();

        //onclick listener to detect if the user wishes to remove a vehicle
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define the url to connect to
                String url = ("http://10.0.2.2:8005/vehicles/api");

                //put the information we want to send into the hashmap
                params.put("vehicle_id", vehicleID);

                //call the function
                performDeleteCall(url, params);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.details_menu, menu);
        return true;
    }

    //If a user chooses to update a vehicle we use intent.putextra to send it to the update activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:

                Toast.makeText(DetailsActivity.this, "you pressed to update a vehicle",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                intent.putExtra("vehicle", theVehicle);
                startActivity(intent);
        }
        return true;
    }


    //performdeletecall is the function for when the user wishes to delete a car from the database, we send over information to the server in order for it to perform the delete function
    public String performDeleteCall(String requestURL, HashMap<String, Integer>DeleteDataParams) {
        URL url;
        String response = "";

        try {
            url = new URL(requestURL);

            //create connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("DELETE");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(DeleteDataParams.get("vehicle_id").toString());
            writer.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.print(responseCode);

            //if everything has worked the user will receive a toast message to let them know
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Toast.makeText(this, "Vehicle Deleted 👍", Toast.LENGTH_SHORT).show();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    response += line;
                }

            } else {
                //if not the toast message will say "Error"
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                response = "";
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return response;
    }
}
