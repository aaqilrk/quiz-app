package com.aaqilrk.quizapp.service;

import com.aaqilrk.quizapp.model.Question;
import com.aaqilrk.quizapp.dao.QuestionDao;
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
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        try {
            questions = questionDao.findAll();
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            logger.error("Error fetching all questions", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(questions);
        }
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        List<Question> questionsByCategory = new ArrayList<>();
        try {
            questionsByCategory = questionDao.findByCategory(category);
            return ResponseEntity.ok(questionsByCategory);
        } catch (Exception e) {
            logger.error("Error fetching questions by category", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(questionsByCategory);
        }
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return ResponseEntity.status(HttpStatus.CREATED).body("Question added successfully");
        } catch(Exception e) {
            logger.error("Error adding question", e);
            return ResponseEntity.internalServerError().body("Error adding question!");
        }
    }

    public ResponseEntity<String> deleteQuestion(Integer id) {
        try {
            Optional<Question> optionalQuestion = questionDao.findById(id); // Use findById instead of getReferenceById

            if (optionalQuestion.isPresent()) {
                Question question = optionalQuestion.get();
                questionDao.delete(question);
                return ResponseEntity.status(HttpStatus.OK).body("Question deleted successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found.");
            }
        } catch (Exception e) {
            logger.error("Error deleting question with id {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting question.");
        }
    }

    public ResponseEntity<String> updateQuestion(Integer id, Question updatedQuestion) {
        try {
            Optional<Question> optionalQuestion = questionDao.findById(id);

            if (optionalQuestion.isPresent()) {
                Question existingQuestion = optionalQuestion.get();

                existingQuestion.setCategory(updatedQuestion.getCategory());
                existingQuestion.setOption1(updatedQuestion.getOption1());
                existingQuestion.setOption2(updatedQuestion.getOption2());
                existingQuestion.setOption3(updatedQuestion.getOption3());
                existingQuestion.setOption4(updatedQuestion.getOption4());
                existingQuestion.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
                existingQuestion.setQuestionTitle(updatedQuestion.getQuestionTitle());
                existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());

                questionDao.save(existingQuestion);
                return ResponseEntity.status(HttpStatus.OK).body("Question updated successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found with ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error updating question with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating question."); // Return 500 Internal Server Error with failure message
        }
    }
}
