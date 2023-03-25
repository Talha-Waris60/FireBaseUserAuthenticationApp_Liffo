package com.devdroidDev.liffo;

import com.google.firebase.firestore.auth.User;

public class UserModel {

    private String userName, email, phoneNumber, password;
    public UserModel()
    {

    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public UserModel(String userName, String email, String phoneNumber, String password)
    {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

}
