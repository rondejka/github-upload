package com.ondejka.breathexercise;

import java.io.Serializable;

public class UserScore implements Serializable {
    public String userName;
    public int date;
    public int time;
    public int time1;
    public int time2;
    public int time3;
    public int time4;
    public int timeMax;
    public int timeAvg;

    public UserScore() {
    }

    public UserScore(String userName, int date, int time, int time1, int time2, int time3, int time4, int timeMax, int timeAvg) {
        this.userName = userName;
        this.date = date;
        this.time = time;
        this.time1 = time1;
        this.time2 = time2;
        this.time3 = time3;
        this.time4 = time4;
        this.timeMax = timeMax;
        this.timeAvg = timeAvg;
    }
}
