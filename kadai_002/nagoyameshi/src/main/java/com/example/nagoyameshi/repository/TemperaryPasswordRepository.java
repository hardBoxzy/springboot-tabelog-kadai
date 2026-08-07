package com.example.nagoyameshi.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.TemperaryPassword;
import com.example.nagoyameshi.entity.User;

public interface TemperaryPasswordRepository extends JpaRepository< TemperaryPassword, Integer> {
    public TemperaryPassword findByUser(User user);
}