package com.client.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.client.dto.ProductUpdateRequestDTO;
import com.client.dto.PurchaseOrderRequestDTO;
import com.client.dto.VendorProductDTO;
import com.client.model.Product;
import com.client.model.Vendor;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminClientController {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${api.base-url:http://localhost:8100/admin}")
	private String apiBaseUrl;

	// Admin Home
	@GetMapping("/adminHome")
	public String adminHome(HttpSession session) {
		String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        
        
		return "adminHome";
	}

	// Vendor Section
	@GetMapping("/vendor")
	public String vendorPage(HttpSession session) {
		String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        
		return "vendor";
	}

	@GetMapping("/vendor/add")
	public String addVendorForm(Model model,HttpSession session) {
		String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
		model.addAttribute("vendorproductDTO", new VendorProductDTO());
		return "vendorAdd";
	}

	@PostMapping("/vendor/add/save")
	public String saveVendor(@ModelAttribute VendorProductDTO vpDTO, RedirectAttributes redirectAttributes,HttpSession session) {
		String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
//        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        
		String url = apiBaseUrl + "/vendor/add/save";

//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON); // if you're using form-data, else JSON

		// Using postForEntity with DTO object, assuming your REST controller accepts
		// VendorProductDTO as @ModelAttribute or @RequestBody
		try {
			HttpEntity<VendorProductDTO> request = new HttpEntity<>(vpDTO,headers);
			restTemplate.postForEntity(url, request, String.class);
			redirectAttributes.addFlashAttribute("success", "Vendor and Product added successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Failed to add vendor.");
		}

		return "redirect:/admin/vendor/view";
	}

	// METHOD 1

//    @GetMapping("/vendor/view")
//    public String viewVendors(Model model) {
//        String url = apiBaseUrl + "/vendor/view";
//        ResponseEntity<Vendor[]> response = restTemplate.getForEntity(url, Vendor[].class);
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            model.addAttribute("vendors", Arrays.asList(response.getBody()));
//        } else {
//            model.addAttribute("vendors", Collections.emptyList());
//        }
//        return "vendorView";
//    }

	// METHOD 2
	@GetMapping("/vendor/view")
	public String viewVendors(Model model,HttpSession session) {
		String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
//        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
		String url = apiBaseUrl + "/vendor/view";

		// Step 1: Create headers
//		HttpHeaders headers = new HttpHeaders();
//        headers.set("Accept", "application/json"); // optional: sets Accept header
//		headers.setContentType(MediaType.APPLICATION_JSON);
		// headers.set("Authorization", "Bearer your_token_here"); // if auth is needed

		// Step 2: Wrap headers in HttpEntity (no request body for GET)
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		// Step 3: Make exchange call
//        ResponseEntity<Vendor[]> response = restTemplate.exchange(
//            url,
//            HttpMethod.GET,
//            requestEntity,
//            Vendor[].class
//        );
//
//        // Step 4: Process response
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//            model.addAttribute("vendors", Arrays.asList(response.getBody()));
//        } else {
//            model.addAttribute("vendors", Collections.emptyList());
//        }
		ResponseEntity<List<Vendor>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Vendor>>() {
				});
		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			model.addAttribute("vendors", response.getBody());
		} else {
			model.addAttribute("vendors", Collections.emptyList());
		}

		return "vendorView";
	}
	
    @PostMapping("/vendor/delete/{id}")
    public String deleteVendor(@PathVariable Long id, RedirectAttributes redirectAttributes,HttpSession session) {
    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        String url = apiBaseUrl + "/vendor/delete/" + id;
        try {
            restTemplate.postForLocation(url, null);
            redirectAttributes.addFlashAttribute("success", "Vendor deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete vendor.");
        }
        return "redirect:/admin/vendor/view";
    }
    
    // Purchase Section
    @GetMapping("/purchase")
    public String showPurchaseForm(Model model,HttpSession session) {	
    	String token = (String) session.getAttribute("token");
    if (token == null) return "redirect:/login";
    HttpHeaders headers = new HttpHeaders();
    
    
    headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
    HttpEntity<?> requestEntity = new HttpEntity<>(headers);
    
    
        String url = apiBaseUrl + "/vendor/view";
        ResponseEntity<Vendor[]> response = restTemplate.getForEntity(url, Vendor[].class,requestEntity);
        model.addAttribute("vendors", Arrays.asList(Objects.requireNonNullElse(response.getBody(), new Vendor[0])));
        return "purchaseOrder";
    }
    
    @PostMapping("/purchase/add")
    public String submitPurchaseOrder(@RequestParam Long vendorId,
                                      @RequestParam int quantity,
                                      @RequestParam double price,
                                      RedirectAttributes redirectAttributes,HttpSession session) {
    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        
        String url = apiBaseUrl + "/purchase/add";

        PurchaseOrderRequestDTO request = new PurchaseOrderRequestDTO();
        request.setVendorId(vendorId);
        request.setQuantity(quantity);
        request.setPrice(price);

        try {
            restTemplate.postForLocation(url, request,requestEntity); // sends as JSON by default with Jackson
            redirectAttributes.addFlashAttribute("success", "Purchase order submitted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit purchase order.");
        }

        return "redirect:/admin/adminHome";
    }
    
    // Products
    @GetMapping("/products")
    public String viewAllProducts(Model model,HttpSession session) {
    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        String url = apiBaseUrl + "/products";
        ResponseEntity<Product[]> response = restTemplate.getForEntity(url, Product[].class,requestEntity);
        model.addAttribute("products", Arrays.asList(Objects.requireNonNullElse(response.getBody(), new Product[0])));
        return "viewProducts";
    }
    
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String description,
                                RedirectAttributes redirectAttributes,HttpSession session) {
    	String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
//        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        String url = apiBaseUrl + "/products/update/" + id;

        ProductUpdateRequestDTO request = new ProductUpdateRequestDTO();
        request.setName(name);
        request.setDescription(description);
        System.out.println(name);
        System.out.println(description);
        System.out.println("HEHE");
        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ProductUpdateRequestDTO> entity = new HttpEntity<>(request, headers);

            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

            redirectAttributes.addFlashAttribute("success", "Product updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product.");
        }

        return "redirect:/admin/products";
    }
    
    @GetMapping("/invoices")
    public String getAllInvoices(Model model,HttpSession session) {
    	
        return fetchAndLoadInvoices(model, "all",session);
    }

    @GetMapping("/invoices/sales")
    public String getSalesInvoices(Model model,HttpSession session) {

        
        return fetchAndLoadInvoices(model, "SALES",session);
    }

    @GetMapping("/invoices/purchase")
    public String getPurchaseInvoices(Model model,HttpSession session) {

        
        return fetchAndLoadInvoices(model, "PURCHASE",session);
    }

    private String fetchAndLoadInvoices(Model model, String type,HttpSession session) {
        String url = apiBaseUrl + "/invoices";
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // 🔑 Send JWT token
//        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        
//        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                }
        );

        List<Map<String, Object>> allInvoices = response.getStatusCode().is2xxSuccessful() && response.getBody() != null
                ? response.getBody()
                : Collections.emptyList();

        if (!"all".equalsIgnoreCase(type)) {
            allInvoices = allInvoices.stream()
                    .filter(map -> {
                        Map<String, Object> invoice = (Map<String, Object>) map.get("invoice");
                        return type.equalsIgnoreCase((String) invoice.get("type"));
                    }).toList();
        }

        model.addAttribute("invoiceData", allInvoices);
        model.addAttribute("activeFilter", type);
        return "viewInvoices";
    }

}
