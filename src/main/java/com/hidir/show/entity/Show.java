package com.hidir.show.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="shows")
public class Show {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String showName;
    private Integer rowCount;
    private Integer seatsPerRow;
    private Integer cancellationWindow;

    public Show(String showName, Integer rowCount, Integer seatsPerRow, Integer cancellationWindow) {
        this.showName = showName;
        this.rowCount = rowCount;
        this.seatsPerRow = seatsPerRow;
        this.cancellationWindow = cancellationWindow;
    }
}
