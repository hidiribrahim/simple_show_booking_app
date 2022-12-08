package com.hidir.show.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hidir.show.dto.PurchaseTicketsRequest;
import com.hidir.show.dto.PurchaseTicketsResponse;
import com.hidir.show.dto.ShowDto;
import com.hidir.show.dto.TicketDto;
import com.hidir.show.entity.Show;
import com.hidir.show.entity.Ticket;
import com.hidir.show.exception.SeatBookingException;
import com.hidir.show.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @InjectMocks
    TicketService ticketService;

    @Mock
    TicketRepository ticketRepository;

    @Mock
    ShowService showService;

    @Spy
    ModelMapper mapper = new ModelMapper();

    @Test
    void getTicketsBookedByShowId() {
        List<Ticket> tickets = List.of(new Ticket(1L,"123","A1",1, ZonedDateTime.now()),new Ticket(2L,"123","A2",1,ZonedDateTime.now()));
        when(ticketRepository.findAllByShowNumber(1)).thenReturn(tickets);
        List<TicketDto> ticketToAssert = ticketService.getTicketsBookedByShowId(1);
        assertEquals(2,ticketToAssert.size());
    }

    @Test
    void buyTicketsAvailableSeats() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int showId = 1;
        ShowDto showDto = new ShowDto(1,"showname",1,2,2);
        when(showService.findShowById(showId)).thenReturn(showDto);

        when(ticketRepository.existsByShowNumberAndSeatNumber(anyInt(),anyString())).thenReturn(false);
        when(ticketRepository.existsByPhoneNumberAndShowNumber(anyString(),anyInt())).thenReturn(false);



        PurchaseTicketsRequest request = PurchaseTicketsRequest.builder()
                .phoneNumber("1")
                .showNumber(showId)
                .seats(List.of("A1","A2"))
                .build();
        PurchaseTicketsResponse response = ticketService.buyTickets(request);
        System.out.println();
        verify(showService).findShowById(showId);

        System.out.println(mapper.writeValueAsString(request));

        assertEquals("showname", response.getShowName());
        assertEquals(2,response.getTicketDtoList().size());

        TicketDto ticketResponse = response.getTicketDtoList().get(0);
        assertEquals(showId,ticketResponse.getShowNumber());
        assertEquals("A1",ticketResponse.getSeatNumber());
        ticketResponse = response.getTicketDtoList().get(1);
        assertEquals("A2",ticketResponse.getSeatNumber());
        verify(ticketRepository,times(1)).saveAll(anyList());

    }

    @Test
    void numberAlreadyBooked_throwSeatBookingException() {
        int showId = 1;
        ShowDto showDto = new ShowDto(1,"showname",1,2,2);
        when(showService.findShowById(showId)).thenReturn(showDto);

        when(ticketRepository.existsByPhoneNumberAndShowNumber(anyString(),anyInt())).thenReturn(true);


        PurchaseTicketsRequest request = PurchaseTicketsRequest.builder()
                .phoneNumber("1")
                .showNumber(showId)
                .seats(List.of("A1","A2"))
                .build();
        assertThrows(SeatBookingException.class,()->ticketService.buyTickets(request));
        verify(ticketRepository,never()).saveAll(anyList());

    }

    @Test
    void buyTickets_givenAtLeastUnavailableSeat_throwSeatBookingException() {
        int showId = 1;
        ShowDto showDto = new ShowDto(1,"showname",1,2,2);
        when(showService.findShowById(showId)).thenReturn(showDto);

        when(ticketRepository.existsByShowNumberAndSeatNumber(1,"A1")).thenReturn(false);
        when(ticketRepository.existsByShowNumberAndSeatNumber(1,"A2")).thenReturn(true);


        PurchaseTicketsRequest request = PurchaseTicketsRequest.builder()
                .phoneNumber("1")
                .showNumber(showId)
                .seats(List.of("A1","A2"))
                .build();
        assertThrows(SeatBookingException.class,()->ticketService.buyTickets(request));
        verify(ticketRepository,never()).saveAll(anyList());

    }

    @Test
    void buyTickets_givenSeatExceedingShowBoundaries_throwSeatBookingException() {
        int showId = 1;
        ShowDto showDto = new ShowDto(1,"showname",1,1,2);
        when(showService.findShowById(showId)).thenReturn(showDto);


        PurchaseTicketsRequest request = PurchaseTicketsRequest.builder()
                .phoneNumber("1")
                .showNumber(showId)
                .seats(List.of("A1","A2"))
                .build();
        assertThrows(SeatBookingException.class,()->ticketService.buyTickets(request));
        verify(ticketRepository,never()).saveAll(anyList());

    }

    @Test
    void deleteTicket_beforeWindow_shouldDeleteTicket(){
        int window = 2;
        ZonedDateTime timePurchased = ZonedDateTime.now();
        ShowDto showDto = new ShowDto(1,"showname",1,1,window);
        Ticket ticket = new Ticket(1L,"1","A1",1,timePurchased);
        when(showService.findShowById(1)).thenReturn(showDto);
        when(ticketRepository.findByIdAndPhoneNumber(1L,"1")).thenReturn(Optional.of(ticket));
        ticketService.cancelTicket(1L,"1");
    }

    @Test
    void deleteTicket_afterWindow_shouldThrowSeatBookingException(){
        int window = 2;
        ZonedDateTime timePurchased = ZonedDateTime.now().minusMinutes(3);
        ShowDto showDto = new ShowDto(1,"showname",1,1,window);
        Ticket ticket = new Ticket(1L,"1","A1",1,timePurchased);
        when(showService.findShowById(1)).thenReturn(showDto);
        when(ticketRepository.findByIdAndPhoneNumber(1L,"1")).thenReturn(Optional.of(ticket));
        assertThrows(SeatBookingException.class, ()-> ticketService.cancelTicket(1L,"1"));
    }
}