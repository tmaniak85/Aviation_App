package com.smart.aviation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirportFlightsAndCargoPieces {

    private int numberOfDepartingFlights;
    private int numberOfArrivingFlights;
    private int numberOfArrivingBaggageInPieces;
    private int numberOfDepartingBaggageInPieces;

}
