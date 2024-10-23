package com.aaqilrk.quizapp.model;

import lombok.Data;

@Data
public class QuizResponse {
    private Integer id;
    private String response;

    public QuizResponse(Integer id, String response) {
        this.id = id;
        this.response = response;
    }
}
