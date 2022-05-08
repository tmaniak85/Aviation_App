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

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    FlightDao flightDao;


    public void addFlights(FlightDto[] flightDtoArray) {
        for(FlightDto flightDto : flightDtoArray) {
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

        List<Flight> flights = flightDao.findAllByFlightNumberIs(flightNumber);

        if(flights.size() == 0) {
            throw new FlightNumberDoesNotExistException("C001-Flight number does not exist in database");
        }

        LocalDate requestedDepartureDate  = convertJsonToLocalDate(departureDateDto);

        double totalCargoWeightInKg = 0;
        double totalBaggageWeightInKg = 0;
        boolean ifFlightExists = false;

        for (Flight flightOnCertainDate : flights) {
            if(flightOnCertainDate.getDepartureDate().getYear() == requestedDepartureDate.getYear()
                    && flightOnCertainDate.getDepartureDate().getMonth() == requestedDepartureDate.getMonth()
                    && flightOnCertainDate.getDepartureDate().getDayOfMonth() == requestedDepartureDate.getDayOfMonth()) {

                ifFlightExists = true;
                TotalCargo totalCargo = flightOnCertainDate.getTotalCargo();

                if(totalCargo == null) {
                    throw new NoTotalCargoException("C002-No total cargo connected with this flight");
                }


                for (Cargo cargo : totalCargo.getCargo()) {
                    if(cargo.getWeightUnit().equals("kg")) {
                        totalCargoWeightInKg += cargo.getWeight();
                    } else if(cargo.getWeightUnit().equals("lb")) {
                        totalCargoWeightInKg += convertLbToKg(cargo.getWeight());
                    }
                }

                for (Baggage baggage : totalCargo.getBaggage()) {
                    if(baggage.getWeightUnit().equals("kg")) {
                        totalBaggageWeightInKg += baggage.getWeight();
                    } else if(baggage.getWeightUnit().equals("lb")) {
                        totalBaggageWeightInKg += convertLbToKg(baggage.getWeight());
                    }
                }
            }
        }

        if(!ifFlightExists) {
            throw new NoFlightOnRequestedDateException("C003-Flight number on requested date does not exist");
        }

        TotalCargoSumDto totalCargoSumDto = new TotalCargoSumDto();
        totalCargoSumDto.setTotalCargoInKg(totalCargoWeightInKg);
        totalCargoSumDto.setTotalBaggageInKg(totalBaggageWeightInKg);
        totalCargoSumDto.setTotalWeightInKg(totalCargoWeightInKg + totalBaggageWeightInKg);
        return totalCargoSumDto;
    }

    public AirportFlightsAndCargoPieces showNumberOfFlightsAndBaggageInPiecesForAirportAndDate(String iataAirportCode, DepartureDateDto departureDateDto) {

        List<Flight> flights = flightDao.findAllByArrivalAirportIATACodeIsOrDepartureAirportIATACodeIs(iataAirportCode, iataAirportCode);

        if(flights.size() == 0) {
            throw new IataAirportCodeDoesNotExistInDatabaseException("C004-Iata airport code does not exist in database");
        }

        LocalDate requestedDepartureDate  = convertJsonToLocalDate(departureDateDto);

        int numberOfDepartingFlights = 0;
        int numberOfArrivingFlights = 0;
        int numberOfArrivingBaggageInPieces = 0;
        int numberOfDepartingBaggageInPieces = 0;
        boolean ifFlightExists = false;

        for (Flight flightOnCertainDate : flights) {
            if(flightOnCertainDate.getDepartureDate().getYear() == requestedDepartureDate.getYear()
                    && flightOnCertainDate.getDepartureDate().getMonth() == requestedDepartureDate.getMonth()
                    && flightOnCertainDate.getDepartureDate().getDayOfMonth() == requestedDepartureDate.getDayOfMonth()) {

                ifFlightExists = true;
                TotalCargo totalCargo = flightOnCertainDate.getTotalCargo();


                if(flightOnCertainDate.getArrivalAirportIATACode().equals(iataAirportCode)) {
                    numberOfArrivingFlights++;

                    for (Baggage baggage : totalCargo.getBaggage()) {
                        numberOfArrivingBaggageInPieces += baggage.getPieces();
                    }
                }

                if(flightOnCertainDate.getDepartureAirportIATACode().equals(iataAirportCode)) {
                    numberOfDepartingFlights++;

                    for (Baggage baggage : totalCargo.getBaggage()) {
                        numberOfDepartingBaggageInPieces += baggage.getPieces();
                    }
                }
            }
        }

        if(!ifFlightExists) {
            throw new NoFlightOnRequestedDateException("C003-Flight number and cargo on requested date does not exist");
        }

        AirportFlightsAndCargoPieces airportFlightsAndCargoPieces = new AirportFlightsAndCargoPieces();
        airportFlightsAndCargoPieces.setNumberOfDepartingFlights(numberOfDepartingFlights);
        airportFlightsAndCargoPieces.setNumberOfArrivingFlights(numberOfArrivingFlights);
        airportFlightsAndCargoPieces.setNumberOfArrivingBaggageInPieces(numberOfArrivingBaggageInPieces);
        airportFlightsAndCargoPieces.setNumberOfDepartingBaggageInPieces(numberOfDepartingBaggageInPieces);
        return airportFlightsAndCargoPieces;
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
}
