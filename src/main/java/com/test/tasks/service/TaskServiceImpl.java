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
        String userId = userService.getUserFromToken(request);
        UserTasks ut = new UserTasks();
        ut.setAddDate(new Date());
        ut.setTaskDesc(task.getDescription());
        ut.setTaskTitle(task.getTaskTitle());
        if (task.getPriority()!=null) {
            ut.setPriority(task.getPriority()); //Optional
        }
        ut.setStatus(TaskStatus.TODO.getValue());
        ut.setUserId(userId);
        return entityToBean(tasksRepository.save(ut));
    }

    @Override
    public List<UserTasksPojo> getMyTasks(HttpServletRequest request) {
        String userId = "";
        try {
            userId = userService.getUserFromToken(request);
        } catch(Exception e) {
            return new ArrayList<>();
        }
        List<UserTasks> tasks = tasksRepository.findByUserId(userId);
        return tasks.stream().map(this::entityToBean).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserTasksPojo updateTaskForUser(UserTasksPojo task, HttpServletRequest request, Integer id) {
        String userId = userService.getUserFromToken(request);
        Optional<UserTasks> tasks = tasksRepository.findById(id);
        if (tasks.isEmpty()) {
            throw new RuntimeException("Invalid task Id!");
        }
        UserTasks ut = tasks.get();
        if (!ut.getUserId().equalsIgnoreCase(userId)) {
            throw new RuntimeException("Security Breach Attempt!");
        }
        ut.setTaskDesc(task.getDescription());
        ut.setPriority(task.getPriority());
        ut.setTaskTitle(task.getTaskTitle());
        ut.setModDate(new Date());
        ut.setStatus(TaskStatus.getValueByName(task.getStatus()));
        return entityToBean(tasksRepository.save(ut));

    }

    @Override
    public void deleteMyTask(Integer id, HttpServletRequest request) {
        String userId = userService.getUserFromToken(request);
        Optional<UserTasks> ut = tasksRepository.findById(id);
        if (ut.isEmpty()) {
            throw new RuntimeException("Invalid task Id!");
        }
        UserTasks task = ut.get();
        if (!task.getUserId().equalsIgnoreCase(userId)) {
            throw new RuntimeException("Security Breach Attempt!");
        }
        tasksRepository.delete(task);
    }

    @Override
    public UserTasksPojo updateStatus(Integer id, HttpServletRequest request)  {
        String userId = userService.getUserFromToken(request);
        Optional<UserTasks> ut = tasksRepository.findById(id);
        if (ut.isEmpty()) {
            throw new RuntimeException("Invalid task Id!");
        }
        UserTasks task = ut.get();
        if (!task.getUserId().equalsIgnoreCase(userId)) {
            throw new RuntimeException("Security Breach Attempt!");
        }
        if (task.getStatus().equals(TaskStatus.DONE.getValue())) {
            throw new RuntimeException("Task already completed!");
        }
        task.setStatus(task.getStatus() + 1);
        task.setModDate(new Date());
        return entityToBean(tasksRepository.save(task));
    }

    @Override
    public List<UserTasksPojo> getFilteredTasks(String status, HttpServletRequest request)  {
        String userId = userService.getUserFromToken(request);
        List<UserTasks> tasks = tasksRepository.findByUserIdAndStatus(userId,TaskStatus.getValueByName(status));
        return tasks.stream().map(this::entityToBean).collect(Collectors.toList());
    }

    @Override
    public UserTasksPojo getMyTask(Integer id, HttpServletRequest request) {
        String userId = userService.getUserFromToken(request);
        Optional<UserTasks> ut = tasksRepository.findById(id);
        if (ut.isEmpty()) {
            throw new RuntimeException("Invalid task Id!");
        }
        UserTasks task = ut.get();
        if (!task.getUserId().equalsIgnoreCase(userId)) {
            throw new RuntimeException("Security Breach Attempt!");
        }
        return entityToBean(task);
    }

    private UserTasksPojo entityToBean(UserTasks userTasks) {

        UserTasksPojo userTasksPojo = new UserTasksPojo();
        userTasksPojo.setDescription(userTasks.getTaskDesc());
        userTasksPojo.setAddDate(userTasks.getAddDate());
        userTasksPojo.setId(userTasks.getId());
        userTasksPojo.setModDate(userTasks.getModDate());
        userTasksPojo.setUserId(userTasks.getUserId());
        userTasksPojo.setStatus(TaskStatus.getName(userTasks.getStatus()));
        userTasksPojo.setStatusId(userTasks.getStatus());
        userTasksPojo.setTaskTitle(userTasks.getTaskTitle());
        userTasksPojo.setPriority(userTasks.getPriority());

        return userTasksPojo;
    }

}
