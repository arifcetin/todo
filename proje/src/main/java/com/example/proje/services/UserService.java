package com.example.proje.services;


import com.example.proje.entities.User;
import com.example.proje.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User newUser) {
        return userRepository.save(newUser);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void userDelete(Long userId) {
        userRepository.deleteById(userId);
    }

    public User updateUser(User updateUser, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            return null;
        }
        user.get().setUserName(updateUser.getUserName());
        user.get().setPassword(updateUser.getPassword());
        userRepository.save(user.get());
        return user.get();
    }


    public User getOneUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.getOneUserByUserName(userName);
    }
}
