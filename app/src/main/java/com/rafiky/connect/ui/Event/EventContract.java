package com.rafiky.connect.ui.Event;

import com.rafiky.connect.base.MvpBase;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;

public interface EventContract {
    interface MvpView extends MvpBase {
        public void showShimmer();
        public void stopShimmer();
        void  onShowErrorDialog(String title, String message, boolean isFinishActivity);
        void onSucessEventData(EventResponseModelClass.DataBean data);
    }
    interface Presenter{
        void onGetEventDetails(String username, String email, String meetingid);
        boolean isSessionTimeCheck(EventResponseModelClass.DataBean.RoomsBean roomsBean, EventResponseModelClass.DataBean.RoomsBean.SessionsBean sessionsBean);
    }


}
