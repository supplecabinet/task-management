package com.test.tasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.tasks.model.UserTasksPojo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TaskService {
    UserTasksPojo addTaskForUser(UserTasksPojo task, HttpServletRequest request) ;

    List<UserTasksPojo> getMyTasks(HttpServletRequest request) ;

    UserTasksPojo updateTaskForUser(UserTasksPojo task, HttpServletRequest request, Integer id) ;

    void deleteMyTask(Integer id, HttpServletRequest request) ;

    UserTasksPojo updateStatus(Integer id, HttpServletRequest request) ;

    List<UserTasksPojo> getFilteredTasks(String status, HttpServletRequest request) ;

    UserTasksPojo getMyTask(Integer id, HttpServletRequest request) ;
}
