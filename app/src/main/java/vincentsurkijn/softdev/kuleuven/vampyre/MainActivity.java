package vincentsurkijn.softdev.kuleuven.vampyre;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String bloodDateString;
    private String plasmaDateString;
    private String bloodgroup;
    private int tickets;
    private TextView AmountOfBlooddays;
    private TextView AmountOfPlasmaDays;
    private TextView Bloodgroups_text;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bloodgroup = "";
        bloodDateString = "Date of next possible blood donation: ";
        plasmaDateString = "Date of next possible plasma donation: ";
        Bloodgroups_text = findViewById(R.id.bloodgroup);
        AmountOfPlasmaDays = findViewById(R.id.amountplasmadays_text);
        AmountOfBlooddays = findViewById(R.id.amountblooddays_text);

        //set al variables that need to be called from database
        setNeededBloodgroups();
        setDateOfNextBloodDonation();
        setDateOfNextPlasmaDonation();
        getAmountOfTickets();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.agenda) {
            Intent intent = new Intent(this, Agenda.class);
            startActivity(intent);
        } else if (id == R.id.my_score) {
            Intent intent = new Intent(this, My_score.class);
            startActivity(intent);
        } else if (id == R.id.add_appointment){
            Intent intent = new Intent(this, newAppointment.class);
            startActivity(intent);
        } else if (id == R.id.Support){
            Intent intent = new Intent(this, Support.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void goToLocations(View v){
        Intent intent = new Intent(this, newAppointment.class);
        startActivity(intent);
    }

    public void goToFactsArticle(View v){
        Intent intent = new Intent(this, FactsArticle.class);
        startActivity(intent);
    }

    public void goToScoreShop(View v){
        Intent intent = new Intent(this, Score_shop.class);
        startActivity(intent);
    }

    public void openWebURL( View v ) {
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( "https://redcrosschat.org/2014/07/23/susans-story-how-donating-blood-saved-her-life/ ") );
        startActivity( browse );
    }

    public void goToSupport(View v){
        Intent intent = new Intent(this, Support.class);
        startActivity(intent);
    }

    public void setDateOfNextBloodDonation(){
        //Retrieve date of next blood donation
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getLastAppointment/"+Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject appointments = response.getJSONObject(0);
                            String date = appointments.getString("date");

                            String pattern = "yyyy-MM-dd HH:mm:ss";
                            simpleDateFormat = new SimpleDateFormat(pattern);

                            System.out.println(date);

                            try {
                                Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                                Calendar cl = Calendar. getInstance();
                                cl.setTime(date2);
                                cl. add(Calendar.MONTH, 2);
                                bloodDateString += simpleDateFormat.format(cl.getTime()) ;
                                AmountOfBlooddays.setText(bloodDateString);

                            }
                            catch (ParseException e){
                                System.out.println("Error");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void setDateOfNextPlasmaDonation(){
        //Retrieve date of next plasma donation
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getLastAppointment/"+Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject appointments = response.getJSONObject(0);
                            String date = appointments.getString("date");

                            String pattern = "yyyy-MM-dd HH:mm:ss";
                            simpleDateFormat = new SimpleDateFormat(pattern);

                            System.out.println(date);

                            try {
                                Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                                Calendar cl = Calendar. getInstance();
                                cl.setTime(date2);
                                cl. add(Calendar.HOUR,336 );
                                plasmaDateString += simpleDateFormat.format(cl.getTime()) ;
                                AmountOfPlasmaDays.setText(plasmaDateString);
                            }
                            catch (ParseException e){
                                System.out.println("Error");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void setNeededBloodgroups(){
        //Retrieve needed bloodgroups
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getNeededBloodgroups";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int i = 0;
                            while(i<response.length()){
                            JSONObject Bloodgroups = response.getJSONObject(i);
                            bloodgroup += Bloodgroups.getString("Bloodgroup");

                            bloodgroup += " ";

                            i++;
                            }

                            Bloodgroups_text.setText(bloodgroup);

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void updateAmountOfTickets(){
        //Calculate tickets that have been earned
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getAttendedDonations/" + Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int i = 0;
                            while(i<response.length()){
                                JSONObject Bloodgroups = response.getJSONObject(i);
                                if(Bloodgroups.getString("bloodorplasma").equals("P")){
                                    tickets+=2;  //+2 voor plasmadonaties
                                }
                                else if (Bloodgroups.getString("bloodorplasma").equals("B")){
                                    tickets+=1;  //+1 voor bloeddonaties
                                }
                                i++;
                            }
                            //Change value in database
                            setAmountOfTickets();

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void getAmountOfTickets(){
        //Retrieve amount of tickets of user
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getAmountOfTokens/"+Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject Tokens = response.getJSONObject(0);
                            tickets = Tokens.getInt("tokens");
                            //Update the amount based on went appointments
                            updateAmountOfTickets();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void setAmountOfTickets(){
        //Set amount of tickets of user in database
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_setAmountOfTokens/"+tickets+"/"+Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Delete the appointments that have been used to calculate new score
                        deletePassedAppointments();
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

    public void deletePassedAppointments(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_deletePassedAppointments/"+Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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

}
