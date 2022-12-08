package com.hidir.show.controller;

import com.hidir.show.dto.PurchaseTicketsRequest;
import com.hidir.show.dto.PurchaseTicketsResponse;
import com.hidir.show.service.TicketService;
import com.hidir.show.service.SeatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Tag(name = "User Show Endpoints", description = "Endpoints for public users for booking shows")
public class UserController {



    private TicketService ticketService;
    private SeatService seatService;

    public UserController(TicketService ticketService, SeatService seatService) {
        this.ticketService = ticketService;
        this.seatService = seatService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/tickets",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public PurchaseTicketsResponse purchaseTickets(@RequestBody @Valid PurchaseTicketsRequest request) {
        return ticketService.buyTickets(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/seats/{showNumber}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<String> getSeatsByShowNumber(@PathVariable Integer showNumber) {
        return seatService.getAvailableSeats(showNumber);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/ticket/{ticketNumber}/phoneNumber/{phoneNumber}")
    public void cancelTicket(@PathVariable Long ticketNumber,@PathVariable String phoneNumber) {
        ticketService.cancelTicket(ticketNumber,phoneNumber);
    }
}
