package com.mtechproject.gsastry.authenticationservice.dto;

public class SignUpRequest {

    private String userName;
    private String password;
    private String userFullName;
    private boolean isJobSeeker;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isJobSeeker() {
        return isJobSeeker;
    }

    public void setJobSeeker(boolean jobSeeker) {
        isJobSeeker = jobSeeker;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
}
