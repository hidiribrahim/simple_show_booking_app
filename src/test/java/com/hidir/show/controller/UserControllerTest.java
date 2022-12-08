package com.hidir.show.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hidir.show.dto.PurchaseTicketsRequest;
import com.hidir.show.service.SeatService;
import com.hidir.show.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TicketService ticketService;
    @MockBean
    SeatService seatService;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @Test
    void givenPurchaseTicketRequest_returnCreated() throws Exception {
        PurchaseTicketsRequest purchaseTicketsRequest = PurchaseTicketsRequest.builder().showNumber(1).phoneNumber("1").seats(List.of("A1")).build();

        mockMvc.perform(post("/user/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(purchaseTicketsRequest)))
                .andExpect(status().isCreated());


    }

    @Test
    void givenShowId_return200() throws Exception {

        Mockito.when(seatService.getAvailableSeats(anyInt())).thenReturn(anySet());
        mockMvc.perform(get("/user/seats/1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteTicket_givenTicketNumAndPhoneNum_return200() throws Exception {

        mockMvc.perform(delete("/user/ticket/1/phoneNumber/1"))
                .andExpect(status().is2xxSuccessful());
    }
}
