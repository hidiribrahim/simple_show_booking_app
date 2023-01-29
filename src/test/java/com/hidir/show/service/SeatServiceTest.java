package com.hidir.show.service;

import com.hidir.show.dto.ShowDto;
import com.hidir.show.dto.TicketDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @InjectMocks
    SeatService seatService;

    @Mock
    ShowService showService;

    @Mock
    TicketService ticketService;



    @Test
    void shouldGenerateSeatByShowBounds() {
        int showNumber = 1;
        int rowCount = 2;
        int seatPerRow = 10;
        int expectedSeatCount = rowCount * seatPerRow;


        ShowDto showDto = new ShowDto(showNumber,"the show",rowCount,seatPerRow,2);
        List<TicketDto> mockTicketDtos = List.of(
                new TicketDto(1L,"1","A1",1),
                new TicketDto(2L,"1","A4",1)
        );
        when(showService.findShowById(showNumber)).thenReturn(showDto);
        Map<String, SeatService.SEAT_AVAILABILITY> mapResponse = seatService.initSeatMapping(showNumber);

        assertEquals(true,mapResponse.containsKey("A1"));
        assertEquals(true,mapResponse.containsKey("A10"));
        assertEquals(expectedSeatCount,mapResponse.size());
        assertEquals(false,mapResponse.containsKey("A11"));
        assertEquals(false,mapResponse.containsKey("C1"));

    }




}