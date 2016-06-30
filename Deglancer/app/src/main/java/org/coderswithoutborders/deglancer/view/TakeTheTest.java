package org.coderswithoutborders.deglancer.view;

import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.ListActivity;
import android.widget.Toast;

import org.coderswithoutborders.deglancer.R;

/**
 * Created by Lauripal on 27.6.2016.
 */
public class TakeTheTest extends Activity {

    private static final String TAG = "Deglancer.TakeTheTest";
    int questionId=0;
    int numOfQuestions=10;
    String[] questions = new String[10];
    String[] answers = new String[10];
    String questionsText;
    TextView tvPreTestActualQuestion, tvPreTestQuestionTitle;
    Button btn;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pretest);

        questions = getResources().getStringArray(R.array.PreTestQuestions);
        questionsText = getResources().getString(R.string.tvPreTestQuestionText);
        numOfQuestions = questions.length;

        tvPreTestActualQuestion = (TextView)findViewById(R.id.tvPreTestActualQuestion);
        tvPreTestActualQuestion.setText(questions[questionId]);

        tvPreTestQuestionTitle = (TextView)findViewById(R.id.tvPreTestQuestion);
        String questionTextFull = questionsText + " " + Integer.toString(questionId+1) + "/10";
        tvPreTestQuestionTitle.setText(questionTextFull);

        btnAnswer1 = (Button) findViewById(R.id.btnPreTestOne);
        btnAnswer1.setOnClickListener(buttonClickListener);

        btnAnswer2 = (Button) findViewById(R.id.btnPreTestTwo);
        btnAnswer2.setOnClickListener(buttonClickListener);

        btnAnswer3 = (Button) findViewById(R.id.btnPreTestThree);
        btnAnswer3.setOnClickListener(buttonClickListener);

        btnAnswer4 = (Button) findViewById(R.id.btnPreTestFour);
        btnAnswer4.setOnClickListener(buttonClickListener);

    }

    View.OnClickListener buttonClickListener = v -> {
        if (v.getId() == R.id.btnPreTestOne) {
            Log.d("Question " + Integer.toString(questionId), " - Answer : 1");
            answers[questionId]= "1";
        } else if (v.getId() == R.id.btnPreTestTwo) {
            Log.d("Question " + Integer.toString(questionId), " - Answer : 2");
            answers[questionId]= "2";
        } else if (v.getId() == R.id.btnPreTestThree) {
            answers[questionId]= "3";
            Log.d("Question " + Integer.toString(questionId), " - Answer : 3");
        } else if (v.getId() == R.id.btnPreTestFour) {
            answers[questionId]= "4";
            Log.d("Question " + Integer.toString(questionId), " - Answer : 4");
        }
        questionId++;
        if(questionId<numOfQuestions){
            tvPreTestActualQuestion.setText(questions[questionId]);
            tvPreTestQuestionTitle.setText("Question " + Integer.toString(questionId+1) + "/10");
        }else {
            Toast toast = Toast.makeText(this, R.string.strPreTestThankYou, 3);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            // Here be Firebase sender call
            Intent intent = new Intent(TakeTheTest.this, MainActivity.class);
            // Bundle b = new Bundle();
            // b.putInt("answers", answers); //Your score
            // intent.putExtras(b); //Put your score to your next Intent
            startActivity(intent);
            finish();
        }
    };

}
