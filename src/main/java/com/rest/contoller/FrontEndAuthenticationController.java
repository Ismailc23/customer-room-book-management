package com.rest.contoller;

import com.rest.Entity.LoginUserDto;
import com.rest.Entity.RegisterUserDto;
import com.rest.Entity.User;
import com.rest.Response.LoginResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/app")
@Controller
public class FrontEndAuthenticationController {

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/auth/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "SignUpPage";
    }

    @GetMapping("/auth/login")
    public String showLoginForm() {
        return "LoginPage";
    }

    @PostMapping("/auth/signup")
    public String signup(@ModelAttribute RegisterUserDto registerUserDto, Model model) {
        String url = "http://localhost:8080/registrationMethod";
        ResponseEntity<User> response = restTemplate.postForEntity(url, registerUserDto, User.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return "redirect:/app/auth/login";
        }
        else {
            model.addAttribute("error", "Sign up failed");
            return "SignUpPage";
        }
    }

    @PostMapping("/auth/login")
    public String login(@ModelAttribute LoginUserDto loginUserDto, HttpSession session, Model model) {
        String url = "http://localhost:8080/loginMethod";
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(url, loginUserDto, LoginResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Logged in successful");
            System.out.println("token hehe : "+response.getBody().getToken());
            session.setAttribute("token", response.getBody().getToken());
            return "redirect:/customerForm";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "LoginPage";
        }
    }

}
