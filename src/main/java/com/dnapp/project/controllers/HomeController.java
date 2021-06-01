package com.dnapp.project.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dnapp.project.helper.Message;
import com.dnapp.project.model.User;
import com.dnapp.project.repositories.UserRepository;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepo;
	
	@RequestMapping("/home")
	public String home(Model model) {
		
		model.addAttribute("title", "Strona główna");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		
		model.addAttribute("title", "O projekcie");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signUp(Model model) {
		
		model.addAttribute("title", "Rejestracja");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
		@RequestParam(value="agreement", defaultValue = "false") boolean agreement, Model model, 
		HttpSession session) {
			
		try {
			if(!agreement) {
				System.out.println("nie zaznaczes checkbozxa");
				throw new Exception("Musisz zaakcepotować regulamin!");
			}
			
			if(result1.hasErrors()) {
				System.out.println("ERROR "+ result1.toString());
				//model.addAttribute("user", user);
				return "signup";
			}
			
			User existingUser = userRepo.findByEmail(user.getEmail());
			
			if(existingUser == null) {
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User result = userRepo.save(user);
			
			model.addAttribute("user", new User());
			
			session.setAttribute("message", new Message("Rejestracja ukończona pomyślnie!" , "alert-success"));

			return "signup";
			
			} else throw new Exception("Użytkownik o podanym adresie E-mail już istnieje!");
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Coś poszło nie tak, " + e.getMessage(), "alert-danger"));
		}
		
		return "signup";
		
	}
	
	@GetMapping("/signin")
	public String login(Model model) {
		
		model.addAttribute("title", "Logowanie");
		
		return "login";
	}
	
	
}
