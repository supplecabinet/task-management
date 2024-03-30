package com.test.tasks.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.tasks.model.UserTasksPojo;
import com.test.tasks.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class TasksController {

    @Autowired
    TaskService taskService;

    @RequestMapping(value="tasks",method = RequestMethod.POST)
    public UserTasksPojo addTask(@RequestBody UserTasksPojo task, HttpServletRequest request)  {
        return taskService.addTaskForUser(task,request);
    }

    @RequestMapping(value="tasks", method = RequestMethod.GET)
    public List<UserTasksPojo> getMyTasks(HttpServletRequest request)  {
        return taskService.getMyTasks(request);
    }

    @RequestMapping(value = "tasks/{id}", method = RequestMethod.PUT)
    public UserTasksPojo updateTask(@RequestBody UserTasksPojo task,@PathVariable Integer id, HttpServletRequest request)  {
        return taskService.updateTaskForUser(task,request,id);
    }
    @RequestMapping(value="tasks/{id}", method = RequestMethod.DELETE)
    public void deleteTask(@PathVariable Integer id, HttpServletRequest request)  {
        taskService.deleteMyTask(id,request);
    }

    @RequestMapping(value="tasks/upgrade/status/{id}", method = RequestMethod.PUT)
    public UserTasksPojo updateStatus(@PathVariable Integer id, HttpServletRequest request)  {
        return taskService.updateStatus(id,request);
    }

    @RequestMapping(value="tasks/{id}", method = RequestMethod.GET)
    public UserTasksPojo getMyTask(@PathVariable Integer id, HttpServletRequest request)  {
        return taskService.getMyTask(id,request);
    }

    @RequestMapping(value = "tasks/filter/{status}", method = RequestMethod.GET)
    public List<UserTasksPojo> filteredList(@PathVariable String status, HttpServletRequest request)  {
        return taskService.getFilteredTasks(status,request);
    }


}
