package com.rest.Repository;

import com.rest.Entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    @Query("SELECT a FROM BookingEntity a WHERE a.customer.customerId = :customerId AND a.room.roomNumber = :roomNumber")
    Optional<BookingEntity> findByCustomerIdAndRoomNumber(@Param("customerId") Long customerId, @Param("roomNumber") Long roomNumber);

    @Query("SELECT b FROM BookingEntity b WHERE b.room.roomNumber = :roomNumber AND b.stayStartDate <= :stayEndDate AND b.stayEndDate >= :stayStartDate")
    List<BookingEntity> findOverlapBookings(@Param("roomNumber") Long roomNumber, @Param("stayEndDate") LocalDate stayEndDate, @Param("stayStartDate") LocalDate stayStartDate);

}
