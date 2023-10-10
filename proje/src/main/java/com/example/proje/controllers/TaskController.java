package com.example.proje.controllers;


import com.example.proje.dto.TaskDto;
import com.example.proje.entities.Task;
import com.example.proje.requests.CreateTaskRequest;
import com.example.proje.response.TasksResponse;
import com.example.proje.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task createTask(@RequestBody TaskDto taskDto, HttpServletRequest httpServletRequest){
        return taskService.createTask(taskDto, httpServletRequest);
    }

    @GetMapping
    public List<Task> getTasks(HttpServletRequest httpRequest){
        return taskService.getUserTasks(httpRequest);
    }

    @GetMapping("/finished")
    public List<Task> getFinishedTask(HttpServletRequest httpServletRequest){
        return taskService.getFinishedTask(httpServletRequest);
    }


    @PutMapping("/{taskId}")
    public Task updateTask(@RequestBody TaskDto taskDto,@PathVariable Long taskId,
                           HttpServletRequest httpServletRequest){
        return taskService.updateTask(taskDto,taskId, httpServletRequest);
    }

    @GetMapping("/{taskId}")
    public TasksResponse finishTask(@PathVariable Long taskId, HttpServletRequest httpServletRequest){
        return taskService.finishTask(taskId, httpServletRequest);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId, HttpServletRequest httpServletRequest){
        taskService.deleteTask(taskId, httpServletRequest);
    }

}