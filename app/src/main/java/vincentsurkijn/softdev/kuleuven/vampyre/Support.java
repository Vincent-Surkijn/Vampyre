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
        QuestionSuggest = findViewById(R.id.Question_suggest);
        QuestionSendMessage = findViewById(R.id.questionSendMessage);

        //Update Questions and Answers
        getQuestionsAndAnswers();
    }

    public void getQuestionsAndAnswers(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_getAllQuestionsAndAnswers";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject QandA1 = response.getJSONObject(0);
                            QuestionOne.setText(QandA1.getString("Question"));
                            AnswerOne.setText(QandA1.getString("Answer"));

                            JSONObject QandA2 = response.getJSONObject(1);
                            QuestionTwo.setText(QandA2.getString("Question"));
                            AnswerTwo.setText(QandA2.getString("Answer"));

                            JSONObject QandA3 = response.getJSONObject(2);
                            QuestionThree.setText(QandA3.getString("Question"));
                            AnswerThree.setText(QandA3.getString("Answer"));

                            JSONObject QandA4 = response.getJSONObject(3);
                            QuestionFour.setText(QandA4.getString("Question"));
                            AnswerFour.setText(QandA4.getString("Answer"));
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
