package org.coderswithoutborders.deglancer.pretest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.Gravity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.coderswithoutborders.deglancer.R;
import org.coderswithoutborders.deglancer.pretest.PreTestActivity;
import org.coderswithoutborders.deglancer.interactor.IPreTestInteractor;
import org.coderswithoutborders.deglancer.view.MainActivity;

import javax.inject.Inject;


/**
 * Created by Lauripal on 27.6.2016.
 */
public class PreTestActivity extends AppCompatActivity implements IPreTestView {

    @Inject
    IPreTestPresenter mPresenter;

    TextView tvPreTestActualQuestion, tvPreTestQuestionTitle;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;

    /*
    private IPreTestInteractor preTestInteractor;
    public PreTestActivity(IPreTestInteractor preTestInteractor) {
        this.mPreTestInteractor = preTestInteractor;
    }
    */

    private static final String TAG = "Deglancer.PreTestActivity";
    int questionId = 0;
    int numOfQuestions = 10;
    String[] questions = new String[10];
    String[] answers = new String[10];
    String questionsText;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pretest);

        questions = getResources().getStringArray(R.array.PreTestQuestions);
        questionsText = getResources().getString(R.string.tvPreTestQuestionText);
        numOfQuestions = questions.length;

        tvPreTestActualQuestion = (TextView) findViewById(R.id.tvPreTestActualQuestion);
        tvPreTestActualQuestion.setText(questions[questionId]);

        tvPreTestQuestionTitle = (TextView) findViewById(R.id.tvPreTestQuestion);
        String questionTextFull = questionsText + " " + Integer.toString(questionId + 1) + "/10";
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
            Log.d(TAG, "Question " + Integer.toString(questionId+1) + " - Answer : 1");
            answers[questionId] = "1";
        } else if (v.getId() == R.id.btnPreTestTwo) {
            Log.d(TAG, "Question " + Integer.toString(questionId+1) + " - Answer : 2");
            answers[questionId] = "2";
        } else if (v.getId() == R.id.btnPreTestThree) {
            answers[questionId] = "3";
            Log.d(TAG, "Question " + Integer.toString(questionId+1) + " - Answer : 3");
        } else if (v.getId() == R.id.btnPreTestFour) {
            answers[questionId] = "4";
            Log.d(TAG, "Question " + Integer.toString(questionId+1) + " - Answer : 4");
        }
        questionId++;
        int ans = 0;
        if (questionId < numOfQuestions) {
            moveToQuestion(questionId);
        } else {
            showToast();
            for (ans = 0; ans < answers.length; ans++) {
                String message = "Question was " + Integer.toString(ans+1) + " - answer was: " + answers[ans];
                Log.d(TAG, message);
            }
            Log.d(TAG, "Uploading results now");
            mPresenter.submitPreTestResults(answers[0], answers[1], answers[2], answers[3], answers[4], answers[5], answers[6], answers[7], answers[8], answers[9]);
            finish();
        }
    };

    @Override
    public void finishActivity() {

    }

    @Override
    public void moveToMainView() {
        // Intent intent = new Intent(PreTestActivity.this, MainActivity.class);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void moveToQuestion(int questionNumber) {
        tvPreTestActualQuestion.setText(questions[questionNumber]);
        tvPreTestQuestionTitle.setText("Question " + Integer.toString(questionNumber + 1) + "/10");
    }

    public void showToast() {
        Toast toast = Toast.makeText(this, R.string.strPreTestThankYou, 3);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /*
    @Override
    protected void onAttachedToWindow() {
        // super.onAttachedToWindow();

        if (mPresenter != null) {
            // mPresenter.setView(this);
            mPresenter.onAttached();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // super.onDetachedFromWindow();

        if (mPresenter != null) {
            mPresenter.onDetached();
            // mPresenter.clearView();
        }
    }
    */

}
