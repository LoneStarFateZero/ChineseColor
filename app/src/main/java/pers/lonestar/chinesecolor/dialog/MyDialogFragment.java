package pers.lonestar.chinesecolor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import pers.lonestar.chinesecolor.R;

/**
 * 自定义实现对话框
 */
public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button approve;
    private Button cancel;
    private AlertDialog dialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_approve:
                //强制转换为自定义的ConfirmListener接口，调用其中的方法，清空全部收藏的颜色
                ConfirmListener confirmListener = (ConfirmListener) getActivity();
                if (confirmListener != null) {
                    confirmListener.confirmDelete();
                }
                //关闭对话框
                dialog.cancel();
                break;
            case R.id.dialog_cancel:
                dialog.cancel();
                break;
            default:
        }
    }

    //自定义接口
    public interface ConfirmListener {
        void confirmDelete();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //原底部设置透明，避免原矩形白色背景在圆角布局下露出
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete, null);

        approve = view.findViewById(R.id.dialog_approve);
        cancel = view.findViewById(R.id.dialog_cancel);
        approve.setOnClickListener(this);
        cancel.setOnClickListener(this);

        //设置背景颜色
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("data", Context.MODE_PRIVATE);
        String actionBarColor = sharedPreferences.getString("ActionBarColor", "#3A3A3A");
        //采用SRC_IN模式，与对话框底部圆角白色背景取交集绘制，显示上层颜色
        view.getBackground().setColorFilter(Color.parseColor(actionBarColor), PorterDuff.Mode.SRC_IN);
        //采用MULTIPLY模式，与按钮底部圆角灰色背景取交集叠加绘制，颜色较原色更深，凸显出按钮
        approve.getBackground().setColorFilter(Color.parseColor(actionBarColor), PorterDuff.Mode.MULTIPLY);
        cancel.getBackground().setColorFilter(Color.parseColor(actionBarColor), PorterDuff.Mode.MULTIPLY);


        builder.setView(view);
        dialog = builder.create();
        return dialog;
    }
}
