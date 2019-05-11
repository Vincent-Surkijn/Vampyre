package vincentsurkijn.softdev.kuleuven.vampyre;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private ArrayList<String> summaryAppointments;

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

        ListView appointments = findViewById(R.id.appointments);
        appointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showappointment = new Intent(Agenda.this, showAppointment.class);
                startActivity(showappointment);
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
                        final ArrayList<String> summaryAppointments = new ArrayList<String>();
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
                ConstraintLayout constraintLayout = findViewById(R.id.ConstrainLayout);
                constraintLayout.setVisibility(View.GONE);
            }
        });
        mQueue.add(request);

        /**
         * todo
         *
         * probeer een list te maken met knoppen. (extra xml en java classen nodig)
         * knoppen voor bv. vragenlijst in te vullen
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
