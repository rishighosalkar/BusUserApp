package com.example.bususerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bususerapp.Activities.LostFoundMainActivity;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    public static TextView textView;
    Bundle extras;
    public static SharedPreferences sharedPreferences;
    public static String key = "1";
    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        textView = (TextView) findViewById(R.id.logout);
        //Toast.makeText(this, textView.getText(), Toast.LENGTH_SHORT).show();
        extras = getIntent().getExtras();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();

        if (sharedPreferences!= null) {// to avoid the NullPointerException
            isLoggedIn = sharedPreferences.getBoolean(key, false);
        }
        Log.i("", String.valueOf(isLoggedIn));
        if(isLoggedIn)
        {
            textView.setText("Logout");
            //sharedPreferences.edit().putBoolean(key, false).commit();
            Toast.makeText(this, "You are logged in", Toast.LENGTH_SHORT).show();
        }

        else{
            textView.setText("Login");
            Toast.makeText(this, "You are logged out", Toast.LENGTH_SHORT).show();
        }
    }

    public void ClickMenu(View view)
    {   //open drawer
        openDrawer(drawerLayout);
    }

    public void openDrawer(DrawerLayout drawerLayout) {
        //open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);

    }

    public void ClickLogo(View view)
    {
        closeDrawer(drawerLayout);
    }

    public void closeDrawer(DrawerLayout drawerLayout) {
        //close drawer layout
        //check condition
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            //drawer is open
            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }
    public void ClickDashboard(View view){
        if(textView.getText() == "Login")
            redirectActivity(this, LoginActivity.class);
    }

    public void ClickSchedule(View view)
    {
        redirectActivity(this, ScheduleActivity.class);
    }

    public void ClickAboutUs(View view){
        //redirectActivity(this, NavigationDrawer.class);
    }

    public void ClickLostAndFound(View view){
        if(!isLoggedIn)
        {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }
        redirectActivity(this, LostFoundMainActivity.class);
    }

    public void ClickLogout(View view){
        if(textView.getText() == "Login") {
            redirectActivity(this, LoginActivity.class);
            //sharedPreferences.edit().putBoolean(key, false).commit();
            //textView.setText("Logout");
        }
        else
            logout(this);
    }

    public static void logout(Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //set tittle
        builder.setTitle("Logout");

        builder.setMessage("Are you sure you want to logout");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
                Intent i = new Intent(activity, MainActivity.class);
                sharedPreferences.edit().putBoolean(key, false).commit();
                //i.putExtra(key, false);
                activity.startActivity(i);
                //sharedPreferences.edit().putBoolean(key, false).commit();
                //System.exit(0);
            }
        });

        //Negative No button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}