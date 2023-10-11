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
    public UserDto createUser(@RequestBody UserDto userDto){
        UserDto user = userService.createUser(modelMapper.map(userDto,User.class));
        return user;
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
    public UserDto userUpdate(@RequestBody UserDto userDto, @PathVariable Long userId){
        UserDto user = userService.updateUser(userDto,userId);
        return user;
    }
}
