package com.smart.aviation.service;

import com.smart.aviation.dao.TotalCargoDao;
import com.smart.aviation.dto.TotalCargoDto;
import com.smart.aviation.model.Baggage;
import com.smart.aviation.model.Cargo;
import com.smart.aviation.model.TotalCargo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TotalCargoServiceImpl implements TotalCargoService {

    TotalCargoDao totalCargoDao;
    CargoService cargoService;
    BaggageService baggageService;


    public void createTotalCargo(TotalCargoDto totalCargoDto) {
        TotalCargo totalCargo = new TotalCargo();
        totalCargo.setFlightId(totalCargoDto.getFlightId());
        List<Cargo> cargo = new ArrayList<>(cargoService.addCargos(totalCargoDto));
        totalCargo.setCargo(cargo);
        List<Baggage> baggage = new ArrayList<>(baggageService.addBaggage(totalCargoDto));
        totalCargo.setBaggage(baggage);
        totalCargoDao.save(totalCargo);
    }

    public void addTotalCargos(TotalCargoDto[] totalCargoDto) {
        for (TotalCargoDto cargoDto : totalCargoDto) {
            createTotalCargo(cargoDto);
        }
    }

}
