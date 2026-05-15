package com.task82hd.Model;

import com.google.gson.annotations.SerializedName;

public class LLMResponse {

    @SerializedName("classification")
    private String classification;

    @SerializedName("isQuestion")
    private boolean isQuestion;

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
}
