package pers.lonestar.chinesecolor.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pers.lonestar.chinesecolor.R;
import pers.lonestar.chinesecolor.adapter.ColorAdapter;
import pers.lonestar.chinesecolor.colorclass.LitePalColor;
import pers.lonestar.chinesecolor.colorclass.jsonColor;

public class MainActivity extends AppCompatActivity {
    //加载颜色列表
    private List<LitePalColor> colorList = new ArrayList<>();
    //滚动展示颜色
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //加载颜色
        initColor();

        //RecyclerView适配
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ColorAdapter colorAdapter = new ColorAdapter(colorList, this);
        recyclerView.setAdapter(colorAdapter);

        //设置ActionBar和StatusBar颜色
        initWindowColor();
    }

    /**
     * 设置ActionBar和StatusBar颜色
     */
    public void initWindowColor() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String actionBarColor = sharedPreferences.getString("ActionBarColor", "#3A3A3A");
        String statusBarColor = sharedPreferences.getString("StatusBarColor", "#3A3A3A");
        int position = sharedPreferences.getInt("MainPosition", 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor(actionBarColor)));
            getWindow().setStatusBarColor(android.graphics.Color.parseColor(statusBarColor));
        }
        recyclerView.scrollToPosition(position);
    }

    /**
     * 加载颜色
     */
    public void initColor() {
        //第一次启动从json文件中加载颜色，将数据通过LitePal保存到数据库中，后续打开从数据库中加载保证打开速度
        colorList = LitePal.findAll(LitePalColor.class);
        //若数据库中无缓存颜色数据，则从json中加载
        if (colorList.isEmpty()) {
            Gson gson = new Gson();
            try (InputStream inputStream = this.getAssets().open("colors.json")
            ) {
                //json文件加载颜色
                List<jsonColor> jsonColorList = gson.fromJson(new InputStreamReader(inputStream), new TypeToken<List<jsonColor>>() {
                }.getType());
                for (jsonColor jsonColor : jsonColorList) {
                    LitePalColor color = new LitePalColor(jsonColor.getName(), jsonColor.getPinyin(), jsonColor.getHex().toUpperCase());
                    //添加到颜色列表
                    colorList.add(color);
                    //通过LitePal保存到数据库中
                    color.save();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载菜单项
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 收藏和关于菜单点击跳转
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
                startActivity(favoriteIntent);
                break;
            case R.id.about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }
        return true;
    }

    /**
     * 每次由不可见转为可见需重新设置窗口颜色
     * 因为在收藏页面中点击得到的颜色需在所有活动中被设置成窗口颜色
     */
    @Override
    protected void onStart() {
        super.onStart();
        initWindowColor();
    }
}
