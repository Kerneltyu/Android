package com.example.android.bluetoothchat;

import processing.core.PApplet;

/**
 * Created by okudo on 2016/12/15.
 */

public class Sketch extends PApplet {
    public void settings(){size(displayWidth,displayHeight);}
    public void setup(){}
    public void draw(){
        if(mousePressed){
            ellipse(mouseX,mouseY,50,50);
        }
    }
}
