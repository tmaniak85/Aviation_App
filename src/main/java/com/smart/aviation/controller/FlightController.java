package com.smart.aviation.controller;

import com.smart.aviation.dto.AirportFlightsAndCargoPieces;
import com.smart.aviation.dto.DepartureDateDto;
import com.smart.aviation.dto.FlightDto;
import com.smart.aviation.dto.TotalCargoSumDto;
import com.smart.aviation.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Flight")
@AllArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public void addFlights(@RequestBody FlightDto[] flightDto) {
        flightService.addFlights(flightDto);
    }

    @PostMapping("/{flightNumber}")
    public TotalCargoSumDto showTotalCargoWeightForFlightNumberAndDate(@PathVariable(value = "flightNumber") int flightNumber, @RequestBody DepartureDateDto departureDateDto) {
        return flightService.showTotalCargoWeightForFlightNumberAndDate(flightNumber, departureDateDto);
    }

    @PostMapping("/Iata/{IATAAirportCode}")
    public AirportFlightsAndCargoPieces showNumberOfFlightsAndBaggageInPiecesForAirportAndDate(@PathVariable(value = "IATAAirportCode")String iataAirportCode, @RequestBody DepartureDateDto departureDateDto) {
        return flightService.showNumberOfFlightsAndBaggageInPiecesForAirportAndDate(iataAirportCode, departureDateDto);
    }

}
