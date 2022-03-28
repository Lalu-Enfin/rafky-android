package com.rafiky.connect.utils;

import android.os.Handler;

import androidx.recyclerview.widget.RecyclerView;

public class RecycleAutoScroll {

    public static void autoScroll(RecyclerView recyclerView , RecyclerView.Adapter adpter){
        final int speedScroll = 1;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if(count == adpter.getItemCount())
                    count =0;
                if(count < adpter.getItemCount()){
                    recyclerView.smoothScrollToPosition(++count);
                    handler.postDelayed(this,speedScroll);
                }
            }
        };
        handler.postDelayed(runnable,speedScroll);
    }


}
