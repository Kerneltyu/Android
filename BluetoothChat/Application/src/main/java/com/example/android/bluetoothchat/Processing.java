package com.example.android.bluetoothchat;

import io.inventit.processing.android.serial.Serial;
import processing.core.PApplet;
import processing.core.PImage;

public class Processing extends PApplet {
    //Context mContext;
    Serial myPort;
    String filename = "gdtk_bar.jpg";
    byte[] photo={};
    Boolean readData = false;
    PImage captureImage;

    public void settings(){
        size(640,480);
    }

    public void setup(){
        //println(Serial.list(this));
        //myPort = new Serial( this, "COM4", 4200 ); //Serial.list()[3]は環境に合わせて変更すること。
        background(0);
    }
    //画像データの取り込みはBluetoothChatFragmentで
    public void draw(){

        try {
            //saveBytes(filename,photo);
            //photo = new byte[0];
            captureImage = loadImage(filename);
            image(captureImage,0,0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void keyPressed()
    {
        if( photo.length > 0 ) {
            readData = false;
            print( "Writing to disk " );
            print( photo.length );
            println( " bytes ..." );
            saveBytes( filename, photo );
            println( "DONE!" );
            photo = new byte[0];

            captureImage = loadImage(filename);
            image(captureImage, 0, 0);
        }
        else {
            readData = true;
            myPort.write(0);
            println( "Waiting for data ..." );
        }
    }
}
