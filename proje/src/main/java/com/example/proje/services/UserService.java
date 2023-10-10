package com.example.proje.services;


import com.example.proje.config.SecurityConfig;
import com.example.proje.dto.UserDto;
import com.example.proje.entities.User;
import com.example.proje.repo.UserRepository;
import com.example.proje.security.JwtTokenProvider;
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

    public User createUser(User newUser) {
        return userRepository.save(newUser);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    public void userDelete(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDto updateUser(UserDto userDto, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            return null;
        }
        user.get().setUserName(userDto.getUserName());
        user.get().setPassword(securityConfig.passwordEncoder().encode(userDto.getPassword()));
        userRepository.save(user.get());
        return modelMapper.map(user.get(), UserDto.class);
    }


    public UserDto getOneUser(Long userId) {
        return modelMapper.map(userRepository.findById(userId),UserDto.class);
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.getOneUserByUserName(userName);
    }
}
