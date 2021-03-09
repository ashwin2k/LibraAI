package com.example.tfai;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class RecorderService extends Service {

    private static final String CHANNEL_ID = "555";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotification();

        return START_NOT_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    void createNotification(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("Test")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        startForeground(1, notification);
        recordAudio();
    }
    AudioRecord microphone;
    short[] buffer = new short[22050];
    public void recordAudio(){
        float chunkDuration= (float) 0.5;
        int sampleRate=44100;
        int chunkSamples= (int) (chunkDuration*sampleRate);
        int feedSamples=sampleRate*10;
        int minBufferSize = chunkSamples;

        microphone = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO,  AudioFormat.ENCODING_PCM_16BIT, chunkSamples*2);
        microphone.startRecording();
        Log.d("Mic", microphone.getBufferSizeInFrames()+"");
        Intent dialogIntent = new Intent(getApplicationContext(), DialogActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    //                        Thread.sleep(5);
                    Log.d("TAG", "run: ");

                    int readSize = microphone.read(buffer, 0, buffer.length);
                        Python py=Python.getInstance();
                        PyObject pyobj=py.getModule("test");
                        PyObject oj=pyobj.callAttr("processData",buffer);

                        if(oj!=null)
                            if(oj.toInt() == 1){
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RecorderService.this, "Initializing safety measures", Toast.LENGTH_LONG).show();
//                                        Dialog d=new Dialog(getApplicationContext());
//                                        d.setContentView(R.layout.safe_action_card);
//                                        TextView loading_txt=d.findViewById(R.id.loading_text);
//                                        d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                                        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                                        d.show();
//                                        d.setCancelable(false);
                                        Intent dialogIntent = new Intent(getApplicationContext(), DialogActivity.class);
                                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(dialogIntent);


                                    }
                                });
                            }

                }
            }
        }).start();




    }
   


}
