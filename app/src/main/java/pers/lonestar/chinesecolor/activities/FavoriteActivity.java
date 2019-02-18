package pers.lonestar.chinesecolor.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.litepal.LitePal;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
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
        int position = sharedPreferences.getInt("FavoritePosition", 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor(actionBarColor)));
            getSupportActionBar().setTitle("收藏");
            getWindow().setStatusBarColor(android.graphics.Color.parseColor(statusBarColor));
        }
        recyclerView.scrollToPosition(position);
    }

    //加载颜色
    //此处加载的库数据为空
    public void initColor() {
        colorList = LitePal.findAll(Color.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("是否删除全部收藏颜色？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecyclerView.Adapter adapter;
                        if ((adapter = recyclerView.getAdapter()) != null) {
                            for (int i = 0; i < colorList.size(); i++) {
                                /*
                                 之前的异常是因为
                                 adapter.notifyItemRemoved(i);
                                  会越界
                                  类似堆栈不断弹出顶部，保持position为0就可以
                                  顶部position始终为0
                                 */
                                adapter.notifyItemRemoved(0);
                            }
                        }
                        for (Color color : colorList) {
                            color.delete();
                        }
                        colorList.clear();
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();
                break;
        }
        return true;
    }
}
