package pers.lonestar.chinesecolor.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pers.lonestar.chinesecolor.R;
import pers.lonestar.chinesecolor.activities.FavoriteActivity;
import pers.lonestar.chinesecolor.colorclass.FavoriteColor;

public class FavoriteColorAdapter extends RecyclerView.Adapter<FavoriteColorAdapter.ViewHolder> {
    private List<FavoriteColor> colorList;
    private FavoriteActivity favoriteActivity;

    //传入颜色列表和收藏活动，用于颜色子项和ActionBar及StatusBar的颜色设置
    public FavoriteColorAdapter(List<FavoriteColor> colorList, FavoriteActivity favoriteActivity) {
        this.colorList = colorList;
        this.favoriteActivity = favoriteActivity;
    }

    //自定义ViewHolder提高效率
    static class ViewHolder extends RecyclerView.ViewHolder {
        private View colorView;
        private TextView name;
        private TextView pinyin;
        private TextView hex;
        private TextView red;
        private TextView green;
        private TextView blue;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView;
            name = itemView.findViewById(R.id.name);
            pinyin = itemView.findViewById(R.id.pinyin);
            hex = itemView.findViewById(R.id.hex);
            red = itemView.findViewById(R.id.red);
            green = itemView.findViewById(R.id.green);
            blue = itemView.findViewById(R.id.blue);
        }
    }

    /**
     * 必须重写方法之一
     * 创建ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //子项短点击监听事件
        holder.colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击产生动画效果
                YoYo.with(Techniques.Bounce).duration(1000).playOn(view);

                //根据position获取对应颜色
                int position = holder.getAdapterPosition();
                FavoriteColor color = colorList.get(position);

                //将复制的颜色的十六进制值复制到剪贴板
                ClipboardManager clipboardManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, color.getHex());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(v.getContext(), "已复制" + color.getHex(), Toast.LENGTH_SHORT).show();

                //改变ActionBar和StatusBar颜色为当前复制的颜色，改善视觉体验
                changWindowColor(color.getName(), color.getHex(), position);
            }
        });
        //子项长点击监听事件
        holder.colorView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                YoYo.with(Techniques.ZoomOut).duration(400).playOn(v);

                int position = holder.getAdapterPosition();
                FavoriteColor color = colorList.get(position);
                //通过LitePal从数据库中删除
                color.delete();
                //从颜色列表中删除
                colorList.remove(color);
                //从滚动布局中删除
                notifyItemRemoved(position);
                Toast.makeText(v.getContext(), "已移除此颜色", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return holder;
    }

    /**
     * 必须重写方法之二
     * 子项滚到屏幕中时会得到执行
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //载入动画
        YoYo.with(Techniques.ZoomIn).duration(400).playOn(holder.colorView);

        //holder保存子项信息
        FavoriteColor color = colorList.get(position);
        holder.name.setText(color.getName());
        holder.pinyin.setText(color.getPinyin());
        holder.hex.setText(color.getHex());
        int colorValue = android.graphics.Color.parseColor(color.getHex());
        //根据颜色十六进制值解析其RGB各项值
        holder.red.setText("R: " + ((colorValue & 0xff0000) >> 16));
        holder.green.setText("G: " + ((colorValue & 0x00ff00) >> 8));
        holder.blue.setText("B: " + (colorValue & 0x0000ff));

        //此处取交集绘制，原底部负责绘制白色圆角矩形（透明背景elevation无效，没有阴影效果）
        //新层负责绘制子项指定的颜色，达到绘制有指定颜色的圆角矩形的布局效果
        holder.colorView.getBackground().setColorFilter(android.graphics.Color.parseColor(color.getHex()), PorterDuff.Mode.SRC_IN);
    }

    /**
     * 必须重写方法之三
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return colorList.size();
    }

    /**
     * 改变收藏活动ActionBar和StatusBar的颜色
     *
     * @param name
     * @param hex
     * @param position
     */
    private void changWindowColor(String name, String hex, int position) {
        if (favoriteActivity.getSupportActionBar() != null) {
            SharedPreferences sharedPreferences = favoriteActivity.getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //保存当前点击复制的颜色和对应的position
            //其中颜色由MainActivity和FavoriteActivity共享同一种颜色，视觉统一
            editor.putString("ActionBarColor", hex);
            editor.putString("StatusBarColor", hex);
            editor.putInt("FavoritePosition", position);
            editor.apply();
            favoriteActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor(hex)));
            favoriteActivity.getWindow().setStatusBarColor(android.graphics.Color.parseColor(hex));
            favoriteActivity.getSupportActionBar().setTitle(name);
        }
    }
}
