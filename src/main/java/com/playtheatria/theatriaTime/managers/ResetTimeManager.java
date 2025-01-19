package com.playtheatria.theatriaTime.managers;

import com.playtheatria.theatriaTime.database.ResetTime;

public class ResetTimeManager {
    private ResetTime resetTime;

    public ResetTimeManager(ResetTime resetTime) {
        this.resetTime = resetTime;
    }

    public ResetTime getResetTime() {
        return resetTime;
    }

    public void setResetTime(ResetTime resetTime) {
        this.resetTime = resetTime;
    }
}
