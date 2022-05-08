package com.smart.aviation.service;

import com.smart.aviation.dto.BaggageDto;
import com.smart.aviation.dto.TotalCargoDto;
import com.smart.aviation.model.Baggage;
import java.util.List;

public interface BaggageService {

    Baggage createBaggage(BaggageDto baggageDto);
    List<Baggage> addBaggages(TotalCargoDto totalCargoDto);

}
