package vincentsurkijn.softdev.kuleuven.vampyre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

public class Login extends AppCompatActivity {

    public static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginButtonPressed();
                }
            });
        final Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonPressed();
            }
        });
    }

    private void loginButtonPressed() {
        //get data from activity
        final EditText emailinput = findViewById(R.id.emailText);
        final String email = emailinput.getText().toString().trim();
        final EditText passwordinput = findViewById(R.id.passwordText);
        final String password = passwordinput.getText().toString();

        setLoadOption();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_givePassword/"+email.toLowerCase();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        setRegOption();
                        try {
                            JSONObject Password = response.getJSONObject(0);
                            String passwordString = Password.getString("password");
                            if(password.equals(passwordString)){
                                user = email;
                                openMainActivity();
                                TextView ErrorView = findViewById(R.id.ErrorView);
                                String wrongpassword = getResources().getString(R.string.succes);
                                ErrorView.setText(wrongpassword);
                            } else {
                                TextView ErrorView = findViewById(R.id.ErrorView);
                                String wrongpassword = getResources().getString(R.string.wrongpassword);
                                ErrorView.setText(wrongpassword);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //account doesn't exist
                            TextView ErrorView = findViewById(R.id.ErrorView);
                            String noaccount = getResources().getString(R.string.accountdoesnotexist);
                            ErrorView.setText(noaccount);
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TextView ErrorView = findViewById(R.id.ErrorView);
                String unknownerror = getResources().getString(R.string.nointernet);
                ErrorView.setText(unknownerror);
                setRegOption();
            }
        });
        mQueue.add(request);



    }

    private void openMainActivity(){
        //open new activity
        Intent Main = new Intent(this, MainActivity.class);
        startActivity(Main);
    }

    private void registerButtonPressed(){
        Intent Register = new Intent(this, register.class);
        startActivity(Register);
    }

    private void setLoadOption(){
        final Button registerButton = findViewById(R.id.registerButton);
        registerButton.setVisibility(View.INVISIBLE);
        final TextView registerTitle = findViewById(R.id.registerTitle);
        registerTitle.setVisibility(View.INVISIBLE);
        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setVisibility(View.INVISIBLE);
        final RadioButton loggedIn = findViewById(R.id.loggedIn);
        loggedIn.setVisibility(View.INVISIBLE);
        final TextView loadingTitle = findViewById(R.id.loadingTitle);
        loadingTitle.setVisibility(View.VISIBLE);
        final ProgressBar loadingSign = findViewById(R.id.loadingSign);
        loadingSign.setVisibility(View.VISIBLE);

    }

    private void setRegOption(){
        final Button registerButton = findViewById(R.id.registerButton);
        registerButton.setVisibility(View.VISIBLE);
        final TextView registerTitle = findViewById(R.id.registerTitle);
        registerTitle.setVisibility(View.VISIBLE);
        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setVisibility(View.VISIBLE);
        final RadioButton loggedIn = findViewById(R.id.loggedIn);
        loggedIn.setVisibility(View.VISIBLE);
        final TextView loadingTitle = findViewById(R.id.loadingTitle);
        loadingTitle.setVisibility(View.INVISIBLE);
        final ProgressBar loadingSign = findViewById(R.id.loadingSign);
        loadingSign.setVisibility(View.INVISIBLE);
        final TextView ErrorView = findViewById(R.id.ErrorView);
        ErrorView.setVisibility(View.VISIBLE);
    }
}

/**
 * TODO
 * maak een autofill functie zodat de gebruiker makkelijker kan inloggen
 */
