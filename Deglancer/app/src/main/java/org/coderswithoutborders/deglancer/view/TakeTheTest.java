package org.coderswithoutborders.deglancer.view;

import android.app.Activity;
import android.os.Bundle;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.coderswithoutborders.deglancer.R;

/**
 * Created by Lauripal on 27.6.2016.
 */
public class TakeTheTest extends Activity {

    int questionId=0;
    int numOfQuestions;
    String[] questions;
    String[] answers;
    TextView tvPreTestActualQuestion;
    RadioButton radioPreTestOne, radioPreTestTwo, radioPreTestThree, radioPreTestFour;
    Button Next, Previous, Submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pretest);

        String [] questions;
        String currentQ;
        questions = getResources().getStringArray(R.array.PreTestQuestions);
        int numOfQuestions = questions.length;

        tvPreTestActualQuestion = (TextView)findViewById(R.id.tvPreTestActualQuestion);
        radioPreTestOne = (RadioButton)findViewById(R.id.radioPreTestOne);
        radioPreTestTwo = (RadioButton)findViewById(R.id.radioPreTestTwo);
        radioPreTestThree = (RadioButton)findViewById(R.id.radioPreTestThree);
        radioPreTestFour = (RadioButton)findViewById(R.id.radioPreTestFour);
        Next = (Button)findViewById(R.id.btnNextQuestion);
        Previous = (Button)findViewById(R.id.btnPrevQuestion);
        Submit = (Button)findViewById(R.id.btnSubmitResponse);

        setQuestionView();

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup grp=(RadioGroup)findViewById(R.id.radiogroupAnswers);
                RadioButton answer=(RadioButton)findViewById(grp.getCheckedRadioButtonId());
                Log.d("Question " + Integer.toString(questionId), " - Answer " + String.valueOf(answer.getText()));
                answers[questionId]= String.valueOf(answer.getText());
                questionId++;
                if(questionId<numOfQuestions){
                    setQuestionView();
                }else {
                    Intent intent = new Intent(TakeTheTest.this, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("answers", answers); //Your score
                    intent.putExtras(b); //Put your score to your next Intent
                    startActivity(intent);
                    finish();
                }
            }
        });

        /*
        stageLength = lengthOfStage[stage - 1];
        String[] introTexts = getResources().getStringArray((R.array.IntroTexts));
        str = introTexts[stage - 1];

        ((TextView)findViewById(R.id.tvIntro)).setText(str);
        remainingTimeText = getResources().getString(R.string.RemainingTimeText);
        int remainingTime;
        remainingTime = stageLength - day + 1;
        dayString = Integer.toString(remainingTime);
        ((TextView)findViewById(R.id.tvStageRemainingTime)).setText(dayString + " " + remainingTimeText);
         */

    }

    private void setQuestionView() {
        if (questionId == 0) {
            Previous.setEnabled(false);
            Next.setEnabled(true);
        } else
        if (questionId < numOfQuestions) {
            Previous.setEnabled(true);
            Next.setEnabled(true);
        } else
        if (questionId == numOfQuestions) {
            Next.setText(getResources().getString(R.string.btnSubmitResponseText));
            Previous.setEnabled(true);
            Next.setEnabled(true);
        }
        tvPreTestActualQuestion.setText(questions[questionId]);
    }
}
