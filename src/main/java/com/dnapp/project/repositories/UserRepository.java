package com.dnapp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dnapp.project.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    User findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByUserName(@Param("email") String email);
	
}
