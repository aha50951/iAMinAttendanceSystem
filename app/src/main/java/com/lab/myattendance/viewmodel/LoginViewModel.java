package com.lab.myattendance.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.lab.myattendance.datalayer.model.response.LoginResponse;
import com.lab.myattendance.datalayer.remote.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * LoginViewModel Class that is used to call these APIs
 * 1- callStudentLogin
 * 2- callLecturerLogin
 */
public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<LoginResponse> getLoginResponseMutableLiveData() {
        return loginResponseMutableLiveData;
    }

    /**
     * LiveData Object used to send the data to the activity to update the UI
     */
    private MutableLiveData<LoginResponse> loginResponseMutableLiveData = new MutableLiveData<>();

    public void callStudentLogin(String userName, String password) {

        ServiceGenerator.getInstance().create().callStudentLogin(userName, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::loginSuccess, this::error);
    }

    public void callLecturerLogin(String userName, String password) {

        ServiceGenerator.getInstance().create().callLecturerLogin(userName, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::loginSuccess, this::error);
    }

    private void error(Throwable throwable) {
        stringMutableLiveData.setValue(throwable.getMessage());
    }

    private void loginSuccess(LoginResponse loginResponse) {
        loginResponseMutableLiveData.setValue(loginResponse);
    }
}
