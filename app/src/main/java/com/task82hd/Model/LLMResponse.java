package com.task82hd.Model;

import com.google.gson.annotations.SerializedName;

public class LLMResponse {

    @SerializedName("classification")
    private String classification;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
