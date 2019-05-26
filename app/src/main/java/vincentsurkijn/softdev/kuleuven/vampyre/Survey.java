package vincentsurkijn.softdev.kuleuven.vampyre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;

public class Survey extends AppCompatActivity {

    private int counter;
    private boolean male;
    private boolean hasDonated;
    private TextView question;
    private TextView message;
    private TextView ageMessage;
    private Button yes;
    private Button no;
    private EditText age;
    private CheckBox Blood;
    private CheckBox Plasma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        //Connect Buttons, TextView and Edittext to variables
        question = findViewById(R.id.Question);
        message = findViewById(R.id.Message);
        ageMessage = findViewById(R.id.explanation);
        yes = findViewById(R.id.Yes_button);
        no = findViewById(R.id.No_button);
        age = findViewById(R.id.age);
        Blood = findViewById(R.id.BloodChoice);
        Plasma = findViewById(R.id.PlasmaChoice);

        //Set first question
        question.setText("Have you already donated at the red cross?");

        //Set counter to 0
        counter = 0;

        //Yes and No can't be clicked before age is confirmed
        yes.setClickable(false);
        no.setClickable(false);

    }

    public void confirmAge(View v){
        if(isInteger(age.getText().toString())){
            int num = Integer.parseInt(age.getText().toString());
            if(num<18) {
                ageMessage.setText("You have to be at least 18 to donate blood or plasma");
            }
            else if(num<130){
                ageMessage.setText("You're old enough to donate blood or plasma. You can now start the survey.");
                if (checkIfCheckboxChecked()){
                    yes.setClickable(true);
                    no.setClickable(true);
                }
                else {
                    message.setText("Now choose if you want to donate blood or plasma");
                }
            }
            else {
                ageMessage.setText("You can't be that old???");
            }
        }
        else{
            // not an integer!
            message.setText("Please enter a valid value");
        }
    }

    public boolean checkIfCheckboxChecked() {
        boolean checked = false;
        if (Blood.isChecked() || Plasma.isChecked()){
            checked = true;
        }
        return checked;
    }

    private boolean isInteger(String s) {
        int i = 0;
        boolean integer = true;
        boolean cont = true;
        if (s.length()!=0 ){
            while (i<s.length()&& cont){
                if(!Character.isDigit(s.charAt(i))){
                    integer = false;
                    cont = false;
                }
                i++;
            }
        }
        else{
            integer = false;
        }

        return integer;
    }

    public void onPlasmaChecked(View v){
        Blood.setChecked(false);
    }

    public void onBloodChecked(View v){
        Plasma.setChecked(false);
    }

    public void goBackToAgenda(View v){
        Intent intent = new Intent(this, Agenda.class);
        startActivity(intent);
    }

    public void onYesClicked(View v){
        ageMessage.setText("");
        if (counter == 0){
            //Already donated? Yes
            hasDonated = true;
            question.setText("Are you male?");
        }
        if (counter == 1 & hasDonated){
            //Are you male? Yes
            male = true;
            message.setText("You are able to donate!");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter == 1 & !hasDonated){
            //Male? yes (who hasn't donated)
            male = true;
            question.setText("Have you been outside Europe in the last 6 months?");
        }
        if(counter == 2 & !male){
            //Female who has been pregnant in last 6 months
            message.setText("You are not able to donate. You have to wait at least 6 months after being pregnant to donate.");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter == 2 & male){
            message.setText("You are not able to donate. You must wait at least 28 days after your return after each trip outside Europe or to the Canary Islands before you can again give blood or plasma.\n" +
                    "\n" +
                    "Depending on where you have been traveling, the waiting period can be up to 6 months. Anyone staying in a malaria-endemic area in South and Central America, Africa or Asia must wait 6 months to give blood regardless of the length of stay.");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter == 3 &!male){
            //Been outside Europe in last 6 months
            message.setText("You are not able to donate. You must wait at least 28 days after your return after each trip outside Europe or to the Canary Islands before you can again give blood or plasma.\n" +
                    "\n" +
                    "Depending on where you have been traveling, the waiting period can be up to 6 months. Anyone staying in a malaria-endemic area in South and Central America, Africa or Asia must wait 6 months to give blood regardless of the length of stay.");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter == 3 & male){
            //Has had new sexual partner
            message.setText("You have to wait at least 4 months after you had a new sexual partner.");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter  == 4 & !male){
            //Has had new sexual partner
            message.setText("You have to wait at least 4 months after you had a new sexual partner.");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter  == 4 & male){
            //Has had sex with a man
            message.setText("You have to wait at least 12 months after you had a sex with another man.");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter  == 5){
            //Has been sick
            message.setText("You have to wait at least 14 days after you have been cured");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter  == 6){
            //Takes medication or has heart disease
            message.setText("It's possible you can't donate. Ask your doctor to be sure.");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter  == 7){
            //Has been in contact with blood
            message.setText("\n" +
                    "286/5000\n" +
                    "We ask you to wait at least 4 months after the incident before offering you back for donation. By a bite accident we only mean these bite accidents originating from humans or ape. If the bite comes from a pet, you can resubmit two weeks after healing.");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if (counter  == 8){
            //Has ever used drugs
            message.setText("If you have ever injected drugs, you will no longer be eligible for donation.\n" +
                    "If you have sniffed drugs, we will ask you to wait four months.\n" +
                    "The use of soft drugs is no reason to delay if you are not under the influence of the donation.");
            yes.setClickable(false);
            no.setClickable(false);
        }
        //Update counter after every click
        counter++;
    }

    public void onNoClicked(View v){
        ageMessage.setText("");
        if (counter == 0){
            //Already donated? No
            hasDonated = false;
            question.setText("Are you male?");
        }
        if(counter == 1){
            //Male? No
            male = false;
            question.setText("Have you been pregnant in the last 6 months?");
        }
        if(counter == 2 & hasDonated){
            //Pregnant in last 6 months? No
            message.setText("You are able to donate!");
            yes.setClickable(false);
            no.setClickable(false);
        }
        if(counter == 2 & !hasDonated){
            //Not donated yet and hasn't been pregnant
            question.setText("Have you been outside Europe in the last 6 months?");
        }
        if (counter == 2 & male){
            //Hasn't been outside Europe
            question.setText("Have you had a new sexual partner in the last 4 months?");
        }
        if (counter == 3 & male){
            //Hasn't had new sexual partner
            question.setText("Have you had sex with another man in the last 12 months?");
        }
        if (counter == 3 &!male){
            //Hasn't been outside europe
            question.setText("Have you had a new sexual partner in the last 4 months?");
        }
        if (counter == 4 ){
            //Hasn't had a new sexual partner
            question.setText("Have you been sick recently?");
        }
        if (counter == 5){
            //Hasn't been sick
            question.setText("Do you have a heart disease or do you take medication for psioriasis or acné?");
        }
        if (counter == 6){
            //Doesn't take medication or heart disease
            question.setText("Have you been in contact with blood in the form of a leak, cut, splash or bite accident in the past 4 months?");
        }
        if (counter == 7){
            //Hasn't been in contact with blood
            question.setText("Did you ever use drugs?");
        }
        if (counter == 8){
            //Hasn't used drugs
            message.setText("Thank you for completing this donor self-test. If you didn't get a reason to delay, you seem to be eligible for a donation.\n" +
                    "\n"  + "Your result has been saved. You can now go back to what you were doing. " + "\n" +
                    "Note: for every donation there is a medical questionnaire and medical examination at a doctor. This donor self-test does not replace this check. There is a chance that during the interview there is another reason why you may be temporarily or permanently refused for donation."
            );
            yes.setClickable(false);
            no.setClickable(false);
            filledOutForm();
        }
        //Update counter after every click
        counter++;
    }

    public void filledOutForm() {
        //Notice database that user has filled out form
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_setFilledOutForm/"+Login.user;
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
