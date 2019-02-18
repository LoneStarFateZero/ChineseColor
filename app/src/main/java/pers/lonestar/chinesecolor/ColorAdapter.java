package pers.lonestar.chinesecolor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    private List<Color> colorList;
    private MainActivity mainActivity;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //子项点击监听事件
        holder.colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Color color = colorList.get(position);

                //将复制的颜色的十六进制值复制到剪贴板
                ClipboardManager clipboardManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, color.getHex());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(v.getContext(), "已复制" + color.getHex(), Toast.LENGTH_SHORT).show();

                changWindowColor(color.getHex(), position);
            }
        });
        return holder;
    }

    //子项滚到屏幕中时会得到执行
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Color color = colorList.get(position);
        holder.name.setText(color.getName());
        holder.pinyin.setText(color.getPinyin());
        holder.hex.setText(color.getHex());
        holder.red.setText("R: " + color.getRed());
        holder.green.setText("G: " + color.getGreen());
        holder.blue.setText("B: " + color.getBlue());
        holder.colorView.setBackground(new ColorDrawable(android.graphics.Color.parseColor(color.getHex())));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View colorView;
        private TextView name;
        private TextView pinyin;
        private TextView hex;
        private TextView red;
        private TextView green;
        private TextView blue;

        public ViewHolder(@NonNull View itemView) {
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

    public ColorAdapter(List<Color> colorList, MainActivity mainActivity) {
        this.colorList = colorList;
        this.mainActivity = mainActivity;
    }

    private void changWindowColor(String hex, int position) {
        if (mainActivity.getSupportActionBar() != null) {
            SharedPreferences sharedPreferences = mainActivity.getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ActionBarColor", hex);
            editor.putString("StatusBarColor", hex);
            editor.putInt("position", position);
            editor.apply();
            mainActivity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor(hex)));
            mainActivity.getWindow().setStatusBarColor(android.graphics.Color.parseColor(hex));
        }
    }
}
