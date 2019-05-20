package vincentsurkijn.softdev.kuleuven.vampyre;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonpressed();
            }
        });
    }
    private void submitButtonpressed(){
        boolean succesful = true;
        //read all data
            //obligated data
        final EditText email = findViewById(R.id.emailText);
        final String emailadress = email.getText().toString();
        if(emailadress.isEmpty() || !(0 < emailadress.indexOf('@') && emailadress.indexOf('@') < emailadress.indexOf('.',emailadress.indexOf('@')))){
                succesful = false;
                final TextView emailError = findViewById(R.id.emailError);
                String erroremailstring = getResources().getString(R.string.noemail);
                emailError.setText(erroremailstring);
                emailError.setVisibility(View.VISIBLE);
        }

        final EditText pass1 = findViewById(R.id.passwordText);
        final String pass1String = pass1.getText().toString();
        final EditText pass2 = findViewById(R.id.repeatpasswordText);
        String pass2String = pass2.getText().toString();
        if(pass1String.isEmpty() || !pass1String.equals(pass2String)){
            succesful = false;
            final TextView passError = findViewById(R.id.passwordError);
            String erroremailstring;
            if(!pass1String.equals(pass2String)){
                erroremailstring = getResources().getString(R.string.differentpassword);
            } else {
                erroremailstring = getResources().getString(R.string.nopassword);
            }
            passError.setText(erroremailstring);
            passError.setVisibility(View.VISIBLE);
        }
            //not obligated data
        final EditText firstname = findViewById(R.id.firstnameText);
        final String firstnameString;
        if(firstname.getText().toString().isEmpty()){
            firstnameString = "No first name";
        } else {
            firstnameString = firstname.getText().toString();
        }
        final EditText lastname = findViewById(R.id.lastnameText);
        final String lastnameString;
        if(lastname.getText().toString().isEmpty()){
            lastnameString = "No last name";
        } else {
            lastnameString = lastname.getText().toString();
        }
        final RadioGroup gender = findViewById(R.id.genderButton);
        int genderid = gender.getCheckedRadioButtonId();
        final char genderChar;
        if(genderid >= 0){
            RadioButton genderButton = findViewById(genderid);
             genderChar = genderButton.getText().toString().charAt(0);
        } else {
            genderChar = 'X';
        }

        if(succesful){
            RequestQueue mQueue = Volley.newRequestQueue(this);
            String url = "https://studev.groept.be/api/a18_sd209/APP_givePassword/"+emailadress;
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject Password = response.getJSONObject(0);
                                final TextView emailError = findViewById(R.id.emailError);
                                String erroremailstring = getResources().getString(R.string.emailexists);
                                emailError.setText(erroremailstring);
                                emailError.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //account doesn't exist
                                makeAccount(emailadress, pass1String, firstnameString, lastnameString, genderChar);
                                resetAll();
                            }
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    TextView ErrorView = findViewById(R.id.errorMessage);
                    String unknownerror = getResources().getString(R.string.nointernet);
                    ErrorView.setText(unknownerror);
                }
            });
            mQueue.add(request);
        }
    }

    //    https://studev.groept.be/api/a18_sd209/APP_addNewAccount/email/pass/firstn/lastn/gen
    private void makeAccount(String email, String password, String firstname, String lastname, char gender) {

        RequestQueue mQueue = Volley.newRequestQueue(this);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentdate = dateFormat.format(date);
        String url = "https://studev.groept.be/api/a18_sd209/APP_addNewAccount/"+email.toLowerCase()+
                "/"+password+"/"+firstname+"/"+lastname+"/"+gender+"/"+currentdate;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        goToLogin();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                TextView ErrorView = findViewById(R.id.errorMessage);
                String unknownerror = getResources().getString(R.string.nointernet);
                ErrorView.setText(unknownerror);
            }
        });
        mQueue.add(request);
    }

    private void goToLogin(){
        Intent Login = new Intent(this, Login.class);
        startActivity(Login);
    }

    private void resetAll(){
        final EditText email = findViewById(R.id.emailText);
        email.setText("");
        final EditText password = findViewById(R.id.passwordText);
        email.setText("");
        final EditText secondpassword = findViewById(R.id.repeatpasswordText);
        email.setText("");
        final EditText firstname = findViewById(R.id.firstnameText);
        email.setText("");
        final EditText lastname = findViewById(R.id.lastnameText);
        email.setText("");
        final RadioGroup gender = findViewById(R.id.genderButton);
        gender.clearCheck();
    }
}

