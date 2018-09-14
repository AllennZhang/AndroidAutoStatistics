package com.yt.statistics.statplugin.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yt.statistics.statplugin.R;

/**
 * Created by youri on 2018/2/27.
 */

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

    private Context mContext ;
    public RecAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recy_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText("Hello World   "+position+"!");
        holder.tvTitle.setTag("Hello World   "+position+"!");
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private  TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
             tvTitle = itemView.findViewById(R.id.tvTitle);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Toast.makeText(mContext,tvTitle.getTag().toString(),Toast.LENGTH_SHORT).show();
                 }
             });
        }
    }
}
