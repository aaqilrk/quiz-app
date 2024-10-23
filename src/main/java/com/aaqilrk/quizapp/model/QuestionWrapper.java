package com.aaqilrk.quizapp.model;

import lombok.Data;

@Data
public class QuestionWrapper {

    private Integer id;
    private String category;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String difficultyLevel;
    private String questionTitle;

    public QuestionWrapper(Integer id, String category, String option1, String option2, String option3, String option4, String difficultyLevel, String questionTitle) {
        this.id = id;
        this.category = category;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.difficultyLevel = difficultyLevel;
        this.questionTitle = questionTitle;
    }
}
