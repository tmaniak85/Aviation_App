package com.smart.aviation.service;

import com.smart.aviation.dto.TotalCargoDto;



public interface TotalCargoService {

    void createTotalCargo(TotalCargoDto totalCargoDto);
    void addTotalCargos(TotalCargoDto[] totalCargoDto);

}
