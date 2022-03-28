package com.rafiky.connect.ui.Streaming;

import com.rafiky.connect.base.MvpBase;
import com.rafiky.connect.model.responsemodel.UserUpdateDetailsResponse;

public interface StreamingContract {

    interface MvpView extends MvpBase{
        public void showShimmer();
        public void stopShimmer();
        void onSuccsessListerDetails(UserUpdateDetailsResponse.DataBean data);
    }

    interface Presenter{
        void updateListnerDetails(String name, String eventId);
        void onUpdateTime(String userId);
    }

}
