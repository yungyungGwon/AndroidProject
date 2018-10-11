package com.google.applicationclient3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Socket socket;
    BufferedReader in;
    PrintWriter out;
    EditText input;
    Button button;
    TextView output;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        button = (Button) findViewById(R.id.button);
        output = (TextView) findViewById(R.id.output);

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String data = input.getText().toString();
                Log.w("NETWORK", " " + data);
                if (data != null) {
                    out.println(data);
                }
            }
        });

        Thread worker = new Thread() {
            public void run() {
                try {
                    socket = new Socket("192.168.219.112", 9090);
                    out = new PrintWriter(socket.getOutputStream(), true);                                                                                                                      //전송한다.
                    in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    while (true) {
                        data = in.readLine();
                        output.post(new Runnable() {
                            public void run() {
                                output.setText(data);
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        };
        worker.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}