package com.example.master.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReConnectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_connect);
    }

    @Override
    protected void onResume(){
        super.onResume();
        new Thread(){
            public void run(){
                try {
                    Thread.sleep(5000);
                    Intent intent = new Intent(getApplication(),ConnectActivity.class);
                    startActivity(intent);
                    finish();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
