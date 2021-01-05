package com.ondejka.breathexercise;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class DisplaySetupParameters extends AppCompatActivity {
    SharedPreferences mPrefs;
    SeekBar breathsSeekBar;
    TextView breathsTextView;
    TextView timeBreathingTextView;
    SeekBar speedBreathingSeekBar;
    TextView speedBreathingTextView;
    SeekBar secondsBreathHoldingSeekBar;
    TextView secondsBreathHoldingTextView;
    TextView versionTextView;
    boolean updateParametersStarted;
    String updateParametersType;
    Parameters param;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SetupParam_OnCr_010", "OK");

        super.onCreate(savedInstanceState);
        Log.i("SetupParam_OnCr_011", "OK");
        setContentView(R.layout.activity_display_setup_parameters);
        Log.i("SetupParam_OnCr_012", "OK");

        breathsSeekBar = findViewById(R.id.breathsSeekBar);
        Log.i("SetupParam_OnCr_013", "OK");
        breathsTextView = findViewById(R.id.breathsTextView);
        timeBreathingTextView = findViewById(R.id.timeBreathingTextView);
        speedBreathingSeekBar = findViewById(R.id.speedBreathingSeekBar);
        speedBreathingTextView = findViewById(R.id.speedBreathingTextView);
        secondsBreathHoldingSeekBar = findViewById(R.id.secondsBreathHoldingSeekBar);
        secondsBreathHoldingTextView = findViewById(R.id.secondsBreathHoldingTextView);
        versionTextView = findViewById(R.id.versionTextView);
        Log.i("SetupParam_OnCr_020", "OK");

        updateParametersStarted=false;
        updateParametersType = "";

        param = (Parameters)getIntent().getSerializableExtra("PARAMETERS");

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        try {
            String versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),0).versionName;
            versionTextView.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String mString = "";
        breathsSeekBar.setMax(60);
        speedBreathingSeekBar.setMax(2);
        secondsBreathHoldingSeekBar.setMax(20);

        breathsSeekBar.setProgress((int) param.getBreathsInStartingPhase());
        breathsTextView.setText(Integer.toString(param.getBreathsInStartingPhase()));

        speedBreathingSeekBar.setProgress(param.getBreathingSpeedInStartingPhase());
        speedBreathingTextView.setText(Integer.toString(param.getBreathingSpeedInStartingPhase()));

        timeBreathingTextView.setText(secondsToTimeString((int) param.getSecondsOfStartingPhase()));

        secondsBreathHoldingSeekBar.setProgress((int) param.getSecondsOfRelaxingPhase());
        secondsBreathHoldingTextView.setText(secondsToTimeString((int) param.getSecondsOfRelaxingPhase()));
        Log.i("SetupParam_OnCr_030", "OK");

        breathsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateParametersType = "B";
                updateBreathsTextView(progress);
                updateBreathingTimeTextView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        speedBreathingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSpeedBreathingTextView(progress);

//                updateBreathsTextView(progress);
                updateBreathingTimeTextView_B();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

         secondsBreathHoldingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSecondsBreathHoldingTextView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    @SuppressLint("LongLogTag")
    public void updateBreathsTextView(int breaths){
        updateParametersStarted = true;

        String breathsString = Integer.toString(breaths);
        breathsTextView.setText(breathsString);
        param.setBreathsInStartingPhase(Integer.parseInt(breathsString));

        mPrefs = getSharedPreferences("breaths", 0);
        String mString = mPrefs.getString("breaths", "40");

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("breaths", breathsString).commit();

//        mPrefs = getSharedPreferences("breaths", 0);
//        mString = mPrefs.getString("breaths", "40");

        updateParametersStarted=false;
    }

    // ---- Breaths
    @SuppressLint("LongLogTag")
    public void updateSpeedBreathingTextView(int speed){
        updateParametersStarted = true;

        String speedString = Integer.toString(speed);
        speedBreathingTextView.setText(speedString);
        param.setBreathingSpeedInStartingPhase(Integer.parseInt(speedString));

        mPrefs = getSharedPreferences("speed", 0);
        String mString = mPrefs.getString("speed", "40");

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("speed", speedString).commit();

        updateParametersStarted=false;
    }

    // ---- Breathing Time
    // --------------A------------------
    @SuppressLint("LongLogTag")
    public void updateBreathingTimeTextView(int breaths){

        float realSpeed = (float) ((((float) (param.getBreathingSpeedInStartingPhase())) / 2) + 2.5);
        int secondsBreathing = (int) (breaths * realSpeed);
        Log.i("updateBreathingTimeTextView speed:", Integer.toString(param.getBreathingSpeedInStartingPhase()));
        Log.i("updateBreathingTimeTextView secondsBreathing:", Integer.toString(secondsBreathing));

        timeBreathingTextView.setText(secondsToTimeString(secondsBreathing));
        param.setSecondsOfStartingPhase(secondsBreathing);

        mPrefs = getSharedPreferences("timeBreathing", 0);
        String mString = mPrefs.getString("timeBreathing", "180");

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("timeBreathing", String.valueOf(secondsBreathing)).commit();
    }

    // ---- Breathing Time
    // --------------B------------------
    @SuppressLint("LongLogTag")
    public void updateBreathingTimeTextView_B(){

        int breaths = param.getBreathsInStartingPhase();
        Log.i("updateBreathingTimeTextView_B breaths:", Integer.toString(breaths));
        float realSpeed = (float) ((((float) (param.getBreathingSpeedInStartingPhase())) / 2) + 2.5);
        int secondsBreathing = (int) (breaths * realSpeed);
        Log.i("updateBreathingTimeTextView_B speed:", Integer.toString(param.getBreathingSpeedInStartingPhase()));
        Log.i("updateBreathingTimeTextView_B secondsBreathing:", Integer.toString(secondsBreathing));

        timeBreathingTextView.setText(secondsToTimeString(secondsBreathing));
        param.setSecondsOfStartingPhase(secondsBreathing);

        mPrefs = getSharedPreferences("timeBreathing", 0);
        String mString = mPrefs.getString("timeBreathing", "180");

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("timeBreathing", String.valueOf(secondsBreathing)).commit();
    }


    @SuppressLint("LongLogTag")
    public void updateSecondsBreathHoldingTextView(int secondsRelaxing){
        updateParametersStarted = true;

        String secondsString = Integer.toString(secondsRelaxing);
        secondsBreathHoldingTextView.setText(secondsString);
        param.setSecondsOfRelaxingPhase(secondsRelaxing);

        mPrefs = getSharedPreferences("timeRelaxing", 0);
        String mString = mPrefs.getString("timeRelaxing", "15");

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("timeRelaxing", secondsString).commit();

        updateParametersStarted=false;
    }

    private String secondsToTimeString(int secondsTotal){
        int minutes = secondsTotal / 60;
        int seconds = secondsTotal % 60;

        String secondsString;
        if (seconds < 10) {
            secondsString = "0" + String.valueOf(seconds);
        }
        else secondsString = String.valueOf(seconds);

        return String.valueOf(minutes) + ":" + secondsString;
    }

    private void savePreferences(String name_key, String value, String defValue){
        mPrefs = getSharedPreferences(name_key, 0);
        String mString = mPrefs.getString(name_key, defValue);

        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString(name_key, value).commit();
    }



}