package com.example.demo.model.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.UserApplication;

public interface UserRepository extends JpaRepository<UserApplication, Long> {
	UserApplication findByUsername(String username);
}
