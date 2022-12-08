package com.hidir.show.controller;

import com.hidir.show.dto.ShowDto;
import com.hidir.show.dto.TicketDto;
import com.hidir.show.service.ShowService;
import com.hidir.show.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Show Endpoints", description = "Endpoints for admin users for booking shows")
public class AdminController {

    private ShowService showService;
    private TicketService ticketService;

    public AdminController(TicketService ticketService, ShowService showService) {
        this.ticketService = ticketService;
        this.showService = showService;
    }



    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/tickets/{showNumber}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TicketDto> getTicketsByShowNumber(@PathVariable Integer showNumber) {
        return ticketService.getTicketsBookedByShowId(showNumber);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/shows",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ShowDto> getAllShows() {
        return showService.getAllShows();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/show",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createShow(@RequestBody @Valid ShowDto showDto) {
        showService.save(showDto);
    }


}