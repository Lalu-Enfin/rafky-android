package com.rafiky.connect.ui.Event;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.rafiky.connect.R;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeadphoneDialog  extends Dialog {

    private Context context ;
    private HeadPhoneListner headPhoneListner ;

    public HeadphoneDialog(@NonNull Context context,HeadPhoneListner headPhoneListnerParams) {
        super(context);
        this.context = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.headPhoneListner = headPhoneListnerParams;
        setContentView(R.layout.headphone_dialog_layout);
        ButterKnife.bind(this,getWindow().getDecorView());
        this.setCancelable(false);
        Objects.requireNonNull(getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }
    @OnClick(R.id.okButton)
    public void dismissDialog(){
        headPhoneListner.dialogOkClicked();
        dismiss();
    }

    @OnClick(R.id.ib_close_button)
    public void closeDialog(){
        dismiss();
    }


    public interface HeadPhoneListner{
        public void dialogOkClicked();
    }

}
