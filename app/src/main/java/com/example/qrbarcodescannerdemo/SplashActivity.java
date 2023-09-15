package com.example.qrbarcodescannerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    String total1;
    Button QR, TotalBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.collection("Total").document("costNWeight");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int SPLASH_SCREEN_TIME_OUT=2000;

//
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            //This method is used so that your splash activity
//            //can cover the entire screen.

            setContentView(R.layout.activity_splash);

            QR = findViewById(R.id.qr_btn);
            TotalBtn = findViewById(R.id.total_btn);
            QR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    docRef.update("Tcost", 0);
                    docRef.update("Tweight", 0);
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });

            TotalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TotalModel totalModel = new TotalModel();
//                    //Toast.makeText(getApplicationContext(),totalModel.getTcost(),Toast.LENGTH_SHORT).show();
//                    Log.d("tt", String.valueOf(totalModel.getTcost()));
                    TotalDialog totalDialog = new TotalDialog();
                    totalDialog.show(getSupportFragmentManager(), "example dialog");
                }
            });

            //this will bind your MainActivity.class file with activity_main.

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent i=new Intent(SplashActivity.this,
//                            MainActivity.class);
//                    //Intent is used to switch from one activity to another.
//
//                    startActivity(i);
//                    //invoke the SecondActivity.
//
//                    finish();
//                    //the current activity will get finished.
//                }
//            }, SPLASH_SCREEN_TIME_OUT);

        }


    }







