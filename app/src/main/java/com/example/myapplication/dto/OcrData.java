package com.example.myapplication.dto;

import com.google.gson.annotations.SerializedName;


public class OcrData {

    @SerializedName("data")
    private String data;

    // Getter 및 Setter 메서드

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
