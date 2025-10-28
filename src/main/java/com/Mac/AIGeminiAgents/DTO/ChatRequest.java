package com.Mac.AIGeminiAgents.DTO;



public class ChatRequest {
    private String userQuestion;
    private String clarificationAnswer;

    public String getUserQuestion() {
        return userQuestion;
    }
    public void setUserQuestion(String userQuestion) {
        this.userQuestion = userQuestion;
    }

    public String getClarificationAnswer() {
        return clarificationAnswer;
    }
    public void setClarificationAnswer(String clarificationAnswer) {
        this.clarificationAnswer = clarificationAnswer;
    }
}
