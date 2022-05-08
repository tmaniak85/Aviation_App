package com.smart.aviation.service;

import com.smart.aviation.dto.AirportFlightsAndCargoPieces;
import com.smart.aviation.dto.DepartureDateDto;
import com.smart.aviation.dto.FlightDto;
import com.smart.aviation.dto.TotalCargoSumDto;

public interface FlightService {

    void addFlights(FlightDto[] flightDtoArray);
    void createFlight(FlightDto flightDto);
    TotalCargoSumDto showTotalCargoWeightForFlightNumberAndDate(int flightNumber, DepartureDateDto departureDateDto);
    AirportFlightsAndCargoPieces showNumberOfFlightsAndBaggageInPiecesForAirportAndDate(String iataAirportCode, DepartureDateDto departureDateDto);

}
