package com.hidir.show.service;

import com.hidir.show.dto.PurchaseTicketsRequest;
import com.hidir.show.dto.PurchaseTicketsResponse;
import com.hidir.show.dto.ShowDto;
import com.hidir.show.dto.TicketDto;
import com.hidir.show.entity.Ticket;
import com.hidir.show.exception.SeatBookingException;
import com.hidir.show.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowService showService;

    private static final int ASCII_OFFSET = 64;

    private static final String ZONE_ID = "Asia/Singapore";

    private ModelMapper mapper;
    public TicketService(TicketRepository ticketRepository, ShowService showService , ModelMapper mapper) {
        this.ticketRepository = ticketRepository;
        this.showService = showService;
        this.mapper = mapper;
    }

    public List<TicketDto> getTicketsBookedByShowId(int showId){
        List<Ticket> tickets = ticketRepository.findAllByShowNumber(showId);
        return tickets.stream()
                .map(ticket->mapper.map(ticket, TicketDto.class))
                .collect(Collectors.toList());
    }

    public PurchaseTicketsResponse buyTickets(PurchaseTicketsRequest request){
        ShowDto showDto = showService.findShowById(request.getShowNumber());
        validateRequest(request,showDto);

        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of(ZONE_ID));

        List<Ticket> ticketEntityList = request.getSeats().stream()
                        .map(seat-> new Ticket(request.getShowNumber(), request.getPhoneNumber(),seat,currentTime))
                        .collect(Collectors.toList());

        ticketRepository.saveAll(ticketEntityList);


        return PurchaseTicketsResponse.builder()
                .ticketDtoList(ticketEntityList.stream()
                        .map(ticket->mapper.map(ticket,TicketDto.class))
                        .collect(Collectors.toList()))
                .showName(showDto.getShowName())
                        .build();

    }

    private void validateRequest(PurchaseTicketsRequest request,ShowDto showDto) {

        if(ticketRepository.existsByPhoneNumberAndShowNumber(request.getPhoneNumber(), showDto.getId())) {
            throw new SeatBookingException("Only 1 phone number per booking");
        }
        request.getSeats().forEach(seat -> {
            validateSeatBounds(seat,showDto.getRowCount(),showDto.getSeatsPerRow());
            validateSeatAvailability(seat,request.getShowNumber());
        });
    }

    private void validateSeatAvailability(String seat, Integer showNumber) {
        if(ticketRepository.existsByShowNumberAndSeatNumber(showNumber,seat)){
            throw new SeatBookingException("Seat is already taken");
        }
    }

    private void validateSeatBounds(String seat, Integer maxRow, Integer maxSeat) {
        int rowNum = Character.toUpperCase(seat.charAt(0)) - ASCII_OFFSET;
        int seatRowNum = Integer.parseInt(seat.substring(1));
        if(rowNum>maxRow || seatRowNum>maxSeat){
            throw new SeatBookingException("No such seat");
        }



    }

    public void cancelTicket(Long ticketNumber, String phoneNumber) {
        Ticket ticket = ticketRepository.findByIdAndPhoneNumber(ticketNumber,phoneNumber).orElseThrow(
                ()-> new SeatBookingException("No such ticket with provided ticket number and phone number"));
        
        validateTicketCancellation(ticket);

        ticketRepository.deleteById(ticketNumber);
    }

    private void validateTicketCancellation(Ticket ticket) {
        int cancellationWindow = showService.findShowById(ticket.getShowNumber())
                .getCancellationWindow();
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of(ZONE_ID));
        long timeDifference = Duration.between(ticket.getTimestamp(),currentTime).toMinutes();
        if(timeDifference>cancellationWindow) {
            throw new SeatBookingException("Failed to cancel ticket as you have exceeded the max cancellation window.");
        }
    }
}
