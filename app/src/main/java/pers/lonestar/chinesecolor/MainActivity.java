package pers.lonestar.chinesecolor;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private List<Color> colorList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initColor();
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ColorAdapter colorAdapter = new ColorAdapter(colorList, this);
        recyclerView.setAdapter(colorAdapter);
        initWindowColor();
    }

    public void initWindowColor() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String actionBarColor = sharedPreferences.getString("ActionBarColor", "#3A3A3A");
        String statusBarColor = sharedPreferences.getString("StatusBarColor", "#3A3A3A");
        int position = sharedPreferences.getInt("position", 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor(actionBarColor)));
            getWindow().setStatusBarColor(android.graphics.Color.parseColor(statusBarColor));
        }
        recyclerView.scrollToPosition(position);
    }

    public void initColor() {
        Gson gson = new Gson();
        try (InputStream inputStream = this.getAssets().open("colors.json")
        ) {
            List<jsonColor> jsonColorList = gson.fromJson(new InputStreamReader(inputStream), new TypeToken<List<jsonColor>>() {
            }.getType());
            for (jsonColor jsonColor : jsonColorList) {
                Color color = new Color(jsonColor.getName(), jsonColor.getPinyin(), jsonColor.getHex().toUpperCase());
                colorList.add(color);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
