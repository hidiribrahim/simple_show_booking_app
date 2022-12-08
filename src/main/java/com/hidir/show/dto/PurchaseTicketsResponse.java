package com.hidir.show.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseTicketsResponse {
    @JsonProperty("tickets")
    List<TicketDto> ticketDtoList;
    String showName;

}
