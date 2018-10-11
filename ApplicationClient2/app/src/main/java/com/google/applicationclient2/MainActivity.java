package com.google.applicationclient2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private String return_msg;
    private EditText mEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
    }
    public void initData(){
        mEt=(EditText)findViewById(R.id.EditText01);
    }
    public void onClickBtn(View v){
        Toast toast = Toast.makeText(getApplicationContext(), mEt.getText().toString(),0);
        toast.show();
        TCPclient tcpTread = new TCPclient(mEt.getText().toString());
        Thread thread = new Thread((Runnable) tcpTread);
        thread.start();
    }
    private class TCPclient implements Runnalble{
        private static final String serverIP="49.166.77.214";
        private static final int serverPort =9000;
        private Socket inetSocket = null;
        private String msg;

        public TCPclient(String _msg){
            this.msg=_msg;
        }

        public void run(){
            try {
                Log.d("TCP", "C:Connecting...");

                inetSocket = new Socket(serverIP, serverPort);

                try {
                    Log.d("TCP", "C:Sending:'" + msg + "'");
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(inetSocket.getOutputStream())), true);
                    out.println(msg);

                    BufferedReader in = new BufferedReader(new InputStreamReader(inetSocket.getInputStream()));
                    return_msg = in.readLine();

                    Log.d("TCP", "C:Server send to me this message-->" + return_msg);
                } catch (Exception e) {
                    Log.e("TCP", "C:Error1", e);
                } finally {
                    inetSocket.close();
                }
            }
            catch(Exception e)
            {
                Log.e("TCP","C:Error2",e);
            }
        }
    }
}
