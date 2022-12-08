package com.hidir.show.repository;

import com.hidir.show.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findAllByShowNumber(Integer showNumber);
    boolean existsByShowNumberAndSeatNumber(Integer showNumber,String seatNumber);
    boolean existsByPhoneNumberAndShowNumber(String phoneNumber,Integer showNumber);

    Optional<Ticket> findByIdAndPhoneNumber(Long ticketNumber,String phoneNumber);
}
