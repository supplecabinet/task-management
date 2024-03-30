package com.test.tasks.service;

import com.test.tasks.model.*;
import com.test.tasks.repository.TasksRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TasksRepository tasksRepository;
    @Autowired
    UserService userService;

    @Override
    @Transactional
    public UserTasksPojo addTaskForUser(UserTasksPojo task, HttpServletRequest request) {
        String userId = userService.getUserFromToken(request); //Extract current user from token
        UserTasks ut = new UserTasks(); //Initialize new object entity
        ut.setAddDate(new Date());
        ut.setTaskDesc(task.getDescription());
        ut.setTaskTitle(task.getTaskTitle());
        ut.setPriority(task.getPriority());
        ut.setStatus(TaskStatus.TODO.getValue());
        ut.setUserId(userId);
        return entityToBean(tasksRepository.save(ut)); //Save into table and return response
    }

    @Override
    public List<UserTasksPojo> getMyTasks(HttpServletRequest request) {
        String userId = "";
        try {
            userId = userService.getUserFromToken(request);
        } catch(Exception e) {
            return new ArrayList<>();
        }
        List<UserTasks> tasks = tasksRepository.findByUserId(userId); //Get tasks of current user
        return tasks.stream().map(this::entityToBean).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserTasksPojo updateTaskForUser(UserTasksPojo task, HttpServletRequest request, Integer id) {
        String userId = userService.getUserFromToken(request);
        Optional<UserTasks> tasks = tasksRepository.findById(id);
        if (tasks.isEmpty()) {
            throw new RuntimeException("Invalid task Id!"); //Check for invalid task
        }
        UserTasks ut = tasks.get();
        if (!ut.getUserId().equalsIgnoreCase(userId)) {
            throw new RuntimeException("Security Breach Attempt!"); //Check for security threat
        }
        ut.setTaskDesc(task.getDescription());
        ut.setPriority(task.getPriority());
        ut.setTaskTitle(task.getTaskTitle());
        ut.setModDate(new Date());
        ut.setStatus(TaskStatus.getValueByName(task.getStatus())); //Find numeric mapping and set value
        return entityToBean(tasksRepository.save(ut));

    }

    @Override
    public void deleteMyTask(Integer id, HttpServletRequest request) {
        String userId = userService.getUserFromToken(request);
        Optional<UserTasks> ut = tasksRepository.findById(id);
        if (ut.isEmpty()) {
            throw new RuntimeException("Invalid task Id!"); //Check for invalid task
        }
        UserTasks task = ut.get();
        if (!task.getUserId().equalsIgnoreCase(userId)) {
            throw new RuntimeException("Security Breach Attempt!"); //Check for security threat
        }
        tasksRepository.delete(task);
    }

    @Override
    public UserTasksPojo updateStatus(Integer id, HttpServletRequest request)  {
        String userId = userService.getUserFromToken(request);
        Optional<UserTasks> ut = tasksRepository.findById(id);
        if (ut.isEmpty()) {
            throw new RuntimeException("Invalid task Id!"); //Check for invalid task
        }
        UserTasks task = ut.get();
        if (!task.getUserId().equalsIgnoreCase(userId)) {
            throw new RuntimeException("Security Breach Attempt!"); //Check for security threat
        }
        if (task.getStatus().equals(TaskStatus.DONE.getValue())) {
            throw new RuntimeException("Task already completed!"); //Check for invalid task status
        }
        task.setStatus(task.getStatus() + 1); //Increment status
        task.setModDate(new Date());
        return entityToBean(tasksRepository.save(task));
    }

    @Override
    public List<UserTasksPojo> getFilteredTasks(String status, HttpServletRequest request)  {
        String userId = userService.getUserFromToken(request);
        List<UserTasks> tasks = tasksRepository.findByUserIdAndStatus(userId,TaskStatus.getValueByName(status));
        //Get tasks of current user with filtered status
        return tasks.stream().map(this::entityToBean).collect(Collectors.toList());
    }

    @Override
    public UserTasksPojo getMyTask(Integer id, HttpServletRequest request) {
        String userId = userService.getUserFromToken(request);
        Optional<UserTasks> ut = tasksRepository.findById(id);
        if (ut.isEmpty()) {
            throw new RuntimeException("Invalid task Id!"); //Check for invalid task
        }
        UserTasks task = ut.get();
        if (!task.getUserId().equalsIgnoreCase(userId)) {
            throw new RuntimeException("Security Breach Attempt!"); //Check for security threat
        }
        return entityToBean(task);
    }

    private UserTasksPojo entityToBean(UserTasks userTasks) { //Method to not directly expose entity

        UserTasksPojo userTasksPojo = new UserTasksPojo();
        userTasksPojo.setDescription(userTasks.getTaskDesc());
        userTasksPojo.setAddDate(userTasks.getAddDate());
        userTasksPojo.setId(userTasks.getId());
        userTasksPojo.setModDate(userTasks.getModDate());
        userTasksPojo.setUserId(userTasks.getUserId());
        userTasksPojo.setStatus(TaskStatus.getName(userTasks.getStatus())); //Find name from value
        userTasksPojo.setStatusId(userTasks.getStatus());
        userTasksPojo.setTaskTitle(userTasks.getTaskTitle());
        userTasksPojo.setPriority(userTasks.getPriority());

        return userTasksPojo;
    }

}
