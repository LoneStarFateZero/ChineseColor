package pers.lonestar.chinesecolor.activities;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import org.litepal.LitePal;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pers.lonestar.chinesecolor.R;
import pers.lonestar.chinesecolor.adapter.FavoriteColorAdapter;
import pers.lonestar.chinesecolor.colorclass.Color;

public class FavoriteActivity extends AppCompatActivity {
    private static final String TAG = "FavoriteActivity";
    private List<Color> colorList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initColor();
        recyclerView = findViewById(R.id.favorite_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FavoriteColorAdapter favoriteColorAdapter = new FavoriteColorAdapter(colorList, this);
        recyclerView.setAdapter(favoriteColorAdapter);
        initWindowColor();
    }

    private void initWindowColor() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String actionBarColor = sharedPreferences.getString("ActionBarColor", "#3A3A3A");
        String statusBarColor = sharedPreferences.getString("StatusBarColor", "#3A3A3A");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor(actionBarColor)));
            getSupportActionBar().setTitle("收藏");
            getWindow().setStatusBarColor(android.graphics.Color.parseColor(statusBarColor));
        }
    }

    public void initColor() {
        colorList = LitePal.findAll(Color.class);
    }
}
