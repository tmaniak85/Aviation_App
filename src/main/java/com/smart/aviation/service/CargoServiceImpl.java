package com.smart.aviation.service;

import com.smart.aviation.dao.CargoDao;
import com.smart.aviation.dto.CargoDto;
import com.smart.aviation.dto.TotalCargoDto;
import com.smart.aviation.model.Cargo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CargoServiceImpl implements CargoService{

    CargoDao cargoDao;


    public Cargo createCargo(CargoDto cargoDto) {
        Cargo cargo = new Cargo();
        cargo.setId(cargoDto.getId());
        cargo.setWeight(cargoDto.getWeight());
        cargo.setWeightUnit(cargoDto.getWeightUnit());
        cargo.setPieces(cargoDto.getPieces());
        return cargoDao.save(cargo);
    }

    public List<Cargo> addCargos(TotalCargoDto totalCargoDto) {
        List<Cargo> cargo = new ArrayList<>();
        List<CargoDto> cargoListFromTotalCargo = totalCargoDto.getCargo();
        cargoListFromTotalCargo.forEach(c -> {
            Cargo cargoObj = createCargo(c);
            cargo.add(cargoObj);
        });
        return cargo;
    }

}
