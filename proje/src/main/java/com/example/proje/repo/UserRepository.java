package com.example.proje.repo;

import com.example.proje.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    User getOneUserByUserName(String userName);

    User getOneUserByEmail(String email);
}