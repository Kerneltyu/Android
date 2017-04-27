package com.example.master.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/** MainActivity
 * 温度・湿度を表示してそのデータから測定値の状況を判定し表示する
 * */
public class MainActivity extends AppCompatActivity {

    private final static String TAG = "Main";       //name:Temperature Management System
    private static RelativeLayout relativeLayout;
    private Socket socket;
    private InputStream in;                         //入力ストリーム
    private OutputStream out;                       //出力ストリーム
    public Handler mHandler;
    /** 終了ボタン */
    private Button endButton;
    /** Threadの状態を表す */
    private boolean isRunning;
    /** 温度変数 */
    private double temperature;
    private double humidity;
    /** ステータス. */
    private TextView alert;
    private TextView temperatureView;
    private TextView humidityView;
    /** 通信関係の変数を引き継ぐ */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        endButton = (Button) findViewById(R.id.end_button);
        alert = (TextView) findViewById(R.id.Alert_text);
        temperatureView = (TextView) findViewById(R.id.textView3);
        humidityView = (TextView) findViewById(R.id.textView7);

        relativeLayout = (RelativeLayout)findViewById(R.id.activity_main);
        /** 終了ボタンの処理 */
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 測定結果が確認終了したら前画面に遷移する */
                finish();
            }
        });

        new Thread(){
            public void run(){
                ShareData shareData = ((ShareData)getApplicationContext());
                temperature = shareData.getTemp();
                humidity = shareData.getHumi();
                changeView();
                while(true){
                    try {
                        Thread.sleep(1000);
                        temperature = shareData.getTemp();
                        humidity = shareData.getHumi();
                        mHandler.post(new Runnable(){
                            public void run(){
                                changeView();
                            }
                        });
                        //changeView();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //Thread thread = new MyThread();
        //thread.start();
    }
/*
    class MyThread extends Thread{
        public void run(){
            while(true) {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                }
                mHandler.post(new Runnable() {
                    public void run() {
                        relativeLayout.invalidate();
                    }
                });
            }
        }
    }
*/
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();

        isRunning = false;
        try{
            /** mSocket.close(); */
        }
        catch(Exception e){}
    }

    private void changeView(){
        Log.d(TAG,"changeView");
        temperatureView.setText(String.valueOf(temperature));
        humidityView.setText(String.valueOf(humidity));
        //不快指数(Discomfort Index)の計算
        double DI = 0.81 * temperature + 0.01 * humidity * (0.99 * temperature - 14.3) + 46.3;

        // 不快指数が75以上で背景を赤くする(高温状態)
        if (DI >= 75) {
            alert.setText("高温注意");
            alert.setBackgroundColor(Color.RED);
        }
        // 10℃以下で背景を青くする(低温状態)
        else if(temperature <= 10){
            alert.setText("低温注意");
            alert.setBackgroundColor(Color.BLUE);
        }
        //それ以外で背景を黒くする(適温状態)
        else{
            alert.setText("適温状態");
            alert.setBackgroundColor(Color.GREEN);
        }
    }

}
