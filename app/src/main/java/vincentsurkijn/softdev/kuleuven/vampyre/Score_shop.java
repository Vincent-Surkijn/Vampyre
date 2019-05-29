package vincentsurkijn.softdev.kuleuven.vampyre;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class Score_shop extends AppCompatActivity {

    private TextView AmountOfTickets_text;
    private int AmountOfTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_shop);
        AmountOfTickets_text = findViewById(R.id.AmountOfTokens_textShop);
        setAmountOfTickets();
    }

    public void setAmountOfTickets(){
        //Retrieve amount of tickets of user
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getAmountOfTokens/"+Login.user;
        url = url.replaceAll(" ","_");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject Tokens = response.getJSONObject(0);
                            AmountOfTickets = Tokens.getInt("tokens");
                            String TicketsText = "Your amount of Tickets: " + String.valueOf(AmountOfTickets);
                            AmountOfTickets_text.setText(TicketsText);
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

    public void oneTicketBuy(View v){
        ticketBuy(1, "Strips, pencils, kitchentowel");
    }

    public void twoTicketBuy(View v){
        ticketBuy(2, "Bodysol, cooler bag, ambulance toy, books, bal, red/white towel");
    }

    public void threeTicketBuy(View v){
        ticketBuy(3,"Blanket, EHBO set, Bodysol bodymilk ");
    }

    public void fourTicketBuy(View v){
        ticketBuy(4, "Beachtowel");
    }

    public void sixTicketBuy(View v){
        ticketBuy(6, "Cinema ticket, book");
    }

    public void sixteenTicketBuy(View v){
        ticketBuy(16, "Zoo ticket(1 to 17y or +60y)");
    }

    public void twentyTicketBuy(View v){
        ticketBuy(20, "Zoo ticket(18 to 60y)");
    }

    public void ticketBuy(int cost, String product){
        if (AmountOfTickets>0 & AmountOfTickets>=cost){
            AmountOfTickets-=cost;
            //adjust tickets in database
            RequestQueue mQueue = Volley.newRequestQueue(this);
            String url = "https://studev.groept.be/api/a18_sd209/APP_setAmountOfTokens/"+ AmountOfTickets +"/" +Login.user;
            url = url.replaceAll(" ","_");
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                           String TicketsText = "Your amount of Tickets: " + String.valueOf(AmountOfTickets);
                           AmountOfTickets_text.setText(TicketsText);
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }
            });
            mQueue.add(request);

            //Add order to database
            saveOrder(product, cost);
        }
    }

    public void saveOrder(String product, int cost){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_addPurchase/"+Login.user+"/"+product+"/"+cost;
        url = url.replaceAll(" ","_");
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
