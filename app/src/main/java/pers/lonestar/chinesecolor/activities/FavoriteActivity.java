package pers.lonestar.chinesecolor.activities;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pers.lonestar.chinesecolor.R;
import pers.lonestar.chinesecolor.adapter.FavoriteColorAdapter;
import pers.lonestar.chinesecolor.colorclass.FavoriteColor;
import pers.lonestar.chinesecolor.dialog.MyDialogFragment;

/**
 * 查看收藏颜色
 */
public class FavoriteActivity extends AppCompatActivity implements MyDialogFragment.ConfirmListener {
    private List<FavoriteColor> colorList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //加载颜色
        initColor();

        //RecyclerView适配
        recyclerView = findViewById(R.id.favorite_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FavoriteColorAdapter favoriteColorAdapter = new FavoriteColorAdapter(colorList, this);
        recyclerView.setAdapter(favoriteColorAdapter);

        //设置ActionBar和StatusBar颜色
        initWindowColor();
    }

    /**
     * 设置ActionBar和StatusBar颜色
     */
    private void initWindowColor() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String actionBarColor = sharedPreferences.getString("ActionBarColor", "#3A3A3A");
        String statusBarColor = sharedPreferences.getString("StatusBarColor", "#3A3A3A");
        int position = sharedPreferences.getInt("FavoritePosition", 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor(actionBarColor)));
            getSupportActionBar().setTitle("收藏");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getWindow().setStatusBarColor(android.graphics.Color.parseColor(statusBarColor));
        }
        recyclerView.scrollToPosition(position);
    }

    /**
     * 加载颜色
     */
    public void initColor() {
        colorList = LitePal.findAll(FavoriteColor.class);
    }

    /**
     * 加载菜单项
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite, menu);
        return true;
    }

    /**
     * 删除菜单，点击弹出确认删除对话框
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                //自定义DialogFragment
                MyDialogFragment dialogFragment = new MyDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "MyDialog");
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


    /**
     * 实现MyDialogFragment中的ConfirmListener接口方法
     * 由确认删除对话框的确认按钮点击后调用
     * 清空颜色列表及对应数据库
     */
    @Override
    public void confirmDelete() {
        if (recyclerView.getAdapter() != null) {
            for (int i = 0; i < colorList.size(); i++) {
                /*之前的异常是因为adapter.notifyItemRemoved(i);
                会越界，类似堆栈不断弹出顶部，
                保持position为0就可以顶部position始终为0*/
                recyclerView.getAdapter().notifyItemRemoved(0);
            }
        }
        //从数据库中删除
        for (FavoriteColor color : colorList) {
            color.delete();
        }
        //清空颜色列表
        colorList.clear();
        Toast.makeText(this, "已移除全部收藏颜色", Toast.LENGTH_SHORT).show();
    }
}
