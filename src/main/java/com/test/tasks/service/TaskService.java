package com.test.tasks.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.tasks.model.UserTasksPojo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TaskService {
    UserTasksPojo addTaskForUser(UserTasksPojo task, HttpServletRequest request) throws JsonProcessingException;

    List<UserTasksPojo> getMyTasks(HttpServletRequest request) throws JsonProcessingException;

    UserTasksPojo updateTaskForUser(UserTasksPojo task, HttpServletRequest request, Integer id) throws JsonProcessingException;

    void deleteMyTask(Integer id, HttpServletRequest request) throws JsonProcessingException;

    UserTasksPojo updateStatus(Integer id, HttpServletRequest request) throws JsonProcessingException;

    List<UserTasksPojo> getFilteredTasks(String status, HttpServletRequest request) throws JsonProcessingException;

    UserTasksPojo getMyTask(Integer id, HttpServletRequest request) throws JsonProcessingException;
}
