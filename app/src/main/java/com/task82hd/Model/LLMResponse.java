package com.task82hd.Model;

import com.google.gson.annotations.SerializedName;

public class LLMResponse {

    @SerializedName("classification")
    private String classification;

    @SerializedName("isQuestion")
    private boolean isQuestion;

    @SerializedName("itemName")
    private String name;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public void setQuestion(boolean question) {
        isQuestion = question;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
