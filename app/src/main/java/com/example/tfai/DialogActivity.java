package com.example.tfai;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {
    DBHelper db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activitt);
        db=new DBHelper(this,"ContactList");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                blinkFlash();
                sendAlertSMS();
                Uri number = Uri.parse("tel:1091");
                Intent callIntent = new Intent(Intent.ACTION_CALL, number);
                startActivity(callIntent);
            }
        },4000);

    }

    private void blinkFlash()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String myString = "010101";
        long blinkDelay = 500; //Delay in ms
        for (int i = 0; i < myString.length(); i++) {
            if (myString.charAt(i) == '0') {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, true);
                } catch (CameraAccessException e) {

                }
            } else {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, false);
                } catch (CameraAccessException e) {
                }
            }
            try {
                Thread.sleep(blinkDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void sendAlertSMS(){
        String helpMessage="Hey,I am not feeling safe here. Do you mind calling me? Please respond ASAP.";
        ArrayList<String> contacts=db.getAllContacts();
        SmsManager smsManager = SmsManager.getDefault();
        for(String cont:contacts){
            try {
                smsManager.sendTextMessage(cont, null, helpMessage, null, null);
            } catch (Exception ex) {
                Toast.makeText(this, "Error sending message to"+cont, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
