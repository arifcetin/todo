package com.example.proje.controllers;


import com.example.proje.dto.UserDto;
import com.example.proje.entities.User;
import com.example.proje.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public UserDto createUser(@RequestBody User user){
        UserDto userDto = userService.createUser(user);
        return userDto;
    }


    @GetMapping
    public List<UserDto> getUsers(){
        List<UserDto> userDtos = userService.getUsers();
        return userDtos;
    }

    @GetMapping("/{userId}")
    public UserDto getOneUser(@PathVariable Long userId){
        UserDto userDto = userService.getOneUser(userId);
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void  userDelete(@PathVariable Long userId){
        userService.userDelete(userId);
    }

    @PutMapping("/{userId}")
    public UserDto userUpdate(@RequestBody User updateUser, @PathVariable Long userId){
        UserDto userDto = userService.updateUser(updateUser,userId);
        return userDto;
    }
}
