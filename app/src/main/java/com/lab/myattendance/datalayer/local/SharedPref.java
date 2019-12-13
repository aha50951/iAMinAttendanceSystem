package com.lab.myattendance.datalayer.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.lab.myattendance.datalayer.model.response.LoginResponse;


/**
 * This class is used to save and retrieve the App data in a sharedPreference File
 */
public class SharedPref {

    private static final String SHARED_NAME = "my_shared";
    private static final String IS_LOGGED_IN = "logged_in";
    private static final String IMAGE_URL = "image_url";
    private static final String USER_NAME = "user_name";
    private static final String NAME = "name";
    private static final String MOBILE = "mobile";
    private static final String USER_Type = "user_type";
    private static final String LOGIN_RESPONSE = "login_response";


    private static SharedPref sharedPref;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized SharedPref getInstance(Context context) {
        if (sharedPref == null) {
            sharedPref = new SharedPref(context);
        }

        return sharedPref;
    }

    public boolean isLoggedin() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public String getImageUrl() {
        return sharedPreferences.getString(IMAGE_URL, "");
    }

    public void setLogin(Boolean login) {
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, login).commit();
    }

    public void setName(String name) {
        sharedPreferences.edit().putString(NAME, name).commit();
    }

    public String getName() {
        return sharedPreferences.getString(NAME, "");
    }

    public void setImageUrl(String imageUrl) {
        sharedPreferences.edit().putString(IMAGE_URL, imageUrl).commit();
    }

    public void setUserName(String userName) {
        sharedPreferences.edit().putString(USER_NAME, userName);
    }

    public void setMobile(String mobile) {
        sharedPreferences.edit().putString(MOBILE, mobile);
    }

    public int getUserType() {
        return sharedPreferences.getInt(USER_Type, 1);
    }

    public void setUserType(int userType) {
        sharedPreferences.edit().putInt(USER_Type, userType).commit();
    }

    public void saveLoginModel(LoginResponse loginResponse) {
        String str = gson.toJson(loginResponse);
        sharedPreferences.edit().putString(LOGIN_RESPONSE, str).commit();
    }

    public LoginResponse getLoginModel() {
        String str = sharedPreferences.getString(LOGIN_RESPONSE, "");
        if (!str.isEmpty()) {
            LoginResponse response = gson.fromJson(str, LoginResponse.class);
            return response;
        }
        return null;
    }
}
