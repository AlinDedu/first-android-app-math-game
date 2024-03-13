package com.alindedu.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity {
    TextView score, life, time, question;
    EditText answer;
    Button submit, nextQuestion;
    Random random = new Random();
    int number1, number2;
    int userAnswer;
    int realAnswer;
    int userScore = 0;
    int userLife = 3;
    String mode = "";

    CountDownTimer timer;
    private static final long START_TIMER_IN_MILIS = 30000;
    Boolean timer_running;
    long time_left_in_milis = START_TIMER_IN_MILIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        score = findViewById(R.id.textViewScore);
        life = findViewById(R.id.textViewLife);
        time = findViewById(R.id.textViewTime);
        question = findViewById(R.id.textViewQuestion);
        answer = findViewById(R.id.editTextAnswer);
        submit = findViewById(R.id.buttonSubmit);
        nextQuestion = findViewById(R.id.buttonNext);
        nextQuestion.setEnabled(false);

        gameContinue();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.getText().toString().isEmpty()) {
                    Toast.makeText(Game.this, "Please enter an answer", Toast.LENGTH_SHORT).show();
                    return;
                }

                userAnswer = Integer.valueOf(answer.getText().toString());
                pauseTimer();
                submit.setEnabled(false);
                if(userAnswer == realAnswer) {
                    userScore = userScore + 10;
                    score.setText(""+userScore);
                    question.setTextColor(getResources().getColor(R.color.green));
                    question.setText("Congratulations, Your answer is true!");
                } else {
                    userLife = userLife - 1;
                    life.setText(""+userLife);
                    question.setTextColor(getResources().getColor(R.color.red));
                    question.setText("Sorry, Your answer is false...");
                }

                nextQuestion.setEnabled(true);
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setText("");
                resetTimer();

                if(userLife <= 0) {
                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Game.this, Result.class);
                    intent.putExtra("score", userScore);
                    startActivity(intent);
                    finish();
                } else {
                    gameContinue();
                }
            }
        });
    }

    public void gameContinue() {
        String questionText = "";


        if(mode.equals("addition")) {
            number1 = random.nextInt(100);
            number2 = random.nextInt(100);
            realAnswer = number1 + number2;
            questionText = number1 + " + " + number2;
        } else if (mode.equals("substraction")) {
            number1 = random.nextInt(81) + 20;
            number2 = random.nextInt(number1);
            realAnswer = number1 - number2;
            questionText = number1 + " - " + number2;
        } else if (mode.equals("multiplication")) {
            number1 = random.nextInt(15);
            number2 = random.nextInt(15);
            realAnswer = number1 * number2;
            questionText = number1 + " * " + number2;
        } else if (mode.equals("division")) {
            realAnswer = random.nextInt(16) + 1;
            number2 = random.nextInt(16) + 1;
            number1 = realAnswer * number2;
            questionText = number1 + " ÷ " + number2;
        }

        nextQuestion.setEnabled(false);
        submit.setEnabled(true);

        question.setTextColor(getResources().getColor(R.color.white));
        question.setText(questionText);
        startTimer();
    }

    public void startTimer() {
        timer = new CountDownTimer(time_left_in_milis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_left_in_milis = millisUntilFinished;
                updateText();
            }

            @Override
            public void onFinish() {
                timer_running = false;
                pauseTimer();
                resetTimer();
                updateText();
                userLife = userLife - 1;
                question.setTextColor(getResources().getColor(R.color.red));
                question.setText("Sorry! Time is up!");
            }
        }.start();

        timer_running = true;
    }

    public void updateText() {
        int second = (int)(time_left_in_milis / 1000) % 60;
        String time_left = String.format(Locale.getDefault(), "%02d", second);
        time.setText(time_left);
    }

    public void pauseTimer() {
        timer.cancel();
        timer_running = false;
    }

    public void resetTimer() {
        time_left_in_milis = START_TIMER_IN_MILIS;
        updateText();
    }
}