package com.rest.contoller;

import com.rest.Entity.RoomEntity;
import com.rest.Repository.BookingRepository;
import com.rest.services.BookingService;
import com.rest.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;

@Controller
public class CheckingAvailabilityUI {

    @Autowired
    private RoomService roomService;

    @GetMapping("/availabilityCheckForm")
    public String showAvailabilityCheckForm() {
        return "RoomAvailability";
    }

    @PostMapping("/availabilityCheckForm")
    public String checkRoomAvailability(
            @RequestParam("stayStartDate") LocalDate stayStartDate,
            @RequestParam("stayEndDate") LocalDate stayEndDate,
            Model model) {
            List<RoomEntity> availableRooms = roomService.findAvailableRooms(stayStartDate, stayEndDate);
            model.addAttribute("stayStartDate", stayStartDate);
            model.addAttribute("stayEndDate", stayEndDate);
            model.addAttribute("availableRooms", availableRooms);
        return "redirect:/availableRooms";
    }

    @GetMapping("/availableRooms")
    public String availableRooms() {
        return "availableRooms";
    }
}
