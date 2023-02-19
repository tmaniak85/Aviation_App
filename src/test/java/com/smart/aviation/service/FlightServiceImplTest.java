package com.smart.aviation.service;

import com.smart.aviation.dao.FlightDao;
import com.smart.aviation.dto.FlightDto;
import com.smart.aviation.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock
    private FlightDao flightDao;

    private Flight flight;

    private FlightDto flightDto;

    @BeforeEach
    void init() {
        flightDto = new FlightDto();
        flightDto.setFlightId(1);
        flightDto.setFlightNumber(122);
        flightDto.setDepartureAirportIATACode("SYD");
        flightDto.setArrivalAirportIATACode("GDY");
        flightDto.setDepartureDate(OffsetDateTime
                .of(2023, 5, 21, 22, 21, 22, 122, ZoneOffset.UTC));
        flight = new Flight();
        flight.setFlightId(1);
        flight.setFlightNumber(122);
        flight.setDepartureAirportIATACode("SYD");
        flight.setArrivalAirportIATACode("GDY");
        flight.setDepartureDate(OffsetDateTime
                .of(2023, 5, 21, 22, 21, 22, 122, ZoneOffset.UTC));
    }

    @Test
    void shouldCreateFlightWithDefinedValuesFromFlightDto() {
//        given
        FlightService flightService = new FlightServiceImpl(flightDao);
//        when
        flightService.createFlight(flightDto);
//        then
        verify(flightDao, times(1)).save(flight);
    }

    @Test
    void shouldCreateFlightObjectsWithDefinedValuesFromArray() {
//        given
        FlightDto flightDto2 = new FlightDto();
        flightDto2.setFlightId(2);
        flightDto2.setFlightNumber(200);
        flightDto2.setDepartureAirportIATACode("SYD");
        flightDto2.setArrivalAirportIATACode("KRK");
        flightDto2.setDepartureDate(OffsetDateTime
                .of(2023, 5, 22, 22, 21, 22, 122, ZoneOffset.UTC));
        FlightDto[] flightDtoArray = {flightDto, flightDto2};

        FlightService flightService = mock(FlightServiceImpl.class);
        doNothing().when(flightService).createFlight(any());
        doCallRealMethod().when(flightService).addFlights(any());
//        when
        flightService.addFlights(flightDtoArray);
//        then
        verify(flightService, times(1)).createFlight(flightDto);
        verify(flightService, times(1)).createFlight(flightDto2);
        verify(flightService, times(2)).createFlight(any());
    }

}
