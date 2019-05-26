package vincentsurkijn.softdev.kuleuven.vampyre;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Agenda extends AppCompatActivity {

    static public ArrayList<String> summaryAppointments;
    static public ArrayList<Integer> summaryAppointmentsIDs;
    private String selectedlocation;
    private int selectedid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        updateAppointments();
        final FloatingActionButton addappointment = findViewById(R.id.addAppointmentButton);
        addappointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newappointment = new Intent(Agenda.this, newAppointment.class);
                startActivity(newappointment);
            }
        });

        final ListView appointments = findViewById(R.id.appointments);
        appointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < parent.getChildCount();i++ ){
                    parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                selectedlocation = summaryAppointments.get(position).split("\n")[1].trim();
                selectedid = summaryAppointmentsIDs.get(position);
                view.setBackgroundColor(Color.RED);
                FloatingActionButton surveyButton = findViewById(R.id.makesurveyButton);
                surveyButton.show();
                FloatingActionButton mapsButton = findViewById(R.id.gotogooglemapsButton);
                mapsButton.show();
                FloatingActionButton deleteButton = findViewById(R.id.deleteappointmetnButton);
                deleteButton.show();
            }
        });

        FloatingActionButton surveyButton = findViewById(R.id.makesurveyButton);
        surveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent survey = new Intent(Agenda.this, Survey.class);
                startActivity(survey);
            }
        });
        FloatingActionButton mapsButton = findViewById(R.id.gotogooglemapsButton);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search location coordinates
                RequestQueue mQueue = Volley.newRequestQueue(Agenda.this);
                String url = "https://studev.groept.be/api/a18_sd209/APP_getcoordinatesof/"+selectedlocation;
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    JSONObject coordinates = response.getJSONObject(0);
                                    double lat = coordinates.getDouble("lattitude");
                                    double longe = coordinates.getDouble("longitude");
                                    // Create a Uri from an intent string. Use the result to create an Intent.
                                    System.out.println("google.navigation:q="+lat+", "+longe);
                                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+", "+longe);

                                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    // Make the Intent explicit by setting the Google Maps package
                                    mapIntent.setPackage("com.google.android.apps.maps");

                                    // Attempt to start an activity that can handle the Intent
                                    startActivity(mapIntent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    System.out.println("No response");
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mQueue.add(request);

            }
        });
        FloatingActionButton deleteButton = findViewById(R.id.deleteappointmetnButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue mQueue = Volley.newRequestQueue(Agenda.this);
                String url = "https://studev.groept.be/api/a18_sd209/APP_removeAppointment/"+selectedid;
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                updateAppointments();
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mQueue.add(request);
            }
        });

        ImageButton homebutton = findViewById(R.id.Homebutton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Agenda.this, MainActivity.class);
                startActivity(home);
            }
        });


    }

    private void updateAppointments(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getAllAppointments/"+Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                            if(response.length() == 0){
                                TextView ErrorView = findViewById(R.id.errorMessage);
                                String unknownerror = getResources().getString(R.string.noappointment);
                                ErrorView.setText(unknownerror);
                                ErrorView.setVisibility(View.VISIBLE);
                                ListView listView = findViewById(R.id.appointments);
                                listView.setVisibility(View.GONE);
                            }
                        summaryAppointments = new ArrayList<String>();
                        summaryAppointmentsIDs = new ArrayList<Integer>();
                        for(int i = 0; i < response.length(); i++){
                            JSONObject appointment = null;
                            try {
                                appointment = response.getJSONObject(i);
                                String message = null;
                                try {
                                    message = bloodorplasma(appointment.getString("bloodorplasma"))
                                            +"\n"+appointment.getString("location")+"\n"
                                            +betterDateFormat(appointment.getString("date"));
                                    summaryAppointments.add(message);
                                    summaryAppointmentsIDs.add(appointment.getInt("afspraakid"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                            ListView appointments = findViewById(R.id.appointments);
                            ArrayAdapter arrayAdapter = new ArrayAdapter(Agenda.this, android.R.layout.simple_list_item_1, summaryAppointments);
                            appointments.setAdapter(arrayAdapter);
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TextView ErrorView = findViewById(R.id.errorMessage);
                String unknownerror = getResources().getString(R.string.nointernet);
                ErrorView.setText(unknownerror);
                ErrorView.setVisibility(View.VISIBLE);
                ListView constraintLayout = findViewById(R.id.appointments);
                constraintLayout.setVisibility(View.GONE);
            }
        });
        mQueue.add(request);


        /**
         * todo
         *
         * Als de afspraak aangeeft dat hij al is geweest veog hem dan niet in de lijst toe.
         */
    }

    private String bloodorplasma(String x){
        if(x.equals("B")){
            return "Blood";
        } if(x.equals("P")){
            return "Plasma";
        } else {
            return "uknown";
        }
    }

    private String betterDateFormat(String date){
        String betterdate = "Hour: "+date.substring(11,16)+" date: "+date.substring(8,10)+
                date.substring(4,7)+"-"+date.substring(0,4);
        return betterdate;
    }
}
