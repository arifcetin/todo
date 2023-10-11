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

    public UserDto updateUser(UserDto userDto, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            return null;
        }
        user.get().setUserName(userDto.getUserName());
        user.get().setPassword(securityConfig.passwordEncoder().encode(userDto.getPassword()));
        userRepository.save(user.get());
        UserDto userDto1 =  modelMapper.map(user.get(), UserDto.class);
        return userDto1;
    }


    public UserDto getOneUser(Long userId) {
        return modelMapper.map(userRepository.findById(userId),UserDto.class);
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.getOneUserByUserName(userName);
    }
}
