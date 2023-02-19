package com.smart.aviation.service;

import com.smart.aviation.dao.FlightDao;
import com.smart.aviation.dto.DepartureDateDto;
import com.smart.aviation.dto.FlightDto;
import com.smart.aviation.dto.TotalCargoSumDto;
import com.smart.aviation.exception.FlightNumberDoesNotExistException;
import com.smart.aviation.exception.NoFlightOnRequestedDateException;
import com.smart.aviation.model.Baggage;
import com.smart.aviation.model.Cargo;
import com.smart.aviation.model.Flight;
import com.smart.aviation.model.TotalCargo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock
    private FlightDao flightDao;

    private static Flight flight;

    @BeforeAll
    static void init() {
        Baggage baggage = new Baggage();
        baggage.setId(1);
        baggage.setWeightUnit("kg");
        baggage.setWeight(21);
        baggage.setPieces(5);

        Cargo cargo = new Cargo();
        cargo.setId(1);
        cargo.setWeightUnit("kg");
        cargo.setWeight(21);
        cargo.setPieces(7);

        Baggage baggage2 = new Baggage();
        baggage2.setId(1);
        baggage2.setWeightUnit("kg");
        baggage2.setWeight(22);
        baggage2.setPieces(5);

        Cargo cargo2 = new Cargo();
        cargo2.setId(1);
        cargo2.setWeightUnit("kg");
        cargo2.setWeight(20);
        cargo2.setPieces(7);

        List<Baggage> baggageList = new ArrayList<>();
        baggageList.add(baggage);
        baggageList.add(baggage2);

        List<Cargo> cargoList = new ArrayList<>();
        cargoList.add(cargo);
        cargoList.add(cargo2);

        TotalCargo totalCargo = new TotalCargo();
        totalCargo.setFlightId(1);
        totalCargo.setCargo(cargoList);
        totalCargo.setBaggage(baggageList);

        flight = new Flight();
        flight.setFlightId(1);
        flight.setFlightNumber(200);
        flight.setDepartureDate(OffsetDateTime
                .of(2023, 5, 21, 22, 21, 22, 122, ZoneOffset.UTC));
        flight.setArrivalAirportIATACode("SYD");
        flight.setDepartureAirportIATACode("MEL");
        flight.setTotalCargo(totalCargo);
    }

    @Test
    void shouldCreateFlightWithDefinedValuesFromFlightDto() {
//        given
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightId(1);
        flightDto.setFlightNumber(122);
        flightDto.setDepartureAirportIATACode("SYD");
        flightDto.setArrivalAirportIATACode("GDY");
        flightDto.setDepartureDate(OffsetDateTime
                .of(2023, 5, 21, 22, 21, 22, 122, ZoneOffset.UTC));
        Flight flight = new Flight();
        flight.setFlightId(1);
        flight.setFlightNumber(122);
        flight.setDepartureAirportIATACode("SYD");
        flight.setArrivalAirportIATACode("GDY");
        flight.setDepartureDate(OffsetDateTime
                .of(2023, 5, 21, 22, 21, 22, 122, ZoneOffset.UTC));

        FlightService flightService = new FlightServiceImpl(flightDao);
//        when
        flightService.createFlight(flightDto);
//        then
        verify(flightDao, times(1)).save(flight);
    }

    @Test
    void shouldCreateFlightObjectsWithDefinedValuesFromArray() {
//        given
        FlightDto flightDto = new FlightDto();
        flightDto.setFlightId(1);
        flightDto.setFlightNumber(122);
        flightDto.setDepartureAirportIATACode("SYD");
        flightDto.setArrivalAirportIATACode("GDY");
        flightDto.setDepartureDate(OffsetDateTime
                .of(2023, 5, 21, 22, 21, 22, 122, ZoneOffset.UTC));
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

    @Test
    void shouldReturnCorrectTotalCargoSumDtoValuesForFlightNumberAndDate() {
//        given
        int requestedFlightNumber = 200;
        DepartureDateDto requestedDepartureDateDto = new DepartureDateDto();
        requestedDepartureDateDto.setRequestedDepartureDate("2023-05-21");

        FlightService flightService = new FlightServiceImpl(flightDao);

        TotalCargoSumDto expectedTotalCargoSumDto = new TotalCargoSumDto(43, 41, 84);

        when(flightDao.findAllByFlightNumberIs(requestedFlightNumber)).thenReturn(List.of(flight));
//        when
        TotalCargoSumDto totalCargoSumDto = flightService.showTotalCargoWeightForFlightNumberAndDate(requestedFlightNumber, requestedDepartureDateDto);
//        then
        assertThat(totalCargoSumDto, equalTo(expectedTotalCargoSumDto));
    }

    @Test
    void shouldReturnFlightNumberDoesNotExistException() {
        //        given
        int requestedFlightNumber = 21;
        DepartureDateDto requestedDepartureDateDto = new DepartureDateDto();
        requestedDepartureDateDto.setRequestedDepartureDate("2023-05-21");

        FlightService flightService = new FlightServiceImpl(flightDao);

        when(flightDao.findAllByFlightNumberIs(requestedFlightNumber)).thenReturn(Collections.emptyList());
//        when
//        then
        assertThrows(FlightNumberDoesNotExistException.class, () ->
                flightService.showTotalCargoWeightForFlightNumberAndDate(requestedFlightNumber, requestedDepartureDateDto));
    }

    @Test
    void shouldReturnNoFlightOnRequestedDateException() {
//        given
        int requestedFlightNumber = 200;
        DepartureDateDto requestedDepartureDateDto = new DepartureDateDto();
        requestedDepartureDateDto.setRequestedDepartureDate("2023-05-22");

        FlightService flightService = new FlightServiceImpl(flightDao);

        when(flightDao.findAllByFlightNumberIs(requestedFlightNumber)).thenReturn(List.of(flight));
//        when
//        then
        assertThrows(NoFlightOnRequestedDateException.class, () ->
                flightService.showTotalCargoWeightForFlightNumberAndDate(requestedFlightNumber, requestedDepartureDateDto));
    }

}
