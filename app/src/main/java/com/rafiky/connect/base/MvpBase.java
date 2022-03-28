package com.rafiky.connect.base;

public interface MvpBase {
    void onShowProgress();
    void onHideProgress();
    void onShowToast(String message);
    void onShowSnackBar(String message);
    void onShowAlertDialog(String title, String message, boolean isLoginRequired);
    void onCheckInternetConnection();
    void onRetry();

}
