package com.hidir.show.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;
    private String seatNumber;
    private Integer showNumber;

    @Basic
    private ZonedDateTime timestamp;

    public Ticket(Integer showNumber,String phoneNumber, String seatNumber,ZonedDateTime timestamp) {
        this.phoneNumber = phoneNumber;
        this.seatNumber = seatNumber;
        this.showNumber = showNumber;
        this.timestamp = timestamp;
    }
}
