package vincentsurkijn.softdev.kuleuven.vampyre;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import static vincentsurkijn.softdev.kuleuven.vampyre.Agenda.summaryAppointments;
import static vincentsurkijn.softdev.kuleuven.vampyre.Agenda.summaryAppointmentsIDs;

public class My_score extends AppCompatActivity {

    private int Amount;
    private int selectedid;
    private int[] costs;
    private ArrayList numbers;
    private TextView Amount_text;
    private ListView orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        Amount_text = findViewById(R.id.AmountOfTokens_textScore);
        numbers = new ArrayList<Integer>();

        orders = findViewById(R.id.orders);
        orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < parent.getChildCount();i++ ){
                    parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
                FloatingActionButton deleteButton = findViewById(R.id.deleteorderButton);
                deleteButton.show();
                view.setBackgroundColor(Color.RED);
                selectedid = position;
            }
        });

        FloatingActionButton deleteButton = findViewById(R.id.deleteorderButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue mQueue = Volley.newRequestQueue(My_score.this);
                String url = "https://studev.groept.be/api/a18_sd209/APP_removeOrder/"+numbers.get(selectedid);
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                updateOrders();
                                giveBackTickets();
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

        //set all variable that need to be retrieved from database
        setAmountOfTickets();
        updateOrders();
    }

    public void setAmountOfTickets(){
        //Retrieve amount of tickets of user
        RequestQueue mQueue = Volley.newRequestQueue(My_score.this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getAmountOfTokens/"+Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject Tokens = response.getJSONObject(0);
                            Amount = Tokens.getInt("tokens");
                            String TicketsText = "Your amount of Tickets: " + String.valueOf(Amount);
                            Amount_text.setText(TicketsText);
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

    public void updateOrders(){
        RequestQueue mQueue = Volley.newRequestQueue(My_score.this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getAllOrders/"+Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String order;
                        ArrayList summaryOrders = new ArrayList<String>();
                        try {
                            int i = 0;
                            costs = new int[response.length()];
                            while(i<response.length()){
                                JSONObject Orders = response.getJSONObject(i);
                                order = "Product: " + Orders.getString("Product")
                                        +"\n"+"Amount: "+Orders.getString("Amount")
                                        + "\n"+"Cost: "+Orders.getInt("Cost");
                                int number = Orders.getInt("Number");
                                int cost = Orders.getInt("Cost");
                                summaryOrders.add(order);
                                numbers.add(number);
                                costs[i] = cost;
                                i++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(My_score.this, android.R.layout.simple_list_item_1, summaryOrders);
                        orders.setAdapter(arrayAdapter);
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

    public void giveBackTickets(){
        Amount+= costs[selectedid];
        //adjust tickets in database
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_setAmountOfTokens/"+ Amount +"/" +Login.user;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        setAmountOfTickets();
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
