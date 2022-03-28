package com.rafiky.connect.ui.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.rafiky.connect.R;
import com.rafiky.connect.base.BaseActivity;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;
import com.rafiky.connect.sharedpreference.SharedPreferenceData;
import com.rafiky.connect.ui.Event.EventActivity;
import com.rafiky.connect.utils.AppUtils;
import com.rafiky.connect.utils.Constants;
import com.rafiky.connect.utils.NetworkUtil;
import com.rafiky.connect.utils.NoInternetDialog;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class LoginActivity extends BaseActivity implements LoginContract.MvpView, NoInternetDialog.RetryListener {

    private LoginPresenter loginPresenter;
    private Context mContext;
    private SharedPreferenceData sharedPreferenceData;
    private static String TAG = "LoginActivity";
    private Dialog progressDialog;

    @BindView(R.id.et_name)
    EditText userName_et;

    @BindView(R.id.et_email)
    EditText emailId_et;

    @BindView(R.id.et_meetingid)
    EditText meetingId_et;

    @BindView(R.id.ti_username)
    TextInputLayout textInputUsername;

    @BindView(R.id.ti_email)
    TextInputLayout textInputEmail;

    @BindView(R.id.ti_meetingid)
    TextInputLayout textInputMeeting;

    @BindView(R.id.cons_parent)
    ConstraintLayout parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginPresenter(this, LoginActivity.this);
        mContext = LoginActivity.this;
        progressDialog = AppUtils.showProgressDialog(mContext);
        Logger.e(TAG, "STARTED");
        sharedPreferenceData = new SharedPreferenceData(mContext);
        loginPresenter.checkLogin();
        checkValidation();
    }

    @Override
    public void updateUserDetails(String userName, String userEmail, String meetingId) {
      userName_et.setText(userName);
      emailId_et.setText(userEmail);
      meetingId_et.setText(meetingId);
    }

    public void checkValidation() {
        userName_et.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                if (userName_et.getText().toString().trim().equals("")) {
                    textInputUsername.setError(getString(R.string.validation_name_required));
                } else {
                    textInputUsername.setError(null);
                }
        });
        emailId_et.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                if (emailId_et.getText().toString().trim().equals("")) {
                    textInputEmail.setError(getString(R.string.validation_email_required));
                } else if (!AppUtils.isValidEmail(emailId_et.getText().toString().trim())) {
                    textInputEmail.setError(getString(R.string.valid_email_required));
                } else {
                    textInputEmail.setError(null);
                }
        });
        meetingId_et.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                if (meetingId_et.getText().toString().trim().equals("")) {
                    textInputMeeting.setError(getString(R.string.validation_meetingid_required));
                } else {
                    textInputMeeting.setError(null);
                }
        });

        userName_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.equals("")) {
                    textInputUsername.setError(null);
                } else {
                    textInputUsername.setError(getString(R.string.validation_name_required));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailId_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.equals("")) {
                    textInputEmail.setError(getString(R.string.validation_email_required));
                } else if (!AppUtils.isValidEmail(emailId_et.getText().toString().trim())) {
                    textInputEmail.setError(getString(R.string.valid_email_required));
                } else {
                    textInputEmail.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.equals("")) {
                    textInputEmail.setError(getString(R.string.validation_email_required));
                } else if (!AppUtils.isValidEmail(emailId_et.getText().toString().trim())) {
                    textInputEmail.setError(getString(R.string.valid_email_required));
                } else {
                    textInputEmail.setError(null);
                }
            }
        });

        meetingId_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.equals("")) {
                    textInputMeeting.setError(getString(R.string.validation_meetingid_required));
                } else {
                    textInputMeeting.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void connectButtonClick() {
        if (loginPresenter.isValid(userName_et.getText().toString(), emailId_et.getText().toString(), meetingId_et.getText().toString())) {
            loginPresenter.onGetEventDetails(userName_et.getText().toString().trim(), emailId_et.getText().toString().trim(), meetingId_et.getText().toString().trim());
        }
    }


    @Override
    public int getLayout() {
        return R.layout.activity_login_layout;
    }

    @Override
    public void onShowProgress() {
        if (progressDialog != null)
            progressDialog.show();
    }

    @Override
    public void onHideProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowSnackBar(String message) {
        Snackbar.make(parent, message, Snackbar.LENGTH_SHORT)
                .setTextColor(getResources().getColor(R.color.red))
                .setAction(R.string.snackbar_ok_title, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();

    }

    @Override
    public void onShowAlertDialog(String title, String message, boolean isLoginRequired) {
        AppUtils.showDialog(mContext, title, message, isLoginRequired);
    }

    @OnClick(R.id.bt_connect)
    @OnEditorAction(R.id.et_meetingid)
    @Override
    public void onCheckInternetConnection() {
        if (NetworkUtil.isConnected(mContext)) {
            connectButtonClick();
        } else {
            NoInternetDialog noInternetDialog = new NoInternetDialog(mContext);
            noInternetDialog.setRetryListener(this);
            noInternetDialog.show();
        }
    }

    @Override
    public void onRetry() {

    }

    @Override
    public void onBackPressed() {
        if (!LoginActivity.this.isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exit_app_title)
                    .setMessage(R.string.exit_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes_text, (dialog, id) -> finishAffinity())
                    .setNegativeButton(R.string.no_text, null)
                    .show();
        }
    }

    @Override
    public void onShowErrorDialog(String title, String message, boolean isFinishActivity) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_alert_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        // set the custom dialog components - text and button
        TextView heading = dialog.findViewById(R.id.titleTextView);
        if (title.equals(""))
            heading.setVisibility(View.GONE);
        TextView error = dialog.findViewById(R.id.errorTextView);
        heading.setText(title);
        error.setText(message);
        Button cancelButton = dialog.findViewById(R.id.okButton);
        // if button is clicked, close the custom dialog
        cancelButton.setOnClickListener(v -> {
            if (isFinishActivity) {
                this.finish();
                dialog.dismiss();

            } else
                dialog.dismiss();
        });
        dialog.show();
    }


    @Override
    public void onSucessEventData(EventResponseModelClass.DataBean data) {
        Logger.e("onSucessEventData", data);
        if(data != null){
            Intent intent = new Intent(this, EventActivity.class);
            intent.putExtra(Constants.KEY_EVENTDATA,new Gson().toJson(data));
            startActivity(intent);
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
