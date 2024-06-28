package com.rest.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "room")
public class RoomEntity {

    @Id
    private long roomNumber;

    private int price;
    private String type;
    private String description;
    private String photoUrl;
}
