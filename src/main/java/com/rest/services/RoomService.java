package com.rest.services;

import com.rest.Entity.RoomEntity;
import com.rest.ExceptionHandling.RoomExceptions.RoomNotFoundException;
import com.rest.ExceptionHandling.RoomExceptions.RoomNumberAlreadyExistException;
import com.rest.Repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public RoomEntity createRoom(RoomEntity room){
        if(roomRepository.findById(room.getRoomNumber()).isPresent()) {
            log.debug("Room number already present : {}",room.getRoomNumber());
            throw new RoomNumberAlreadyExistException("Room number "+room.getRoomNumber()+" is already there");
        }
        roomRepository.save(room);
        log.debug("Room Saved successfully");
        return room;
    }

    public List<RoomEntity> findAvailableRooms(LocalDate stayStartDate, LocalDate stayEndDate) {
        return roomRepository.findAvailableRooms(stayStartDate, stayEndDate);
    }

    public Optional<RoomEntity> getRoomById(Long id) {
        Optional<RoomEntity> getRoom = roomRepository.findById(id);
        if(!getRoom.isPresent()) {
            throw new RoomNotFoundException("Room number "+id+" is not present");
        }
        return roomRepository.findById(id);
    }

    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll();
    }

    public RoomEntity updateRoom(RoomEntity room) {
        Optional<RoomEntity> existRoom = roomRepository.findById(room.getRoomNumber());
        if(!existRoom.isPresent()) {
            log.debug("Room is not present");
            throw new RoomNotFoundException("Room number "+room.getRoomNumber()+" is not present to perform update");
        }
        log.debug("Room saved successfully");
        return roomRepository.save(room);
    }

    public boolean deleteRoom(Long roomNumber) {
        if(roomRepository.existsById(roomNumber)) {
            roomRepository.deleteById(roomNumber);
            log.debug("Room is deleted successfully");
            return true;
        }
        else {
            log.debug("Room is not present");
            throw new RoomNotFoundException("Room number "+roomNumber+" is not present to perform deletion");
        }
    }
}
