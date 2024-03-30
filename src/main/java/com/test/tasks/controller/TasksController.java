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
        return taskService.addTaskForUser(task,request); //Add task for current user
    }

    @RequestMapping(value="tasks", method = RequestMethod.GET)
    public List<UserTasksPojo> getMyTasks(HttpServletRequest request)  {
        return taskService.getMyTasks(request); //Get tasks of current user
    }

    @RequestMapping(value = "tasks/{id}", method = RequestMethod.PUT)
    public UserTasksPojo updateTask(@RequestBody UserTasksPojo task,@PathVariable Integer id, HttpServletRequest request)  {
        return taskService.updateTaskForUser(task,request,id); //Update Task for current user
    }
    @RequestMapping(value="tasks/{id}", method = RequestMethod.DELETE)
    public void deleteTask(@PathVariable Integer id, HttpServletRequest request)  {
        taskService.deleteMyTask(id,request); //Delete task for current user
    }

    @RequestMapping(value="tasks/upgrade/status/{id}", method = RequestMethod.PUT)
    public UserTasksPojo updateStatus(@PathVariable Integer id, HttpServletRequest request)  {
        return taskService.updateStatus(id,request); //Update status of task
    }

    @RequestMapping(value="tasks/{id}", method = RequestMethod.GET)
    public UserTasksPojo getMyTask(@PathVariable Integer id, HttpServletRequest request)  {
        return taskService.getMyTask(id,request); //Get detailed task response by id
    }

    @RequestMapping(value = "tasks/filter/{status}", method = RequestMethod.GET)
    public List<UserTasksPojo> filteredList(@PathVariable String status, HttpServletRequest request)  {
        return taskService.getFilteredTasks(status,request); //Filter tasks by status for current user
    }

}
