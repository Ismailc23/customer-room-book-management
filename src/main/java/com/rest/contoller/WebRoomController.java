package com.rest.contoller;

import com.rest.Entity.RoomEntity;
import com.rest.Repository.RoomRepository;
import com.rest.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class WebRoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @GetMapping("/roomCreation")
    public String showRoomForm(Model model)
    {
        model.addAttribute("room", new RoomEntity());
        return "roomForm";
    }

    @PostMapping("/roomCreation")
    public String submitRoomForm(@ModelAttribute RoomEntity room) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity("http://localhost:8080/request/api/room", room, RoomEntity.class);
        return "redirect:/roomCreation";
    }

    @GetMapping("/roomlist")
    public String showRoomsAdmin(Model model) {
        List<RoomEntity> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "RoomListAdmin";
    }

}
