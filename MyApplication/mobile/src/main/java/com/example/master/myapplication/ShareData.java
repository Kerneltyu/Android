package com.example.master.myapplication;

import android.app.Application;
import android.widget.TextView;

/**
 * Created by okudo on 2017/01/26.
 */

public class ShareData extends Application {
    private double Temperature;
    private double Humidity;

    public void setTempAndHumi(double temperature,double humidity){
        Temperature = temperature;
        Humidity = humidity;
    }

    public double getTemp(){
        return Temperature;
    }

    public double getHumi(){
        return Humidity;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

}
