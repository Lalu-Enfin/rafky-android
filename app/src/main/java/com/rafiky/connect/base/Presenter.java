package com.rafiky.connect.base;

public interface Presenter<V extends MvpBase> {

    void attachView(V mvpView);

    void detachView();
}
