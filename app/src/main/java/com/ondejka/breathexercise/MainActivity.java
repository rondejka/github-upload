package com.ondejka.breathexercise;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView timerTexView, breathdownTextView, stopTextView;
    TextView time01TexView, time02TexView, time03TexView, time04TexView;
    TextView time01ShadowTextView, time02ShadowTextView, time03ShadowTextView, time04ShadowTextView;
    Boolean counterIsActive = false;
    Button goButton;
    CountDownTimer countDownTimer;
    CountDownTimer breathsDownTimer;
    SharedPreferences mPrefs;
    private static final String FILE_NAME = "example.txt";
    public Parameters parameters;
    int[] results = {0, 0, 0, 0, 0};
    int phase;
    boolean timerExit = false;
    int breathsLeft;
    Handler handler;
    Runnable run;
    TextToSpeech textToSpeech;
    int timerSeconds;
    int round;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        String mString;

        parameters = new Parameters(40, 1, 120, 15);
        Log.i("Main 01:", "OK");

        mPrefs = getSharedPreferences("breaths", 0);
        mString = mPrefs.getString("breaths", "40");
        parameters.setBreathsInStartingPhase(Integer.parseInt(mString));
        Log.i("Param Breaths Main:", Integer.toString(parameters.getBreathsInStartingPhase()));

        mPrefs = getSharedPreferences("speed", 0);
        mString = mPrefs.getString("speed", "2");
        parameters.setBreathingSpeedInStartingPhase(Integer.parseInt(mString));
        Log.i("Param Speed Main:", Integer.toString(parameters.getBreathingSpeedInStartingPhase()));

        mPrefs = getSharedPreferences("timeBreathing", 0);
        mString = mPrefs.getString("timeBreathing", "120");
        parameters.setSecondsOfStartingPhase(Integer.parseInt(mString));
        Log.i("Param Seconds1 Main:", String.valueOf(parameters.getBreathsInStartingPhase()));

        mPrefs = getSharedPreferences("timeRelaxing", 0);
        mString = mPrefs.getString("timeRelaxing", "15");
        parameters.setSecondsOfRelaxingPhase(Integer.parseInt(mString));
        Log.i("Param Seconds2 Main:", String.valueOf(parameters.getSecondsOfRelaxingPhase()));

        goButton = findViewById(R.id.goButton);
        timerTexView = findViewById(R.id.countdownTextView);
        breathdownTextView = findViewById(R.id.breathdownTextView);
        stopTextView = findViewById(R.id.stopTextView);
        time01TexView = findViewById(R.id.time01TextView);
        time02TexView = findViewById(R.id.time02TextView);
        time03TexView = findViewById(R.id.time03TextView);
        time04TexView = findViewById(R.id.time04TextView);
        time01ShadowTextView = findViewById(R.id.time01ShadowTextView);
        time02ShadowTextView = findViewById(R.id.time02ShadowTextView);
        time03ShadowTextView = findViewById(R.id.time03ShadowTextView);
        time04ShadowTextView = findViewById(R.id.time04ShadowTextView);

        phase = 0;
        timerSeconds = 0;
        round = 0;
        for (int i = 0; i <= 4; i++){
            results[i] = 0;
        }
    }

    /** Called when the user taps the Setup button */
    public void setupParameters(View view){
        Intent intent = new Intent(this, DisplaySetupParameters.class);
        intent.putExtra("PARAMETERS", parameters);
        startActivity(intent);
    }

    public void resetTimer(){
        if(phase == 1) {
            Log.i("resetTimer ", "phase=1");

            countDownTimer.cancel();
            breathsDownTimer.cancel();
            counterIsActive = false;
        }else
            if(phase == 2){
            Log.i("resetTimer ", "phase=2");

            timerExit = true;
            processForTimerExit();

            goButton.setVisibility(View.VISIBLE);
            goButton.setText("CANCEL");
            stopTextView.setVisibility(View.INVISIBLE);
            thirdPhase();
        }else
            if(phase == 3){
                Log.i("resetTimer ", "phase=3");

                countDownTimer.cancel();
                breathsDownTimer.cancel();
                counterIsActive = false;
            }

    }

    /** Called when the user taps the START button */
    public void startCountDown (View view){

        if (phase == 1){
            Log.i("Button Pressed", "resetTimer, phase 1");

            resetTimer();
            goButton.setText("START");
            phase = 0;
        } else if(phase == 2){
            Log.i("Button Pressed", "resetTimer, phase 2");

            resetTimer();
        }else if(phase == 3) {
            resetTimer();
            goButton.setText("START");
            phase = 0;
        }
        else if (phase == 0)
        {
            Log.i("Button Pressed", "startCountDown");

            counterIsActive = true;
            goButton.setText("CANCEL");
            round = 0;

            clearTextVievs();
            firstPhase();
        }
        else{
            Log.i("startCountDown", "UNTREATED CASE !!!");
        }
    }

    private void firstPhase(){
        phase = 1;
        round ++;

//        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS){
//                    textToSpeech.setLanguage(Locale.ENGLISH);
//                    textToSpeech.speak("breath in", TextToSpeech.QUEUE_FLUSH, null);
//                }
//            }
//        });
//        Log.i("firstPhase 01", "OK");
//        textToSpeech.speak("breath in", TextToSpeech.QUEUE_FLUSH, null);
        Log.i("firstPhase 02", "OK");



        countDownTimer = new CountDownTimer(parameters.getSecondsOfStartingPhase() * 1000 + 100, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                updateCountdownTextView((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Log.i("Phase 1", "Timer all done");
                MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.a_tone);
                mplayer.start();
                resetTimer();

                secondPhase();
            }
        }.start();

        breathsLeft = parameters.getBreathsInStartingPhase();
        float realSpeed = (float) ((((float) (parameters.getBreathingSpeedInStartingPhase())) / 2) + 2.5);
        int secondsBreathing = (int) (breathsLeft * realSpeed);
        breathsDownTimer = new CountDownTimer(parameters.getSecondsOfStartingPhase() * 1000 + 100, (long) (realSpeed * 1000)) {

            @Override
            public void onTick(long millisUntilFinished) {
//                Log.i("breathsDownTimer", "tick");
                updateBreathdownTextView((int) (breathsLeft));
                breathsLeft--;
                if(breathsLeft>=0){
                    MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.breath_in);
                    mplayer.start();
                }
            }

            @Override
            public void onFinish() {
            }
        }.start();


    }

    @SuppressLint("LongLogTag")
    public void updateCountdownTextView(int secondsLeft){
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);

        String secondString;
        if (seconds < 10) {
            secondString = "0" + Integer.toString(seconds);
        }
        else secondString = Integer.toString(seconds);

        timerTexView.setText(Integer.toString(minutes) + ":" + secondString);

    }

    @SuppressLint("LongLogTag")
    public void updateBreathdownTextView(int breathsLeft){

         breathdownTextView.setText(Integer.toString(breathsLeft));

    }

    public void secondPhase(){
        Log.i("Phase 2", "Started");
        phase = 2;
        timerSeconds = -1;
        timerExit = false;
        goButton.setText("STOP");
        goButton.setVisibility(View.INVISIBLE);
        stopTextView.setVisibility(View.VISIBLE);


        handler = new Handler();
        run = new Runnable() {
            @Override
            public void run() {
//                Log.i("Timer", "A second passed by");
                timerSeconds++;
                updateCountdownTextView((int) timerSeconds);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(run);
    }

    private void processForTimerExit(){
        handler.removeCallbacks(run);

        results[round] = timerSeconds;
        time01TexView.setText(SecondsToTimeString(results[1]));
        if(results[2]>0) time02TexView.setText(SecondsToTimeString(results[2]));
        if(results[3]>0) time03TexView.setText(SecondsToTimeString(results[3]));
        if(results[4]>0) time04TexView.setText(SecondsToTimeString(results[4]));
        time01ShadowTextView.setText(time01TexView.getText());
        time02ShadowTextView.setText(time02TexView.getText());
        time03ShadowTextView.setText(time03TexView.getText());
        time04ShadowTextView.setText(time04TexView.getText());

        Log.i("processForTimerExit02", "OK");

    }

    public String SecondsToTimeString(int secondsTotal){
        int minutes = secondsTotal / 60;
        int seconds = secondsTotal % 60;

        String secondsString;
        if (seconds < 10) {
            secondsString = "0" + String.valueOf(seconds);
        }
        else secondsString = String.valueOf(seconds);

        return String.valueOf(minutes) + ":" + secondsString;
    }

    private void thirdPhase(){
        phase = 3;
        countDownTimer = new CountDownTimer(parameters.getSecondsOfRelaxingPhase() * 1000 + 100, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                updateCountdownTextView((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Log.i("Phase 3", "Timer all done");
                MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.a_tone);
                mplayer.start();
                resetTimer();

                if (round < 4){
                    firstPhase();
                }else {
                    Log.i("Phase 3", "End of game.");
                    timerTexView.setText("");
                    saveScore();
                }

            }
        }.start();
    }

    private void clearTextVievs(){
        time01TexView.setText("");
        time02TexView.setText("");
        time03TexView.setText("");
        time04TexView.setText("");
        time01ShadowTextView.setText("");
        time02ShadowTextView.setText("");
        time03ShadowTextView.setText("");
        time04ShadowTextView.setText("");
        for (int i = 0; i <= 4; i++){
            results[i] = 0;
        }
    }

    /** Called when the user taps the Setup button */
    public void openHistoryScoreScreen(View view){
        Intent intent = new Intent(this, HistoryScore.class);
        intent.putExtra("PARAMETERS", parameters);
        startActivity(intent);

    }

    public void saveScore() {

        String newScoreText = getNewScore();
        String oldScoreText = getOldHistoryScore();
        String scoreText = newScoreText + ";\n" + oldScoreText;
//        String scoreText = newScoreText;
        Log.i("saveScore_01", scoreText);

        FileOutputStream fos = null;
        try {
//            fos = openFileOutput(FILE_NAME, MODE_APPEND);
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(scoreText.getBytes());

//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
            Log.i("Saved to ", getFilesDir() + "/" + FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getOldHistoryScore()  {
        FileInputStream fis = null;
        String oldScoreHistoryText = "";

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            int l = 0;
            while ((l < 4) && ((oldScoreHistoryText = br.readLine()) != null)){
                sb.append(oldScoreHistoryText);
            }
            oldScoreHistoryText = sb.toString();
            Log.i("getOldHistoryScore", oldScoreHistoryText);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return oldScoreHistoryText;
    }

    private String getNewScore(){
        Date date = new Date();
        DateFormat dateFormat1 = new SimpleDateFormat("d. MMM, yyyy");
        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        String dateStr = dateFormat1.format(date);
        String timeStr = dateFormat2.format(date);

        String newScoreText = String.valueOf("Robo");
        newScoreText = newScoreText  + ";" + String.valueOf("WHex1");
        newScoreText = newScoreText  + ";" + String.valueOf(dateStr);
        newScoreText = newScoreText  + ";" + String.valueOf(timeStr);
        newScoreText = newScoreText  + ";" + String.valueOf(results[1])  + ";" + String.valueOf(results[2]) + ";" + String.valueOf(results[3]) + ";" + String.valueOf(results[4]);
        newScoreText = newScoreText  + ";" + String.valueOf(maxTime());
        newScoreText = newScoreText  + ";" + String.valueOf(avgTime());
        Log.i("NewScore", newScoreText);

        return newScoreText;
    }

    private int maxTime(){
        int maxTime = results[1];
        for (int i = 2; i <= 4; i++){
            if (maxTime < results[i]){
                maxTime = results[i];
            }
        }
        return maxTime;
    }

    private float avgTime(){
        float avgTime = 0;
        for (int i = 1; i <= 4; i++){
            avgTime = avgTime + results[i];
            }
        avgTime = avgTime / 4;
        return avgTime;
    }
}