package vincentsurkijn.softdev.kuleuven.vampyre;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class MainActivity extends AppCompatActivity


        implements NavigationView.OnNavigationItemSelectedListener {

    private int amountBloodDays;
    private int amountPlasmaDays;

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
        setAmountBloodDays();
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

        if (id == R.id.locations) {
            // Handle the locations action
            Intent intent = new Intent(this, Locations.class);
            startActivity(intent);
        } else if (id == R.id.agenda) {
            Intent intent = new Intent(this, Agenda.class);
            startActivity(intent);
        } else if (id == R.id.add_appointment) {
            Intent intent = new Intent(this, Add_appointment.class);
            startActivity(intent);
        } else if (id == R.id.my_score) {
            Intent intent = new Intent(this, My_score.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToLocations(View v){
        Intent intent = new Intent(this, Locations.class);
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

    public void setAmountBloodDays() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/getDaysUntilBloodDonation/"+Login.getUser().toLowerCase();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject amountOfDays = response.getJSONObject(0);
                            String date = amountOfDays.getString("nexttimeblood");
                            System.out.println(date);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //account doesn't exist
                            System.out.println("error");
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("error2");
                /*TextView ErrorView = findViewById(R.id.ErrorView);
                String unknownerror = getResources().getString(R.string.nointernet);
                ErrorView.setText(unknownerror);*/
            }
        });
        mQueue.add(request);
    }

    public void setAmountPlasmaDays() {
        //this.amountPlasmaDays = amountPlasmaDays;
    }

    public void setTextAmountBloodDays() {
        TextView textBloodDays = findViewById(R.id.amountBloodDays);
        textBloodDays.setHint("@string/amount_of_days_until_next_blood_donation" + String.valueOf(amountBloodDays));
    }

    public void setTextAmountPlasmaDays() {
        TextView textPlasmaDays = findViewById(R.id.AmountPlasmaDays);
        textPlasmaDays.setHint("@string/amount_of_days_until_next_blood_donation" + String.valueOf(amountPlasmaDays));
    }



}
