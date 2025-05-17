package com.client.controller;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.client.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class CentralClientController {
	
	 private final RestTemplate restTemplate = new RestTemplate();
	    private final String BASE_URL = "http://localhost:8100"; //  Change if REST backend runs on a different host/port

	@GetMapping("/")
    public String showRegisterUserForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Renders register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        String url = BASE_URL + "/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> request = new HttpEntity<>(user, headers);
        try {
            restTemplate.postForEntity(url, request, String.class);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed");
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login"; // Renders login.html
    }

    @PostMapping("/loginVal")
    public String loginValidation(@ModelAttribute User user, HttpSession session, Model model) {
        String url = BASE_URL + "/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> request = new HttpEntity<>(user, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();

                session.setAttribute("token", body.get("token")); // Store token
                session.setAttribute("userId", body.get("id"));
                session.setAttribute("email", body.get("email"));
                session.setAttribute("role", body.get("role"));
                session.setAttribute("name", body.get("name"));

                if ("ADMIN".equalsIgnoreCase((String) body.get("role"))) {
                    return "redirect:/admin/adminHome";
                } else {
                    return "redirect:/user/userHome";
                }
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Login failed: " + e.getStatusCode());
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Remove JWT-related data from session
        session.removeAttribute("jwtToken");
        session.removeAttribute("loggedInUser");

        session.invalidate();
        return "redirect:/login";
    }
}