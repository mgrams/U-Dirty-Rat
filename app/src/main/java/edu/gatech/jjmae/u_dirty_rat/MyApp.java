package edu.gatech.jjmae.u_dirty_rat;

import android.app.Application;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import edu.gatech.jjmae.u_dirty_rat.controller.HomeActivity;
import edu.gatech.jjmae.u_dirty_rat.model.RatSightingDataItem;
import edu.gatech.jjmae.u_dirty_rat.model.SampleModel;
import edu.gatech.jjmae.u_dirty_rat.model.UserData;

/**
 * Created by Madison on 10/22/2017.
 */

public class MyApp extends Application {
    public MyApp() {
        // this method fires only once per application start.
        // getApplicationContext returns null here

        Log.i("main", "Constructor fired");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // this method fires once as well as constructor
        // but also application has context here

        Log.i("main", "onCreate fired");
        File file = new File(getApplicationContext().getFilesDir(), "userData.txt");
        loadText(file, true);

        File file2 = new File(getApplicationContext().getFilesDir(), "ratData.txt");
        loadText(file2, false);

        if (SampleModel.INSTANCE.getItems().size() < 100000) { //when app is used for first time, add csv file to model and save the file
            readCSVFile();
            File file3 = new File(this.getFilesDir(), "ratData.txt");
            SampleModel.INSTANCE.saveText(file);
        }

    }

    public boolean loadText(File file, boolean isUserData) {
        try {
            //make an input object for reading

            BufferedReader reader = new BufferedReader(new FileReader(file));
            if (isUserData) {
                UserData.loadFromText(reader);
            } else {
               SampleModel.INSTANCE.loadFromText(reader);
            }

        } catch (FileNotFoundException e) {
            Log.e("MyApp", "Failed to open text file for loading!");
            return false;
        }

        return true;
    }

    /**
     * method that reads in the csv file
     * reads in entire csv file and records data into a SampleModel
     */
    private void readCSVFile() {
        SampleModel model = SampleModel.INSTANCE;

        try {
            InputStream is = getResources().openRawResource(R.raw.rat_sightings);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String line;
            br.readLine(); //get rid of header line
            while ((line = br.readLine()) != null) {
                //Log.d(HomeActivity.TAG, line);
                String[] tokens = line.split(",");
                int id = 0;
                int zip = 0;
                try {
                    id = Integer.parseInt(tokens[0]);
                } catch (Exception e) {
                }
                try {
                    zip = Integer.parseInt(tokens[8]);
                } catch (Exception e) {
                }
                double latitude =  0.0;
                double longitude = 0.0;
                try {
                    latitude = Double.parseDouble(tokens[49]);
                    longitude = Double.parseDouble(tokens[50]);
                } catch (IndexOutOfBoundsException e) {

                }
                Date entryDate = new Date(1969, 12, 31);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    entryDate = df.parse(tokens[1]);
                } catch (Exception e) {

                }

                model.addItem(new RatSightingDataItem(id, entryDate, tokens[7], zip, tokens[9], tokens[16], tokens[23], latitude, longitude), false);
            }
            br.close();
        } catch (IOException e) {
            Log.e("MyApp", "error reading assets", e);
        }

    }


}
