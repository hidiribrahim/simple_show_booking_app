package com.hidir.show.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseTicketsRequest {
    @NotNull
    Integer showNumber;
    @NotNull
    String phoneNumber;
    @Size(min=1)
    List<String> seats;
}
