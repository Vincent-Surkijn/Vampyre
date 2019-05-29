package vincentsurkijn.softdev.kuleuven.vampyre;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.AndroidAuthenticator;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Support extends AppCompatActivity {

    private TextView QuestionOne;
    private TextView AnswerOne;
    private TextView QuestionTwo;
    private TextView AnswerTwo;
    private TextView QuestionThree;
    private TextView AnswerThree;
    private TextView QuestionFour;
    private TextView AnswerFour;
    private TextView QuestionFive;
    private TextView AnswerFive;
    private TextView QuestionSix;
    private TextView AnswerSix;
    private TextView QuestionSeven;
    private TextView AnswerSeven;
    private TextView QuestionEight;
    private TextView AnswerEight;
    private TextView QuestionNine;
    private TextView AnswerNine;
    private TextView QuestionTen;
    private TextView AnswerTen;

    private EditText QuestionSuggest;
    private TextView QuestionSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        //set all Textviews, EditViews to variables
        QuestionOne = findViewById(R.id.questionOne);
        AnswerOne = findViewById(R.id.answerOne);
        QuestionTwo = findViewById(R.id.questionTwo);
        AnswerTwo = findViewById(R.id.answerTwo);
        QuestionThree = findViewById(R.id.questionThree);
        AnswerThree = findViewById(R.id.answerThree);
        QuestionFour = findViewById(R.id.questionFour);
        AnswerFour = findViewById(R.id.answerFour);
        QuestionFive = findViewById(R.id.questionFive);
        AnswerFive = findViewById(R.id.answerFive);
        QuestionSix = findViewById(R.id.questionSix);
        AnswerSix = findViewById(R.id.answerSix);
        QuestionSeven = findViewById(R.id.questionSeven);
        AnswerSeven = findViewById(R.id.answerSeven);
        QuestionEight = findViewById(R.id.questionEight);
        AnswerEight = findViewById(R.id.answerEight);
        QuestionNine = findViewById(R.id.questionNine);
        AnswerNine = findViewById(R.id.answerNine);
        QuestionTen = findViewById(R.id.questionTen);
        AnswerTen = findViewById(R.id.answerTen);
        QuestionSuggest = findViewById(R.id.Question_suggest);
        QuestionSendMessage = findViewById(R.id.questionSendMessage);

        //Update Questions and Answers
        getQuestionsAndAnswers();
    }

    public void getQuestionsAndAnswers(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getAllQuestionsAndAnswers";
        url = url.replaceAll(" ","_");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject QandA1 = response.getJSONObject(0);
                            QuestionOne.setText(QandA1.getString("Question").replaceAll("_", " "));
                            AnswerOne.setText(QandA1.getString("Answer").replaceAll("_", " "));

                            JSONObject QandA2 = response.getJSONObject(1);
                            QuestionTwo.setText(QandA2.getString("Question").replaceAll("_", " "));
                            AnswerTwo.setText(QandA2.getString("Answer").replaceAll("_", " "));

                            JSONObject QandA3 = response.getJSONObject(2);
                            QuestionThree.setText(QandA3.getString("Question").replaceAll("_", " "));
                            AnswerThree.setText(QandA3.getString("Answer").replaceAll("_", " "));

                            JSONObject QandA4 = response.getJSONObject(3);
                            QuestionFour.setText(QandA4.getString("Question").replaceAll("_", " "));
                            AnswerFour.setText(QandA4.getString("Answer").replaceAll("_", " "));

                            JSONObject QandA5 = response.getJSONObject(4);
                            QuestionFive.setText(QandA5.getString("Question").replaceAll("_", " "));
                            AnswerFive.setText(QandA5.getString("Answer").replaceAll("_", " "));

                            JSONObject QandA6 = response.getJSONObject(5);
                            QuestionSix.setText(QandA6.getString("Question").replaceAll("_", " "));
                            AnswerSix.setText(QandA6.getString("Answer").replaceAll("_", " "));

                            JSONObject QandA7 = response.getJSONObject(6);
                            QuestionSeven.setText(QandA7.getString("Question").replaceAll("_", " "));
                            AnswerSeven.setText(QandA7.getString("Answer").replaceAll("_", " "));

                            JSONObject QandA8 = response.getJSONObject(7);
                            QuestionEight.setText(QandA8.getString("Question").replaceAll("_", " "));
                            AnswerEight.setText(QandA8.getString("Answer").replaceAll("_", " "));

                            JSONObject QandA9 = response.getJSONObject(8);
                            QuestionNine.setText(QandA9.getString("Question").replaceAll("_", " "));
                            AnswerNine.setText(QandA9.getString("Answer").replaceAll("_", " "));

                            JSONObject QandA10 = response.getJSONObject(9);
                            QuestionTen.setText(QandA10.getString("Question").replaceAll("_", " "));
                            AnswerTen.setText(QandA10.getString("Answer").replaceAll("_", " "));

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

    public void sendQuestion(View v){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_suggestNewQuestion/" + QuestionSuggest.getText();
        url = url.replaceAll(" ","_");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONArray response) {
                        QuestionSendMessage.setText("Question is send to our database! We will try to answer it as soon as possible!");
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        mQueue.add(request);
        QuestionSuggest.getText().clear();
    }


}
