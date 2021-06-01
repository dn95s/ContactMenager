 package com.dnapp.project.model;

import java.util.ArrayList; 
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "Pole nie może być puste")
	@Size(min = 2, max = 20, message = "Imię nie może być krótsze, niż 2 znaki")
	private String name;
	
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Podany adres nie jest adresem E-mail")
	@NotBlank(message = "Pole nie może być puste")
	@Column(unique = true)
	private String email;
	
	@NotBlank(message = "Pole nie może być puste")
	private String password;
	
	@Column(length = 500)
	private String about;
	
	private String role;
	
	private Boolean enabled;
	
	private String imageUrl;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();
	
}
