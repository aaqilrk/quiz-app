package com.aaqilrk.quizapp.dao;

import com.aaqilrk.quizapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizDao extends JpaRepository<Quiz, Integer> {

}