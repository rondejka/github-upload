package com.ondejka.breathexercise;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class HistoryScore extends AppCompatActivity {
    TextView date1TextView, time1TextView, time11TextView, time21TextView, time31TextView, time41TextView;
    TextView timeMax1TextView, timeAvg1TextView;
    String[][] history = {{"","","","","","","","","",""},{"","","","","","","","","",""}};
    private static final String FILE_NAME = "example.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_score);

        date1TextView = findViewById(R.id.date1TextView);
        time1TextView = findViewById(R.id.time1TextView);
        time11TextView = findViewById(R.id.time11TextView);
        time21TextView = findViewById(R.id.time21TextView);
        time31TextView = findViewById(R.id.time31TextView);
        time41TextView = findViewById(R.id.time41TextView);
        timeMax1TextView = findViewById(R.id.timeMax1TextView);
        timeAvg1TextView = findViewById(R.id.timeAvg1TextView);

        load();
        showData();
    }

    public void load(){
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            int l = 0;
            while ((l < 4) && ((text = br.readLine()) != null)){
                sb.append(text).append("\n");
            }
            text = sb.toString();

            int j = 0;
            for (int k = 0; k < 2; k++) {
                for (int i = 0; ((i < 10) && (j < text.length())); i++) {
                    int start = j;
                    while ((j < text.length()) && (!text.substring(j, j + 1).equals(";"))) {
                        Log.i("load 01 ", text.substring(j, j + 1));
                        j++;
                    }
                    history[k][i] = text.substring(start, j);
                    Log.i("load 02 ", history[k][i]);
                    j++;
                }
            }
            Log.i("load 03 ", "OK");

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
    }

    private void showData(){
        date1TextView.setText(history[0][2] + " at " + history[0][3]);
//        time1TextView.setText(history[0][3]);

        time11TextView.setText(SecondsToTimeString(Integer.parseInt(history[0][4])));
        time21TextView.setText( SecondsToTimeString(Integer.parseInt(history[0][5])));
        time31TextView.setText(SecondsToTimeString(Integer.parseInt(history[0][6])));
        time41TextView.setText(SecondsToTimeString(Integer.parseInt(history[0][7])));
        timeMax1TextView.setText("Max: " + SecondsToTimeString(Integer.parseInt(history[0][8])));
        timeAvg1TextView.setText("Avg: " + SecondsFloatToTimeString(Float.parseFloat(history[0][9])));
    }

    private String SecondsToTimeString(int secondsTotal){
        int minutes = secondsTotal / 60;
        int seconds = secondsTotal % 60;

        String secondsString;
        if (seconds < 10) {
            secondsString = "0" + String.valueOf(seconds);
        }
        else secondsString = String.valueOf(seconds);

        return String.valueOf(minutes) + ":" + secondsString;
    }

    @SuppressLint("LongLogTag")
    private String SecondsFloatToTimeString(Float secondsTotal){
        String timeString = "";
        String secondsString = String.valueOf(secondsTotal);

        int pos = secondsString.indexOf(".");
        if (pos != -1) {
            timeString =  SecondsToTimeString(Integer.parseInt(secondsString.substring(0, pos))) + secondsString.substring(pos);
        } else {
            timeString = SecondsToTimeString(Integer.parseInt(secondsString));
        }

        return timeString;
    }


}