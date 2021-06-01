package com.dnapp.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CONTACT")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	
	private String firstName;
	
	private String secondName;
	
	private String work;
	
	private String email;
	
	private String image;
	
	@Column(length = 1000)
	private String description;
	
	private String phone;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	
	
}
