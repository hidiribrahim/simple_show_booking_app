package com.hidir.show.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hidir.show.dto.ShowDto;
import com.hidir.show.dto.TicketDto;
import com.hidir.show.entity.Show;
import com.hidir.show.exception.ApiError;
import com.hidir.show.service.ShowService;
import com.hidir.show.service.TicketService;
import com.hidir.show.service.SeatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminController.class)
@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SeatService seatService;
    @MockBean
    TicketService ticketService;

    @MockBean
    ShowService showService;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void givenShowNum_return200() throws Exception {
        List<TicketDto> tickets = List.of(new TicketDto(1L,"123","A1",1),new TicketDto(2L,"123","A2",1));
        when(ticketService.getTicketsBookedByShowId(1)).thenReturn(tickets);

        mockMvc.perform(get("/admin/tickets/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"phoneNumber\":\"123\",\"seatNumber\":\"A1\",\"showNumber\":1},{\"id\":2,\"phoneNumber\":\"123\",\"seatNumber\":\"A2\",\"showNumber\":1}]"));


    }

    @Test
    void givenShow_returnCreated() throws Exception {
        Show show = new Show(1,"the show",1,10,2);
        mockMvc.perform(post("/admin/show")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(show)))
                .andExpect(status().isCreated());
    }
    @Test
    void givenShowExceedRowAndSeat_getValidationMessagesAndException() throws Exception {
        Show show = new Show(1,"the show",27,11,2);
        MvcResult res = mockMvc.perform(post("/admin/show")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(show)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andReturn();

        ApiError error = mapper.readValue(res.getResponse().getContentAsString(),ApiError.class);
        assertEquals("Validation errors",error.getMesage());
        assertEquals(2,error.getSubErrors().size());
        assertEquals("Maximum row is 26",error.getSubErrors().get("rowCount"));
        assertEquals("Maximum seats per row is 10",error.getSubErrors().get("seatsPerRow"));

    }

    @Test
    void getShows_return200() throws Exception {
        List<ShowDto> shows = List.of(new ShowDto(1,"the show",1,10,2),new ShowDto(2,"the second show",2,10,2));
        when(showService.getAllShows()).thenReturn(shows);
        mockMvc.perform(get("/admin/shows"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("[{\"id\":1,\"showName\":\"the show\",\"rowCount\":1,\"seatsPerRow\":10,\"cancellationWindow\":2},{\"id\":2,\"showName\":\"the second show\",\"rowCount\":2,\"seatsPerRow\":10,\"cancellationWindow\":2}]"));


    }

}