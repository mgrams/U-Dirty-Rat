package edu.gatech.jjmae.u_dirty_rat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // When this button (logout) is hit, we should go back to welcome screen.
        // User should also be logged out
        Button next1 = (Button) findViewById(R.id.button3);
        next1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UserData.setCurrentUser(null);
                Intent myIntent = new Intent(view.getContext(), WelcomeActivity.class);
                startActivityForResult(myIntent, 0);
            }

        });



        // When this button (add a rat entry) is hit, ...
        Button next2 = (Button) findViewById(R.id.button8);
        next2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), CreateNewRatSighting.class);
                startActivityForResult(myIntent, 0);
            }

        });

        // When this button (view rat entries) is hit, ...
        Button next3 = (Button) findViewById(R.id.button7);
        next3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onLoadButtonPressed(view);
            }

        });
    }

    public void onLoadButtonPressed(View view) {
        Log.v(HomeActivity.TAG, "Pressed the load button");
        if (firstTime) {
            readSDFile();
            firstTime = false;
        }
        Intent intent = new Intent(this, RatSightingsList.class);
        startActivity(intent);
    }

    private void readSDFile() {
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

                model.addItem(new RatSightingDataItem(id, entryDate, tokens[7], zip, tokens[9], tokens[16], tokens[23], latitude, longitude));
            }
            br.close();
        } catch (IOException e) {
            Log.e(HomeActivity.TAG, "error reading assets", e);
        }

    }

    @Override
    public void onBackPressed() {
        // prevent back button from being pressed
    }

}
