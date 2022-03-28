package com.rafiky.connect.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.rafiky.connect.R;

import java.util.Objects;

/**
 * Created By Athul on 20-11-2019.
 */
public class NoInternetDialog extends Dialog implements View.OnClickListener {

    private Button retry;
    private RetryListener retryListener;
    private Context mContext;
    private long mLastClickTime;

    public NoInternetDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.no_internet_dialog);
        setCancelable(false);
        Objects.requireNonNull(getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        retry = findViewById(R.id.btnRetry);
        retry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (NetworkUtil.isConnected(mContext)){
                retryListener.onRetry();
                dismiss();
            }
            else
                Toast.makeText(mContext,mContext.getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
    }

    public void setRetryListener(RetryListener listener) {
        this.retryListener = listener;
    }

    public interface RetryListener {
        void onRetry();
    }
}