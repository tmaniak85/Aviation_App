package com.smart.aviation.dto;

import lombok.Data;

@Data
public class AirportFlightsAndCargoPieces {

    private int numberOfDepartingFlights;
    private int numberOfArrivingFlights;
    private int numberOfArrivingBaggageInPieces;
    private int numberOfDepartingBaggageInPieces;

}
