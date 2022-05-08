package com.smart.aviation.service;

import com.smart.aviation.dto.CargoDto;
import com.smart.aviation.dto.TotalCargoDto;
import com.smart.aviation.model.Cargo;
import java.util.List;

public interface CargoService {

    Cargo createCargo(CargoDto cargoDto);
    List<Cargo> addCargos(TotalCargoDto totalCargoDto);

}
