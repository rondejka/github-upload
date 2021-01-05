package com.ondejka.breathexercise;

import java.io.Serializable;

public class Parameters implements Serializable {
    private int breathsInStartingPhase;
    private int breathingSpeedInStartingPhase;
    private int secondsOfStartingPhase;
    private int secondsOfRelaxingPhase;

    public Parameters(int breaths, int breathingSpeed, int secondsStarting, int secondsRelaxing) {
        this.breathsInStartingPhase = breaths;
        this.breathingSpeedInStartingPhase = breathingSpeed;
        this.secondsOfStartingPhase = secondsStarting;
        this.secondsOfRelaxingPhase = secondsRelaxing;
    }

    public int getBreathsInStartingPhase() {
        return breathsInStartingPhase;
    }

    public void setBreathsInStartingPhase(int p_breathsInStartingPhase) {
        breathsInStartingPhase = p_breathsInStartingPhase;
    }

    public int getBreathingSpeedInStartingPhase() {
        return breathingSpeedInStartingPhase;
    }

    public void setBreathingSpeedInStartingPhase(int p_breathingSpeedsInStartingPhase) {
        breathingSpeedInStartingPhase = p_breathingSpeedsInStartingPhase;
    }

    public long getSecondsOfStartingPhase() {
        return secondsOfStartingPhase;
    }

    public void setSecondsOfStartingPhase(int p_secondsOfStartingPhase) {
        secondsOfStartingPhase = p_secondsOfStartingPhase;
    }

    public long getSecondsOfRelaxingPhase() {
        return secondsOfRelaxingPhase;
    }

    public void setSecondsOfRelaxingPhase(int p_secondsOfRelaxingPhase) {
        secondsOfRelaxingPhase = p_secondsOfRelaxingPhase;
    }

}
