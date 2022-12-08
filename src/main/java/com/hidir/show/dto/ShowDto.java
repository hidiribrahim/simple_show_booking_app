package com.hidir.show.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class ShowDto {

    @NotNull
    private Integer id;
    @NotNull
    private String showName;
    @NotNull
    @Max(value = 26,message = "Maximum row is 26")
    private Integer rowCount;
    @NotNull
    @Max(value = 10,message = "Maximum seats per row is 10")
    private Integer seatsPerRow;
    @NotNull
    private Integer cancellationWindow;
}
