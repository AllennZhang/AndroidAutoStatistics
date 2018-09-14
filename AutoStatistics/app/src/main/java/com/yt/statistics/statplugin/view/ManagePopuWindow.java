package com.yt.statistics.statplugin.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yt.statistics.statplugin.R;

/**
 * Created by youri on 2017/6/12.
 */

public class ManagePopuWindow extends BasePopupWindow {

    private OnMangePopuListener mListener;
    private boolean isRecommand ;
    private ToggleButton toggleButton;

    public ManagePopuWindow(Context context, boolean toggleOn) {
        super(context);
        init(context,toggleOn);
    }

    private void init(Context context, boolean toggleOn) {
        final View view = LayoutInflater.from(context).inflate(R.layout.popu_manage_goods, null);
        TextView iconArrow = (TextView) view.findViewById(R.id.iv_arrow);
        toggleButton = (ToggleButton) view.findViewById(R.id.iv_toggleButton);
        LinearLayout lltModifyPrice = (LinearLayout) view.findViewById(R.id.llt_modify_price);
        LinearLayout lltModifyProfit = (LinearLayout) view.findViewById(R.id.llt_modify_profit);
        toggleButton.setChecked(toggleOn);
        View.OnClickListener listener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.iv_arrow:
                        popuDismiss();
                        break;
//                    case R.id.iv_toggleButton:
//                        //切换开关
//                        isRecommand = !isRecommand;
//                        break;
                    case R.id.llt_modify_price:
                        if (mListener !=null){
                            mListener.onModifyPrice();
                        }
                        popuDismiss();
                        break;
                    case R.id.llt_modify_profit:
                        if (mListener !=null){
                            mListener.onModifyProfit();
                        }
                        popuDismiss();
                        break;
                    default:
                        break;
                }
            }
        };

        iconArrow.setOnClickListener(listener);
        //toggleButton.setOnClickListener(listener);
        lltModifyPrice.setOnClickListener(listener);
        lltModifyProfit.setOnClickListener(listener);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRecommand = isChecked;
                if (mListener !=null){
                    mListener.booleanRecommand(isChecked);
                }
            }
        });

        this.setContentView(view);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        // ColorDrawable bg = new ColorDrawable(0xb0000000);
        // managePopu.setBackgroundDrawable(bg);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setAnimationStyle(R.style.bottom_popwindow_anim_style);

    }


    public interface OnMangePopuListener{
        void onModifyPrice();
        void onModifyProfit();
        void booleanRecommand(boolean recommand);
    }

    public void setnMangePopuListener(OnMangePopuListener l){
        this.mListener = l;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void popuDismiss(){
        dismiss();
    }

    public void setIsRecommand(boolean isRecommand){
        this.isRecommand = isRecommand;
        if (toggleButton !=null){
            toggleButton.setChecked(isRecommand);
        }
    }

    public boolean IsRecommand(){
        return isRecommand;
    }

}
