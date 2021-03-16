package com.example.tfai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    FrameLayout fragmentHolder;
    HomeFragment home;
    NavigationView navdrawer;
    ImageView menuicon;
    DrawerLayout drawerLayout;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try (Interpreter interpreter = new Interpreter(loadModelFile(this))) {
            Log.d("LOAD", "LOADED SUCCESSFULLY");
            Intent serviceIntent = new Intent(this, RecorderService.class);
            serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
            serviceIntent.setAction("ACTION_START_FOREGROUND_SERVICE");
            startService(serviceIntent);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("LOAD", e.getMessage());
        }





        fragmentHolder = findViewById(R.id.frag_content);
        navdrawer = findViewById(R.id.nav_drawer);
        menuicon = findViewById(R.id.menu_icon);
        drawerLayout = findViewById(R.id.drawer_layout);
        home = new HomeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frag_content, home);
        ft.addToBackStack(null);
        ft.commit();






        //OnclickListeners
        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navdrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Log.d("Navbar", item.getItemId() + "Click");
                switch (item.getItemId()){

                    case R.id.item_contact:
                        FragmentTransaction fxt= getSupportFragmentManager().beginTransaction();
                        fxt.replace(R.id.frag_content,new ContactFragment());
                        fxt.commit();
                        break;
                    case R.id.item_help:
                        FragmentTransaction fyt= getSupportFragmentManager().beginTransaction();
                        fyt.replace(R.id.frag_content,new HelpFragment());
                        fyt.commit();
                        break;
                    default:
                        FragmentTransaction fzt= getSupportFragmentManager().beginTransaction();
                        fzt.replace(R.id.frag_content,new HomeFragment());
                        fzt.commit();


                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("final_mod_opt.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }










}