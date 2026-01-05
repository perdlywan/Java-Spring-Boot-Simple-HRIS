package com.example.simple_hris.repository;

import com.example.simple_hris.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
