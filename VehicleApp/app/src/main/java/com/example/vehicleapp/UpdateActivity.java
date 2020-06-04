package com.example.vehicleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class UpdateActivity extends AppCompatActivity implements Serializable {
    private TextView vehicleTextView;
    private EditText makeText;
    private EditText modelText;
    private EditText priceText;
    private EditText yearText;
    private EditText licenseText;
    private EditText colourText;
    private EditText numberDoorsText;
    private EditText transText;
    private EditText mileageText;
    private EditText fuelText;
    private EditText engineText;
    private EditText styleText;
    private EditText condText;
    private EditText notesText;
    private Button updateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setContentView(R.layout.activity_update);
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        vehicleTextView = findViewById(R.id.vehicleView);
        makeText = findViewById(R.id.editTextMake);
        modelText = findViewById(R.id.editTextModel);
        priceText = findViewById(R.id.editTextPrice);
        yearText = findViewById(R.id.editTextYear);
        licenseText = findViewById(R.id.editTextLicense);
        colourText = findViewById(R.id.editTextColour);
        numberDoorsText = findViewById(R.id.editTextNumDoors);
        transText = findViewById(R.id.editTextTransmission);
        mileageText = findViewById(R.id.editTextMileage);
        fuelText = findViewById(R.id.editTextFuel);
        engineText = findViewById(R.id.editTextEngine);
        styleText = findViewById(R.id.editTextBody);
        condText = findViewById(R.id.editTextCond);
        notesText = findViewById(R.id.editTextNotes);
        updateButton = findViewById(R.id.updateButton);

        final Vehicle theVehicle = (Vehicle) extras.get("vehicle");

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
        final String bodyStyle = theVehicle.getBody_style();
        String condition = theVehicle.getCondition();
        String notes = theVehicle.getNotes();

        //setting the text to the vehicle information passed over in order to update them
        makeText.setText(vehicleMake);
        modelText.setText(vehicleModel);
        yearText.setText(String.valueOf(year));
        priceText.setText(String.valueOf(price));
        licenseText.setText(licenseNumber);
        colourText.setText(colour);
        numberDoorsText.setText(String.valueOf(numberDoors));
        transText.setText(transmission);
        mileageText.setText(String.valueOf(mileage));
        fuelText.setText(fuelType);
        engineText.setText(String.valueOf(engineSize));
        styleText.setText(bodyStyle);
        condText.setText(condition);
        notesText.setText(notes);


        String vehicleText = "" + theVehicle.getMake() +" " + theVehicle.getModel();
        vehicleTextView.setText(vehicleText);
        //set the title to the car

        final HashMap<String, String> params = new HashMap<>();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ("http://10.0.2.2:8005/vehicles/api");
                Gson gson = new Gson();

                Vehicle vehicle = new Vehicle(vehicleID, makeText.getText().toString(), modelText.getText().toString(), Integer.parseInt(yearText.getText().toString()), Integer.parseInt(priceText.getText().toString()), licenseText.getText().toString(), colourText.getText().toString(), Integer.parseInt(numberDoorsText.getText().toString()), transText.getText().toString(), Integer.parseInt(mileageText.getText().toString()), fuelText.getText().toString(), Integer.parseInt(engineText.getText().toString()), styleText.getText().toString(), condText.getText().toString(), notesText.getText().toString());
                String myJson = gson.toJson(vehicle);

                //add the new updated vehicle to the hashmap
                params.put("vehicle", myJson);

                performPutCall(url, params);
            }
        });
    }
    public String performPutCall(String requestURL, HashMap<String, String>PutDataParams) {
        URL url;
        String response = "";

        try {
            url = new URL(requestURL);

            //create connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("PUT");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));


            writer.write(PutDataParams.get("vehicle"));
            writer.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.print(responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Toast.makeText(this, "Vehicle Updated 👍", Toast.LENGTH_SHORT).show();
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
        return response;
    }
}
