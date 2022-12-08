package com.hidir.show.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketDto {
    @NotNull
    private Long id;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String seatNumber;
    @NotNull
    private Integer showNumber;
}
