package com.example.proje.services;



import com.example.proje.dto.TaskDto;
import com.example.proje.entities.Task;
import com.example.proje.entities.User;
import com.example.proje.repo.TaskRepository;
import com.example.proje.repo.UserRepository;
import com.example.proje.requests.CreateTaskRequest;
import com.example.proje.response.TasksResponse;
import com.example.proje.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Task createTask(TaskDto taskDto, HttpServletRequest httpServletRequest) {
        String bearer = httpServletRequest.getHeader("Authorization");
        Long user_id = jwtTokenProvider.getUserIdFromJwt(bearer.substring("Bearer".length()+1));
        Optional<User> user = userRepository.findById(user_id);
        if(user.isPresent()){
            Task newTask = new Task();
            newTask.setText(taskDto.getText());
            newTask.setCreateDate(new Date());
            newTask.setUser(user.get());
            newTask.setFinished(false);
            taskRepository.save(newTask);
            return newTask;
        }
        return null;
    }

    public List<Task> getUserTasks(HttpServletRequest httpRequest) {
        String bearer = httpRequest.getHeader("Authorization");
        Long user_id = jwtTokenProvider.getUserIdFromJwt(bearer.substring("Bearer".length()+1));
        return taskRepository.findTaskFromUserId(user_id);
    }


    public Task updateTask(TaskDto taskDto, Long taskId, HttpServletRequest httpServletRequest) {
        String bearer = httpServletRequest.getHeader("Authorization");
        Long user_id = jwtTokenProvider.getUserIdFromJwt(bearer.substring("Bearer".length() + 1));
        Optional<Task> foundTask = taskRepository.findById(taskId);
        if (foundTask.isPresent() && foundTask.get().getUser().getUser_id().equals(user_id)) {
            foundTask.get().setText(taskDto.getText());
            taskRepository.save(foundTask.get());
            return foundTask.get();
        } else
            return null;
    }

    public TasksResponse finishTask(Long taskId, HttpServletRequest httpServletRequest) {
        String bearer = httpServletRequest.getHeader("Authorization");
        Long user_id = jwtTokenProvider.getUserIdFromJwt(bearer.substring("Bearer".length() + 1));
        Optional<Task> foundTask = taskRepository.findById(taskId);
        if (foundTask.isPresent() && foundTask.get().getUser().getUser_id().equals(user_id)){
            foundTask.get().setFinished(true);
            TasksResponse tasksResponse = new TasksResponse();
            tasksResponse.setFinishDate(new Date());
            taskRepository.save(foundTask.get());
            long fark = new Date().getTime()-foundTask.get().getCreateDate().getTime();
            tasksResponse.setMessage("görevin bitmesi için geçen gün:"+ TimeUnit.DAYS.convert(fark,TimeUnit.MICROSECONDS));
            return tasksResponse;
        }
        else
            return null;
    }


    public void deleteTask(Long taskId, HttpServletRequest httpServletRequest) {
        String bearer = httpServletRequest.getHeader("Authorization");
        Long user_id = jwtTokenProvider.getUserIdFromJwt(bearer.substring("Bearer".length() + 1));
        Optional<Task> foundTask = taskRepository.findById(taskId);
        if (foundTask.isPresent() && foundTask.get().getUser().getUser_id().equals(user_id)){
            taskRepository.deleteById(taskId);
        }
    }

    public List<Task> getFinishedTask(HttpServletRequest httpServletRequest) {
        String bearer = httpServletRequest.getHeader("Authorization");
        Long user_id = jwtTokenProvider.getUserIdFromJwt(bearer.substring("Bearer".length()+1));
        return taskRepository.findFinishedTask(user_id);
    }
}

