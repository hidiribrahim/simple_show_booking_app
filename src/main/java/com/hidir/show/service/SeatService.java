package com.hidir.show.service;

import com.hidir.show.dto.ShowDto;
import com.hidir.show.dto.TicketDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final ShowService showService;
    private final TicketService ticketService;


    public SeatService(ShowService showService, TicketService ticketService) {
        this.showService = showService;
        this.ticketService = ticketService;
    }


    public Set<String> getAvailableSeats(int showNumber) {
        ShowDto show = showService.findShowById(showNumber);

        Map<String, SEAT_AVAILABILITY> seatMap = initSeatMapping(show.getSeatsPerRow(),show.getRowCount());

        Set<String> seatsBooked = ticketService.getTicketsBookedByShowId(showNumber).stream()
                .map(TicketDto::getSeatNumber)
                .collect(Collectors.toSet());

        return seatMap.keySet().stream()
                .filter(seat->!seatsBooked.contains(seat))
                .collect(Collectors.toSet());


    }
    public Map<String, SEAT_AVAILABILITY> getSeatAllocationByShowNumber(int showNumber){

        ShowDto show = showService.findShowById(showNumber);

        Map<String, SEAT_AVAILABILITY> seatMap = initSeatMapping(show.getSeatsPerRow(),show.getRowCount());
        return updateAvailability(showNumber,seatMap);

    }

    private Map<String, SEAT_AVAILABILITY> initSeatMapping(Integer seatsPerRow, Integer rowCount) {

        Map<String, SEAT_AVAILABILITY> seatMap = new HashMap<>();
        char currentRow = 'A';

        for (int i = 1; i <= rowCount; i++) {
            for (int j = 1; j <= seatsPerRow; j++) {
                String seatString = currentRow + Integer.toString(j);
                seatMap.put(seatString, SEAT_AVAILABILITY.AVAILABLE);
            }
            currentRow++;
        }

        return seatMap;

    }

    private Map<String, SEAT_AVAILABILITY> updateAvailability(int showNumber, Map<String, SEAT_AVAILABILITY> seatMap) {
        Set<String> seatsBooked = ticketService.getTicketsBookedByShowId(showNumber).stream()
                .map(TicketDto::getSeatNumber)
                .collect(Collectors.toSet());


        seatsBooked.forEach(seat->seatMap.put(seat, SEAT_AVAILABILITY.BOOKED));

        return seatMap;
    }
    public enum SEAT_AVAILABILITY {
        AVAILABLE, BOOKED
    }


}
