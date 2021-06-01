//package com.dnapp.project.controllers;
//
//import java.security.Principal;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.dnapp.project.model.Contact;
//import com.dnapp.project.model.User;
//import com.dnapp.project.repositories.ContactRepository;
//import com.dnapp.project.repositories.UserRepository;
//
//@RestController
//public class SearchController {
//
//	
//	@Autowired
//	private UserRepository userRepo;
//	@Autowired
//	private ContactRepository contactRepo;
//	
//	@GetMapping("/search/{query}")
//	public ResponseEntity<?> search(@PathVariable String query,Principal principal){
//		
//		User user = userRepo.getUserByUserName(principal.getName());
//		
//		List<Contact> contacts = contactRepo.findByNameContainingAndUser(query, user);
//		
//		return ResponseEntity.ok(contacts);
//	}
//	
//}
