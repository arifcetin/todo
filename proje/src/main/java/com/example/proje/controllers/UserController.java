package com.example.proje.controllers;


import com.example.proje.entities.User;
import com.example.proje.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User newUser){
        return userService.createUser(newUser);
    }


    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getOneUser(@PathVariable Long userId){
        return userService.getOneUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void  userDelete(@PathVariable Long userId){
        userService.userDelete(userId);
    }

    @PutMapping("/{userId}")
    public User userUpdate(@RequestBody User updateUser, @PathVariable Long userId){
        return userService.updateUser(updateUser,userId);
    }
}
