package com.client.controller;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.client.model.Product;
import com.client.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserClientController {

	private final RestTemplate restTemplate = new RestTemplate();

    private final String BASE_URL = "http://localhost:8100/user"; // Update port if needed

    @GetMapping("/products")
    public String viewAllProducts(HttpSession session, Model model) {
    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                BASE_URL + "/products",
                HttpMethod.GET,
                requestEntity,

                new ParameterizedTypeReference<List<Product>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("products", response.getBody());
            return "viewProductUser";
        }

        return "error"; // Generic error view
    }

    @GetMapping("/userHome")
    public String userHome(HttpSession session, Model model) {
    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(
                BASE_URL + "/userHome",
                HttpMethod.GET,
                requestEntity,
                User.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("user", response.getBody());
            return "userHome";
        }

        return "error";
    }

    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            @RequestParam int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        try {
            
            ResponseEntity<String> response = restTemplate.exchange(
                    BASE_URL + "/cart/add/" + productId + "?quantity=" + quantity,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            redirectAttributes.addFlashAttribute("success", response.getBody());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error: " + ex.getMessage());
        }

        return "redirect:/user/products";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                BASE_URL + "/cart",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            model.addAttribute("cart", responseBody.get("cart"));
            model.addAttribute("totalAmount", responseBody.get("totalAmount"));
            return "viewCart";
        }

        return "error";
    }

    @PostMapping("/cart/placeOrder")
    public String placeOrder(HttpSession session) {
    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        restTemplate.exchange(
                BASE_URL + "/cart/orders",
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        return "redirect:/user/orders";
    }

    @GetMapping("/orders")
    public String viewUserOrders(HttpSession session, Model model) {
    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                BASE_URL + "/orders",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> invoiceData = response.getBody();
            model.addAttribute("invoiceData", invoiceData);
            return "viewOrders";
        }

        return "error";
    }
//    private HttpHeaders createHeaders(User user, HttpSession session) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + session.getAttribute("token")); // 👈 Custom header
//        return headers;
//    }
}
