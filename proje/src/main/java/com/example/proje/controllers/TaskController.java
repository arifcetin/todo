package com.example.proje.controllers;


import com.example.proje.dto.FinishTaskDto;
import com.example.proje.dto.TaskDto;
import com.example.proje.entities.Task;
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
        Task task = taskService.createTask(taskDto, httpServletRequest);
        return task;
    }

    @GetMapping
    public List<Task> getTasks(HttpServletRequest httpRequest){
        List<Task> tasks = taskService.getUserTasks(httpRequest);
        return tasks;
    }

    @GetMapping("/finished")
    public List<Task> getFinishedTask(HttpServletRequest httpServletRequest){
        List<Task> tasks = taskService.getFinishedTask(httpServletRequest);
        return tasks;
    }

    @PutMapping("/{taskId}")
    public Task updateTask(@RequestBody TaskDto taskDto,@PathVariable Long taskId,
                           HttpServletRequest httpServletRequest){
        Task task = taskService.updateTask(taskDto,taskId, httpServletRequest);
        return task;
    }

    @GetMapping("/{taskId}")
    public FinishTaskDto finishTask(@PathVariable Long taskId, HttpServletRequest httpServletRequest){
        FinishTaskDto finishTaskDto = taskService.finishTask(taskId, httpServletRequest);
        return finishTaskDto;
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId, HttpServletRequest httpServletRequest){
        taskService.deleteTask(taskId, httpServletRequest);
    }

}