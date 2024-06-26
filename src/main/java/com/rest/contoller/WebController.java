package com.rest.contoller;

import com.rest.Entity.UserEntity;
import com.rest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String viewLoginPage()
    {
        return "LoginPage";
    }

    @GetMapping("/signUp")
    public String registerForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "SignUpPage";
    }

    @PostMapping("/registrationMethod")
    public String registrationProcess(UserEntity user, Model model) {
        if (userRepository.existsByUserName(user.getUserName())) {
            model.addAttribute("errorMessage", "Username already exists");
            model.addAttribute("user", user);
            return "SignUpPage";
        }
        userRepository.save(user);
        return "LoginPage";
    }

    @PostMapping("/loginMethod")
    public String loginMethod(@RequestParam("username") String username, @RequestParam("password") String password, Model model){
        Optional<UserEntity> user = userRepository.findByUserName(username);
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            model.addAttribute("errorMessage", "Invalid username or password");
            return "LoginPage";
        }
        else {
            return "RoomAvailabilty";
        }
    }

    @GetMapping("/roomAvailability")
    public String viewRoomAvailabilityPage() {
        return "RoomAvailabilty";
    }
}
