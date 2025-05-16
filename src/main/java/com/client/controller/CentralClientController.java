package com.client.controller;

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

import com.client.dto.LoginResponseDTO;
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
            ResponseEntity<LoginResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                LoginResponseDTO.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                LoginResponseDTO loginResponse = response.getBody();

                // Creating a lightweight session user object
                User sessionUser = new User();
                sessionUser.setName(loginResponse.getName());
                sessionUser.setEmail(loginResponse.getEmail());
                sessionUser.setId(loginResponse.getId());

                String role = loginResponse.getRole();

                if ("ADMIN".equalsIgnoreCase(role)) {
                    session.setAttribute("admin", sessionUser);
                    return "redirect:/admin/adminHome";
                } else if ("USER".equalsIgnoreCase(role)) {
                    session.setAttribute("loggedInUser", sessionUser);
                    return "redirect:/user/userHome";
                }
            } else {
                model.addAttribute("error", "Invalid credentials");
            }

        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Login failed: " + e.getStatusCode());
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error occurred");
        }

        return "login"; // return to login page with error
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        String url = BASE_URL + "/logout";
        try {
            restTemplate.postForEntity(url, null, String.class);
        } catch (Exception e) {
            // Ignore errors; session will still be invalidated locally
        }

        session.invalidate();
        return "redirect:/login";
    }
}