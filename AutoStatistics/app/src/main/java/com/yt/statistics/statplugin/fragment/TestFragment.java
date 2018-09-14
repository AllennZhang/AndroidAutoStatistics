package com.yt.statistics.statplugin.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hipac.codeless.core.TraceService;
import com.yt.statistics.statplugin.R;
import com.yt.statistics.statplugin.view.SelectePhotoDialog;

/**
 * Created by youri on 2018/2/27.
 */

public class TestFragment extends Fragment  {

    private RecyclerView mRecyler;
    private SelectePhotoDialog dialog;
    private String attr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_test,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyler = view.findViewById(R.id.recyclerView);

        view.findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new SelectePhotoDialog(getActivity());
                    dialog.setOnSelectPhotoListener(new SelectePhotoDialog.SelectPhotoListener() {
                        @Override
                        public void takePhoto() {

                        }

                        @Override
                        public void pickedPhoto() {

                        }
                    });

                    DialogInterface dialogInterface = new DialogInterface() {
                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void dismiss() {

                        }
                    };
                }
                dialog.show();
            }
        });

        initView();
        Log.e("Test", "fragment onViewCreated  "+ TraceService.getCurrentPage());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Test", "fragment onActivityCreated  "+TraceService.getCurrentPage());
    }

    private void initView() {
        mRecyler.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecAdapter adapter = new RecAdapter(getActivity());
        mRecyler.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void writeAttribute(String label) {
        attr = label;
    }



}
