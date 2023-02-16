package com.smart.aviation.service;

import com.smart.aviation.dao.CargoDao;
import com.smart.aviation.dto.CargoDto;
import com.smart.aviation.dto.TotalCargoDto;
import com.smart.aviation.model.Cargo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargoServiceImplTest {

    @Mock
    private CargoDao cargoDao;

    private Cargo cargo;

    private CargoDto cargoDto;

    @BeforeEach
    void init() {
        cargoDto = new CargoDto();
        cargoDto.setId(1);
        cargoDto.setWeightUnit("lbs");
        cargoDto.setWeight(21);
        cargoDto.setPieces(5);
        cargo = new Cargo();
        cargo.setId(1);
        cargo.setWeightUnit("lbs");
        cargo.setWeight(21);
        cargo.setPieces(5);
        when(cargoDao.save(cargo)).thenReturn(cargo);
    }

    @Test
    void shouldCreateCargoWithDefinedValues() {
//        given
        CargoService cargoService = new CargoServiceImpl(cargoDao);
//        when
        Cargo resultCargo = cargoService.createCargo(cargoDto);
//        then
        assertThat(resultCargo, notNullValue());
        verify(cargoDao, times(1)).save(cargo);
        assertThat(resultCargo.getId(), equalTo(cargoDto.getId()));
        assertThat(resultCargo.getWeightUnit(), equalTo(cargoDto.getWeightUnit()));
        assertThat(resultCargo.getWeight(), equalTo(cargoDto.getWeight()));
        assertThat(resultCargo.getPieces(), equalTo(cargoDto.getPieces()));
    }

    @Test
    void shouldCreateListOfCargosFromTotalCargo() {
//        given
        CargoDto[] arrayCargoDto = {cargoDto};
        TotalCargoDto totalCargoDto = new TotalCargoDto();
        totalCargoDto.setCargo(arrayCargoDto);
        CargoService cargoService = new CargoServiceImpl(cargoDao);
        when(cargoDao.save(cargo)).thenReturn(cargo);
//        when
        List<Cargo> cargos = cargoService.addCargos(totalCargoDto);
//        then
        assertThat(cargos, hasSize(1));
        assertThat(cargos.get(0).getId(), equalTo(cargoDto.getId()));
        assertThat(cargos.get(0).getWeightUnit(), equalTo(cargoDto.getWeightUnit()));
        assertThat(cargos.get(0).getWeight(), equalTo(cargoDto.getWeight()));
        assertThat(cargos.get(0).getPieces(), equalTo(cargoDto.getPieces()));
    }
}
