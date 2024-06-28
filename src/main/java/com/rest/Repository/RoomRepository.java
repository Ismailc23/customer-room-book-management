package com.rest.Repository;

import com.rest.Entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    @Query("SELECT r FROM RoomEntity r WHERE r.roomNumber NOT IN " +
            "(SELECT b.room.roomNumber FROM BookingEntity b " +
            "WHERE NOT (b.stayEndDate < :stayStartDate OR b.stayStartDate > :stayEndDate))")
    List<RoomEntity> findAvailableRooms(@Param("stayStartDate") LocalDate stayStartDate, @Param("stayEndDate") LocalDate stayEndDate);

}
