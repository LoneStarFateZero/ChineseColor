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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import pers.lonestar.chinesecolor.R;

public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    private Button approve;
    private Button cancel;
    private AlertDialog dialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_approve:
                ConfirmListener confirmListener = (ConfirmListener) getActivity();
                if (confirmListener != null) {
                    confirmListener.confirmDelete();
                }
                dialog.cancel();
                break;
            case R.id.dialog_cancel:
                dialog.cancel();
                break;
            default:
        }
    }

    public interface ConfirmListener {
        void confirmDelete();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //原底部设置透明，避免原矩形白色背景在圆角布局下露出
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete, null);
        approve = view.findViewById(R.id.dialog_approve);
        cancel = view.findViewById(R.id.dialog_cancel);
        approve.setOnClickListener(this);
        cancel.setOnClickListener(this);

        //设置背景颜色
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String actionBarColor = sharedPreferences.getString("ActionBarColor", "#3A3A3A");
        view.getBackground().setColorFilter(Color.parseColor(actionBarColor), PorterDuff.Mode.SRC_IN);
        approve.getBackground().setColorFilter(Color.parseColor(actionBarColor), PorterDuff.Mode.MULTIPLY);
        cancel.getBackground().setColorFilter(Color.parseColor(actionBarColor), PorterDuff.Mode.MULTIPLY);


        builder.setView(view);
        dialog = builder.create();
        return dialog;
    }
}
