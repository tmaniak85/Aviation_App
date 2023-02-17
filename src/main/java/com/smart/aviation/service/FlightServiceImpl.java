package com.smart.aviation.service;

import com.smart.aviation.dao.FlightDao;
import com.smart.aviation.dto.AirportFlightsAndCargoPieces;
import com.smart.aviation.dto.DepartureDateDto;
import com.smart.aviation.dto.FlightDto;
import com.smart.aviation.dto.TotalCargoSumDto;
import com.smart.aviation.exception.*;
import com.smart.aviation.model.Baggage;
import com.smart.aviation.model.Cargo;
import com.smart.aviation.model.Flight;
import com.smart.aviation.model.TotalCargo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightDao flightDao;

    public void addFlights(FlightDto[] flightDtoArray) {
        for (FlightDto flightDto : flightDtoArray) {
            createFlight(flightDto);
        }
    }

    public void createFlight(FlightDto flightDto) {
        Flight flight = new Flight();
        flight.setFlightId(flightDto.getFlightId());
        flight.setFlightNumber(flightDto.getFlightNumber());
        flight.setDepartureAirportIATACode(flightDto.getDepartureAirportIATACode());
        flight.setArrivalAirportIATACode(flightDto.getArrivalAirportIATACode());
        flight.setDepartureDate(flightDto.getDepartureDate());
        flightDao.save(flight);
    }

    public TotalCargoSumDto showTotalCargoWeightForFlightNumberAndDate(int flightNumber, DepartureDateDto departureDateDto) {

        List<Flight> flights = getFlightsByFlightNumber(flightNumber);
        LocalDate requestedDepartureDate = convertJsonToLocalDate(departureDateDto);

        double totalCargosWeightInKg = 0;
        double totalBaggageWeightInKg = 0;

        flights = getFlightsOnCertainDate(flights, requestedDepartureDate);

        if (flights.size() > 0) {
            for (Flight flightOnCertainDate : flights) {
                TotalCargo totalCargo = getTotalCargoOnCertainDate(flightOnCertainDate);
                totalCargosWeightInKg += getTotalCargosWeightInKg(totalCargosWeightInKg, totalCargo);
                totalBaggageWeightInKg += getTotalBaggageWeightInKg(totalBaggageWeightInKg, totalCargo);
            }
        } else {
            throw new NoFlightOnRequestedDateException("C003-Flight number on requested date does not exist");
        }

        double totalWeightInKg = totalCargosWeightInKg + totalBaggageWeightInKg;

        return new TotalCargoSumDto(totalBaggageWeightInKg, totalCargosWeightInKg, totalWeightInKg);
    }

    public AirportFlightsAndCargoPieces showNumberOfFlightsAndBaggageInPiecesForAirportAndDate(String iataAirportCode, DepartureDateDto departureDateDto) {

        List<Flight> flights = getFlightsWithRequiredArrivalAndDepartureIATACode(iataAirportCode);
        LocalDate requestedDepartureDate = convertJsonToLocalDate(departureDateDto);

        int numberOfDepartingFlights;
        int numberOfArrivingFlights;
        int numberOfArrivingBaggageInPieces;
        int numberOfDepartingBaggageInPieces;

        flights = getFlightsOnCertainDate(flights, requestedDepartureDate);

        if (flights.size() > 0) {
            numberOfArrivingFlights = getNumberOfArrivingFlightsForIATACode(iataAirportCode, flights);
            numberOfArrivingBaggageInPieces = getNumberOfArrivingBaggageInPiecesForIATACode(iataAirportCode, flights);
            numberOfDepartingFlights = getNumberOfDepartingFlightsForIATACode(iataAirportCode, flights);
            numberOfDepartingBaggageInPieces = getNumberOfDepartingBaggageInPiecesForIATACode(iataAirportCode, flights);
        } else {
            throw new NoFlightOnRequestedDateException("C003-Flight number and cargo on requested date does not exist");
        }

        return new AirportFlightsAndCargoPieces(numberOfDepartingFlights, numberOfArrivingFlights,
                numberOfArrivingBaggageInPieces, numberOfDepartingBaggageInPieces);
    }

    private List<Flight> getFlightsByFlightNumber(int flightNumber) {
        List<Flight> flights = flightDao.findAllByFlightNumberIs(flightNumber);

        if (flights.size() == 0) {
            throw new FlightNumberDoesNotExistException("C001-Flight number does not exist in database");
        }
        return flights;
    }

    private List<Flight> getFlightsOnCertainDate(List<Flight> flights, LocalDate requestedDepartureDate) {
        return flights.stream().filter(flight -> flight.getDepartureDate().getYear() == requestedDepartureDate.getYear()
                && flight.getDepartureDate().getMonth() == requestedDepartureDate.getMonth()
                && flight.getDepartureDate().getDayOfMonth() == requestedDepartureDate.getDayOfMonth())
                .collect(Collectors.toList());
    }

    private List<Flight> getFlightsWithRequiredArrivalAndDepartureIATACode(String iataAirportCode) {
        List<Flight> flights = flightDao.findAllByArrivalAirportIATACodeIsOrDepartureAirportIATACodeIs(iataAirportCode, iataAirportCode);

        if (flights.size() == 0) {
            throw new IataAirportCodeDoesNotExistInDatabaseException("C004-Iata airport code does not exist in database");
        }
        return flights;
    }

    private TotalCargo getTotalCargoOnCertainDate(Flight flightOnCertainDate) {
        return Optional.of(flightOnCertainDate.getTotalCargo())
                .orElseThrow(() -> new NoTotalCargoException("C002-No total cargo connected with this flight"));
    }

    private double getTotalBaggageWeightInKg(double totalBaggageWeightInKg, TotalCargo totalCargo) {
        totalBaggageWeightInKg += totalCargo.getBaggage().stream()
                .filter(b -> b.getWeightUnit().equals("kg"))
                .map(Baggage::getWeight)
                .reduce(Integer::sum)
                .orElse(0);
        totalBaggageWeightInKg += totalCargo.getBaggage().stream()
                .filter(b -> b.getWeightUnit().equals("lb"))
                .map(Baggage::getWeight)
                .map(this::convertLbToKg)
                .reduce(Double::sum)
                .orElse(0d);
        return totalBaggageWeightInKg;
    }

    private double getTotalCargosWeightInKg(double totalCargosWeightInKg, TotalCargo totalCargo) {
        totalCargosWeightInKg += totalCargo.getCargo().stream()
                .filter(c -> c.getWeightUnit().equals("kg"))
                .map(Cargo::getWeight)
                .reduce(Integer::sum)
                .orElse(0);
        totalCargosWeightInKg += totalCargo.getCargo().stream()
                .filter(c -> c.getWeightUnit().equals("lb"))
                .map(Cargo::getWeight)
                .map(this::convertLbToKg)
                .reduce(Double::sum)
                .orElse(0d);
        return totalCargosWeightInKg;
    }

    private double convertLbToKg(int weightInLb) {
        return weightInLb * 0.45359237;
    }

    private LocalDate convertJsonToLocalDate(DepartureDateDto departureDateDto) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(departureDateDto.getRequestedDepartureDate(), formatter);
        } catch (Exception e) {
            throw new NoDateOrWrongDateFormat("No date or bad date format");
        }
    }

    private int getNumberOfDepartingBaggageInPiecesForIATACode(String iataAirportCode, List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getDepartureAirportIATACode().equals(iataAirportCode))
                .map(flight -> this.getNumberOfBaggageInPieces(flight.getTotalCargo()))
                .reduce(Integer::sum).orElse(0);
    }

    private int getNumberOfDepartingFlightsForIATACode(String iataAirportCode, List<Flight> flights) {
        return (int) flights.stream()
                .filter(flight -> flight.getDepartureAirportIATACode().equals(iataAirportCode))
                .count();
    }

    private int getNumberOfArrivingBaggageInPiecesForIATACode(String iataAirportCode, List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getArrivalAirportIATACode().equals(iataAirportCode))
                .map(flight -> this.getNumberOfBaggageInPieces(flight.getTotalCargo()))
                .reduce(Integer::sum).orElse(0);
    }

    private int getNumberOfArrivingFlightsForIATACode(String iataAirportCode, List<Flight> flights) {
        return (int) flights.stream()
                .filter(flight -> flight.getArrivalAirportIATACode().equals(iataAirportCode))
                .count();
    }

    private int getNumberOfBaggageInPieces(TotalCargo totalCargo) {
        return totalCargo.getBaggage()
                .stream()
                .map(Baggage::getPieces)
                .reduce(Integer::sum)
                .orElse(0);
    }
}
