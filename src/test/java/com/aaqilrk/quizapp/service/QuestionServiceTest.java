package com.aaqilrk.quizapp.service;

import com.aaqilrk.quizapp.dao.QuestionDao;
import com.aaqilrk.quizapp.model.Question;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllQuestions_Success() throws Exception{
        // Arrange
        List<Question> mockQuestions = new ArrayList<>(Arrays.asList(
                new Question(1, "Programming", "Python", "Java", "C++", "Ruby", "Easy", "Which of the following is a dynamically typed language?", "Python"),
                new Question(2, "Data Structures", "Stack", "Queue", "Heap", "Array", "Medium", "Which data structure follows the LIFO (Last In, First Out) principle?", "Stack"),
                new Question(3, "Algorithms", "Merge Sort", "Quick Sort", "Bubble Sort", "Binary Search", "Hard", "Which of the following sorting algorithms has an average time complexity of O(n log n)?", "Merge Sort"),
                new Question(4, "Networking", "Application", "Transport", "Network", "Data Link", "Medium", "At which layer of the OSI model does the TCP protocol operate?", "Transport"),
                new Question(5, "Operating Systems", "Deadlock", "Starvation", "Mutex", "Semaphore", "Hard", "What term is used when two or more processes are unable to proceed because each is waiting for the other to release resources?", "Deadlock")
        ));

        when(questionDao.findAll()).thenReturn(mockQuestions);

        // Act
        ResponseEntity<List<Question>> response = questionService.getAllQuestions();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockQuestions, response.getBody());
    }

    @Test
    public void testGetAllQuestions_Failure() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(questionDao).findAll();

        // Act
        ResponseEntity<List<Question>> response = questionService.getAllQuestions();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(0, response.getBody().size());  // Since exception was thrown, the list should be empty
    }

    @Test
    public void testGetQuestionsByCategory_Success() {
        // Arrange
        List<Question> mockQuestions = List.of(
                new Question(1, "Programming", "Option1", "Option2", "Option3", "Option4", "Easy", "What is Java?", "Option1"),
                new Question(2, "Programming", "OptionA", "OptionB", "OptionC", "OptionD", "Medium", "What is Polymorphism?", "OptionA")
        );
        String category = "Programming";
        when(questionDao.findByCategory(category)).thenReturn(mockQuestions);

        // Act
        ResponseEntity<List<Question>> response = questionService.getQuestionsByCategory(category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockQuestions, response.getBody());
    }

    @Test
    public void testGetQuestionsByCategory_Failure() {
        // Arrange
        String category = "Science";
        doThrow(new RuntimeException("Database error")).when(questionDao).findByCategory(category);

        // Act
        ResponseEntity<List<Question>> response = questionService.getQuestionsByCategory(category);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testAddQuestion_Success() {
        // Arrange
        Question newQuestion = new Question(null, "Programming", "Option1", "Option2", "Option3", "Option4", "Medium", "What is Java?", "Option1");
        when(questionDao.save(newQuestion)).thenReturn(newQuestion);

        // Act
        ResponseEntity<String> response = questionService.addQuestion(newQuestion);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Question added successfully", response.getBody());
    }

    @Test
    public void testAddQuestion_Failure() {
        // Arrange
        Question newQuestion = new Question(null, "Programming", "Option1", "Option2", "Option3", "Option4", "Medium", "What is Java?", "Option1");
        doThrow(new RuntimeException("Database error")).when(questionDao).save(newQuestion);

        // Act
        ResponseEntity<String> response = questionService.addQuestion(newQuestion);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error adding question!", response.getBody());
    }

    @Test
    public void testDeleteQuestion_Success() {
        // Arrange
        Integer questionId = 1;
        Question existingQuestion = new Question(1, "Programming", "Option1", "Option2", "Option3", "Option4", "Easy", "What is Java?", "Option1");
        when(questionDao.findById(questionId)).thenReturn(Optional.of(existingQuestion));

        // Act
        ResponseEntity<String> response = questionService.deleteQuestion(questionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Question deleted successfully!", response.getBody());
        verify(questionDao).delete(existingQuestion);  // Verify that delete() was called
    }

    @Test
    public void testDeleteQuestion_NotFound() {
        // Arrange
        Integer questionId = 1;
        when(questionDao.findById(questionId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = questionService.deleteQuestion(questionId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Question not found.", response.getBody());
    }

    @Test
    public void testDeleteQuestion_Failure() {
        // Arrange
        Integer questionId = 1;
        when(questionDao.findById(questionId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<String> response = questionService.deleteQuestion(questionId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error deleting question.", response.getBody());
    }

    @Test
    public void testUpdateQuestion_Success() {
        // Arrange
        Integer questionId = 1;
        Question existingQuestion = new Question(1, "Programming", "Option1", "Option2", "Option3", "Option4", "Easy", "What is Java?", "Option1");
        Question updatedQuestion = new Question(1, "Programming", "UpdatedOption1", "UpdatedOption2", "UpdatedOption3", "UpdatedOption4", "Medium", "What is Spring?", "UpdatedOption1");

        when(questionDao.findById(questionId)).thenReturn(Optional.of(existingQuestion));

        // Act
        ResponseEntity<String> response = questionService.updateQuestion(questionId, updatedQuestion);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Question updated successfully!", response.getBody());
        verify(questionDao).save(existingQuestion);  // Verify save() was called with the updated data
    }

    @Test
    public void testUpdateQuestion_NotFound() {
        // Arrange
        Integer questionId = 1;
        Question updatedQuestion = new Question(1, "Programming", "UpdatedOption1", "UpdatedOption2", "UpdatedOption3", "UpdatedOption4", "Medium", "What is Spring?", "UpdatedOption1");
        when(questionDao.findById(questionId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = questionService.updateQuestion(questionId, updatedQuestion);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Question not found with ID: " + questionId, response.getBody());
    }

    @Test
    public void testUpdateQuestion_Failure() {
        // Arrange
        Integer questionId = 1;
        Question updatedQuestion = new Question(1, "Programming", "UpdatedOption1", "UpdatedOption2", "UpdatedOption3", "UpdatedOption4", "Medium", "What is Spring?", "UpdatedOption1");
        when(questionDao.findById(questionId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<String> response = questionService.updateQuestion(questionId, updatedQuestion);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error updating question.", response.getBody());
    }
}
