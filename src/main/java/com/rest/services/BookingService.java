package com.rest.services;

import com.rest.Entity.BookingEntity;
import com.rest.Entity.BookingPatchDTO;
import com.rest.Entity.CustomerEntity;
import com.rest.Entity.RoomEntity;
import com.rest.ExceptionHandling.BookingExceptions.*;
import com.rest.ExceptionHandling.CustomerExceptions.CustomerNotFoundException;
import com.rest.ExceptionHandling.RoomExceptions.RoomNotFoundException;
import com.rest.Repository.BookingRepository;
import com.rest.Repository.CustomerRepository;
import com.rest.Repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookingService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

     public BookingEntity createBookings (Long customerId, Long roomNumber, BookingEntity bookings) {
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);
        log.info("CustomerDetails {}", customer);
        Optional<RoomEntity> room = roomRepository.findById(roomNumber);
         log.info("Room Details {}", room);
         if (customer.isEmpty()) {
             throw new CustomerNotFoundException("Customer is not present with the give id : "+customerId);
         }
         if(room.isEmpty()) {
             throw new RoomNotFoundException("Room is not present with the given room number : "+roomNumber);
         }
         List<BookingEntity> existingBookings = bookingRepository.findOverlapBookings(room.get().getRoomNumber(), bookings.getStayEndDate(),bookings.getStayStartDate());
         if(!existingBookings.isEmpty()) {
             log.debug("Bookings already exist for the given dates : {}" , existingBookings);
             throw new OverlappingDatesException("The provided dates overlaps the already done booking date for the given room number : "+roomNumber);
         }
         if(bookings.getStayStartDate().isBefore(LocalDate.now()) || bookings.getStayEndDate().isBefore(bookings.getStayStartDate())) {
             log.debug("Invalid Dates are given");
             throw new InvalidDateException("The date provided is invalid");
         }
         long daysBetween = ChronoUnit.DAYS.between(bookings.getStayStartDate(), bookings.getStayEndDate());

         if (daysBetween > 30) {
             throw new InvalidDateException("Maximum stay duration is 30 days.");
         }
         bookings.setCustomer(customer.get());
         bookings.setRoom(room.get());
         bookings.setCustomerFirstName(customer.get().getFirstName());
         bookings.setCustomerLastName(customer.get().getLastName());
         bookings.setRoomType(room.get().getType());
         bookings.setRoomPrice(room.get().getPrice());
         bookingRepository.save(bookings);
         log.debug("Bookings is made successfully : {}",bookings);
         return bookings;
    }

    public Optional<BookingEntity> getBookingsByReferenceId(Long referenceId) {
        Optional<BookingEntity> getBooking = bookingRepository.findById(referenceId);
        if(!getBooking.isPresent()) {
            throw new BookingNotFoundException("Booking with the reference ID : "+referenceId+" is not present");
        }
        return getBooking;
    }

    public Optional<BookingEntity> getBookingsByCustomerIdAndRoomNumber(Long customerId, Long roomNumber) {
        Optional<BookingEntity> getBooking =bookingRepository.findByCustomerIdAndRoomNumber(customerId,roomNumber);
        if(!getBooking.isPresent()) {
            throw new BookingNotFoundException("Booking is not present with the provided customer id "+customerId+" and room number "+roomNumber);
        }
        return getBooking;
    }

    public BookingEntity patchBooking(Long id, BookingPatchDTO bookingPatchDTO) {
            Optional<BookingEntity> patchBooking = bookingRepository.findById(id);
            BookingEntity booking = patchBooking.get();
            if (patchBooking.isPresent()) {
                if (bookingPatchDTO.getBookedDate() != null) {
                    booking.setBookedDate(bookingPatchDTO.getBookedDate());
                }
                if (bookingPatchDTO.getStayStartDate() != null) {
                    booking.setStayStartDate(bookingPatchDTO.getStayStartDate());
                }
                if (bookingPatchDTO.getStayEndDate() != null) {
                    booking.setStayEndDate(bookingPatchDTO.getStayEndDate());
                }
                log.info("Patch Dates : Booking Date : {} , Start Date : {} ,  End Date : {}",bookingPatchDTO.getBookedDate(),bookingPatchDTO.getStayStartDate(),bookingPatchDTO.getStayEndDate());
                return bookingRepository.save(booking);
            }
            return null;
    }
}
