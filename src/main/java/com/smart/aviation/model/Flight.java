package com.smart.aviation.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "flight_table")
@Data
public class Flight {

    @Id
    private long flightId;
    @Column(nullable = false)
    private int flightNumber;
    @Column(nullable = false)
    private String departureAirportIATACode;
    @Column(nullable = false)
    private String arrivalAirportIATACode;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Column(name = "departure_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime departureDate;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "flight_id")
    private TotalCargo totalCargo;

}
