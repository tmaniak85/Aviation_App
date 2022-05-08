package com.smart.aviation.dao;

import com.smart.aviation.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FlightDao extends JpaRepository<Flight, Long> {

    List<Flight> findAllByFlightNumberIs(int flightNumber);
    List<Flight> findAllByArrivalAirportIATACodeIsOrDepartureAirportIATACodeIs(String arrivalAirportIataCode, String departureAirportIataCode);

}
