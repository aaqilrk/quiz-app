package com.aaqilrk.quizapp.controller;

import com.aaqilrk.quizapp.model.QuestionWrapper;
import com.aaqilrk.quizapp.model.QuizResponse;
import com.aaqilrk.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {
    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String title, @RequestParam String category, @RequestParam Integer noOfQuestions) {
        return quizService.createQuiz(title, category, noOfQuestions);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuiz(@PathVariable Integer id) {
        return quizService.getQuiz(id);
    }

    @GetMapping("submit/{id}")
    public ResponseEntity<Integer> calculateScore(@PathVariable Integer id, @RequestBody List<QuizResponse> responses) {
        return quizService.calculateScore(id, responses);
    }
}
