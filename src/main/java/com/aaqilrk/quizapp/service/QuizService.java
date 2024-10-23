package com.aaqilrk.quizapp.service;

import com.aaqilrk.quizapp.dao.QuestionDao;
import com.aaqilrk.quizapp.dao.QuizDao;
import com.aaqilrk.quizapp.model.Question;
import com.aaqilrk.quizapp.model.QuestionWrapper;
import com.aaqilrk.quizapp.model.Quiz;
import com.aaqilrk.quizapp.model.QuizResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String title, String category, Integer noOfQuestions) {
        try {
            List<Question> questionsByCategory = questionDao.findXQuestionsByCategory(category, noOfQuestions);

            if(questionsByCategory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No questions found of this category");
            } else {
                Quiz quiz = new Quiz();
                quiz.setQuestions(questionsByCategory);
                quiz.setTitle(title);
                quizDao.save(quiz);
                return ResponseEntity.status(HttpStatus.CREATED).body("Quiz created");
            }
        } catch(Exception e) {
            logger.error("Error creating quiz", e);
            return ResponseEntity.internalServerError().body("Error creating quiz");
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuiz(Integer id) {
        try {
            Optional<Quiz> optionalQuiz = quizDao.findById(id);

            if (optionalQuiz.isPresent()) {
                List<Question> questionsFromDB = optionalQuiz.get().getQuestions();
                List<QuestionWrapper> questionForUser = new ArrayList<>();

                for (Question q : questionsFromDB) {
                    QuestionWrapper questionWrapper = new QuestionWrapper(q.getId(), q.getCategory(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4(), q.getDifficultyLevel(), q.getQuestionTitle());
                    questionForUser.add(questionWrapper);
                }
                
                return ResponseEntity.ok(questionForUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
            }
        } catch(Exception e) {
            logger.error("Error fetching quiz", e);
            return ResponseEntity.internalServerError().body(new ArrayList<>());
        }
    }

    public ResponseEntity<Integer> calculateScore(Integer id, List<QuizResponse> responses) {
        Optional<Quiz> optionalQuiz = quizDao.findById(id);

        if(optionalQuiz.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        } else {
            Quiz quiz = optionalQuiz.get();
            List<Question> questions = quiz.getQuestions();

            int score = 0;

            for(int i = 0; i < questions.size(); i++) {
                if(responses.get(i).getResponse().equals(questions.get(i).getCorrectAnswer()))
                    score++;
            }

            return ResponseEntity.ok(score);
        }
    }
}
