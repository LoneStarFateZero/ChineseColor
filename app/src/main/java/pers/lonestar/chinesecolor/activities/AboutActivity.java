package pers.lonestar.chinesecolor.activities;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import pers.lonestar.chinesecolor.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initWindowColor();
    }

    private void initWindowColor() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String actionBarColor = sharedPreferences.getString("ActionBarColor", "#3A3A3A");
        String statusBarColor = sharedPreferences.getString("StatusBarColor", "#3A3A3A");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor(actionBarColor)));
            getWindow().setStatusBarColor(android.graphics.Color.parseColor(statusBarColor));
        }
    }
}
