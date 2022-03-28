package com.rafiky.connect.ui.Login;

import com.rafiky.connect.base.MvpBase;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;

public interface LoginContract {

    interface MvpView extends MvpBase{
        void  onShowErrorDialog(String title, String message, boolean isFinishActivity);

        void onSucessEventData(EventResponseModelClass.DataBean data);

        void updateUserDetails(String userName, String userEmail, String meetingId);
    }
    interface Presnter{

        public boolean isValid(String userName,String eMail,String meetingId);

        void onGetEventDetails(String toString, String toString1, String toString2);

        void checkLogin();
    }

}
