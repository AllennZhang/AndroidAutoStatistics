package com.yt.statistics.statplugin.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yt.statistics.statplugin.R;

/**
 *
 * 选择图片的dialog
 */
public class SelectePhotoDialog extends Dialog implements View.OnClickListener {


    private SelectPhotoListener mListener;
    private Context mContext ;

    /**
     * @param context
     *
     */
    public SelectePhotoDialog(Context context) {
        super(context, R.style.add_dialog);
        mContext =context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        Window window=getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        Log.e("Manage",manager.getDefaultDisplay().getWidth()+"");
        window.setAttributes(lp);
        setContentView(R.layout.dialog_selecte_photo_popupwindow);
        findViewById(R.id.tv_dialog_cancel).setOnClickListener(this);
        findViewById(R.id.tv_dialog_take).setOnClickListener(this);
        findViewById(R.id.tv_dialog_select).setOnClickListener(this);

        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_dialog_take) {//启动相机
            if (mListener != null) {
                mListener.takePhoto();
            }
            dismiss();

        } else if (i == R.id.tv_dialog_select) {//启动相册
            if (mListener != null) {
                mListener.pickedPhoto();
            }
            dismiss();

        } else if (i == R.id.tv_dialog_cancel) {
            dismiss();

        } else {
        }

    }

    public interface SelectPhotoListener{
        void takePhoto();
        void pickedPhoto();

    }

    public void setOnSelectPhotoListener(SelectPhotoListener listener){
        this.mListener=listener;
    }
}
