package com.lab.myattendance.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


/**
 * Base ViewModel Class
 * */
public class BaseViewModel extends ViewModel {

    public MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<String> getStringMutableLiveData() {
        return stringMutableLiveData;
    }
}
