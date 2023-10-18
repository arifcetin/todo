package com.example.proje.services;


import com.example.proje.config.SecurityConfig;
import com.example.proje.dto.UserDto;
import com.example.proje.entities.User;
import com.example.proje.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final SecurityConfig securityConfig;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.securityConfig = securityConfig;
    }

    public UserDto createUser(User newUser) {
        User user = userRepository.save(newUser);
        return modelMapper.map(user,UserDto.class);
    }

    public List<UserDto> getUsers() {
        List<UserDto> userDtos = userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        return userDtos;
    }

    public void userDelete(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDto updateUser(User updateUser, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            return null;
        }
        user.get().setUserName(updateUser.getUserName());
        user.get().setEmail(updateUser.getEmail());
        user.get().setPassword(securityConfig.passwordEncoder().encode(updateUser.getPassword()));
        userRepository.save(user.get());
        return modelMapper.map(user.get(), UserDto.class);
    }


    public UserDto getOneUser(Long userId) {
        return modelMapper.map(userRepository.findById(userId),UserDto.class);
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.getOneUserByUserName(userName);
    }

    public User getOneUserByEmail(String email) {
        return userRepository.getOneUserByEmail(email);
    }
}
