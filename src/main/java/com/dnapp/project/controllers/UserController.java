package com.dnapp.project.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dnapp.project.helper.Message;
import com.dnapp.project.model.Contact;
import com.dnapp.project.model.User;
import com.dnapp.project.repositories.ContactRepository;
import com.dnapp.project.repositories.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	private List<Contact> contacts;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		
		User user = userRepository.getUserByUserName(userName);
		
		model.addAttribute("user", user);
	}
	
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		
		model.addAttribute("title", "Dashboard");
		return "normal/user_dashboard";
	}
	
	@GetMapping("/add-contact")
	public String getAddContact(Model model) {
		
		model.addAttribute("title", "Dodaj kontakt");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, 
				@RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session) {
		
		try {
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);
		
		if(file.isEmpty()) {
			contact.setImage("contact_image.png");
			
		} else {
			contact.setImage(file.getOriginalFilename());
			
			File saveFile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(), path ,StandardCopyOption.REPLACE_EXISTING);
			
		}
		
		user.getContacts().add(contact);	
		contact.setUser(user);
		
		userRepository.save(user);
		session.setAttribute("message", new Message("Kontakt został dodany! ", "success"));
		
		} catch(Exception e) {
			System.out.println("Błąd "+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Coś poszło nie tak, spróbuj ponownie ", "danger"));
		}
		
		return "normal/add_contact";
	}
	
	@GetMapping("/show-contact/{page}")
	public String showContacts(@PathVariable("page")Integer page, Model model, Principal principal) {
		model.addAttribute("title", "Kontakty");
		
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		
		Pageable pageable = PageRequest.of(page, 5);
		Page<Contact> contacts = contactRepository.findContactsByUser(user.getId(), pageable);
		
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages",contacts.getTotalPages());
		
		return "normal/show_contact";
	}
	
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable Integer cId, Model model, Principal principal) {
		
		System.out.println("CID "+ cId);
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		
		if(contact.getUser().getId() == user.getId()) {
			
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getFirstName());
		}
		return "normal/contact_detail";
	}
	
	@RequestMapping("/delete/{cId}")
	public String deleteContact(@PathVariable int cId, Model model) {
		
		Optional<Contact> contactOptional = contactRepository.findById(cId);
		Contact contact = contactOptional.get();		
		
		contact.setUser(null);
		
		contactRepository.delete(contact);
		
		return "redirect:/user/show-contact/0";
	}

	@RequestMapping(value = "/update/{cId}", method = RequestMethod.POST)
	public String updateForm(@PathVariable int cId, Model model) {
		
		Contact contact = contactRepository.findById(cId).get();
		
		model.addAttribute("contact", contact);
		model.addAttribute("title", "Aktualizuj kontakt");
		return "normal/update_form";
	}
	
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateProfile(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model model, HttpSession session, Principal principal) {
		
		try {
			
			Contact oldContact = this.contactRepository.findById(contact.getCId()).get();
			
			if(!file.isEmpty()) {
				
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				Files.copy(file.getInputStream(), path ,StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				
			} else {
				contact.setImage(oldContact.getImage());
			}
			
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Pomyslnie zaktualizowano!" , "success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/"+contact.getCId()+"/contact";
	}
	
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String yourProfile(Model model)	{
		
		model.addAttribute("title", "Twój profil");
		return "normal/settings";
	}
	
	
	
}
