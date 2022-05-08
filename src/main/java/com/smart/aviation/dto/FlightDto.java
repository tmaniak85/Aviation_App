package com.smart.aviation.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class FlightDto {

    private long flightId;
    private int flightNumber;
    private String departureAirportIATACode;
    private String arrivalAirportIATACode;
    private OffsetDateTime departureDate;

}
