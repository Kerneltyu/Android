package com.example.okudo.tcp_guest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    TextView tv;
    EditText et;
    Button btn;
    ConnectThread connectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText)findViewById(R.id.editText);
        et.setText("");
        btn = (Button)findViewById(R.id.button);
        btn.setText("Send");
        tv = (TextView)findViewById(R.id.textView2);
        tv.setText("Receiving Area");

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what ==0x123){
                    tv.setText("Received:"+msg.obj.toString());
                }
            }
        };

        connectThread = new ConnectThread(handler);
        new Thread(connectThread).start();

        btn.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
               try{
                   Message msg = new Message();
                   msg.what = 0x345;
                   msg.obj = et.getText().toString();
                   connectThread.revHandler.sendMessage(msg);
                   et.setText("");
               }catch(Exception e){
                   e.printStackTrace();
               }
           }
        });
    }
}
