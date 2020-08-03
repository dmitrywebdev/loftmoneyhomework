package com.dkoptin.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status") private String status;
    @SerializedName("id") private int userId;
    @SerializedName("auth_token") private String accessToken;

    public String getStatus() {
        return status;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
