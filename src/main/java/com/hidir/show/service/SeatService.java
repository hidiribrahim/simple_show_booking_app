package com.hidir.show.service;

import com.hidir.show.dto.ShowDto;
import com.hidir.show.dto.TicketDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final ShowService showService;


    public SeatService(ShowService showService) {
        this.showService = showService;
    }

    @Cacheable(
            value = "seatsCache",
            key = "#showNumber")
    public Map<String, SEAT_AVAILABILITY> initSeatMapping(int showNumber) {

        ShowDto show = showService.findShowById(showNumber);

        Map<String, SEAT_AVAILABILITY> seatMap = new HashMap<>();

        char currentRow = 'A';

        for (int i = 1; i <= show.getRowCount(); i++) {
            for (int j = 1; j <= show.getSeatsPerRow(); j++) {
                String seatString = currentRow + Integer.toString(j);
                seatMap.put(seatString, SEAT_AVAILABILITY.AVAILABLE);
            }
            currentRow++;
        }

        return seatMap;

    }

    public enum SEAT_AVAILABILITY {
        AVAILABLE, BOOKED
    }


}
