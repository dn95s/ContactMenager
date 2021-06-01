package com.dnapp.project.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dnapp.project.model.Contact;
import com.dnapp.project.model.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{

	@Query("FROM Contact c WHERE c.user.id = :userId")
	public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pageable);
	
//	
//	@Query
//	public List<Contact> findByNameContainingAndUser(String name, User user);
	
	
}
