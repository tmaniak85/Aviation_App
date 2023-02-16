package com.smart.aviation.service;

import com.smart.aviation.dao.TotalCargoDao;
import com.smart.aviation.dto.BaggageDto;
import com.smart.aviation.dto.CargoDto;
import com.smart.aviation.dto.TotalCargoDto;
import com.smart.aviation.model.Baggage;
import com.smart.aviation.model.Cargo;
import com.smart.aviation.model.TotalCargo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TotalCargoServiceImplTest {

    @Mock
    private CargoService cargoService;

    @Mock
    private BaggageService baggageService;

    @Mock
    private TotalCargoDao totalCargoDao;

    private Baggage baggage;

    private BaggageDto baggageDto;

    private Cargo cargo;

    private CargoDto cargoDto;

    private List<BaggageDto> baggageDtoList = new ArrayList<>();

    private List<CargoDto> cargoDtoList = new ArrayList<>();

    private List<Baggage> baggageList = new ArrayList<>();

    private List<Cargo> cargoList = new ArrayList<>();

    @BeforeEach
    void init() {
        baggage = new Baggage();
        baggage.setId(1);
        baggage.setWeightUnit("lbs");
        baggage.setWeight(21);
        baggage.setPieces(5);

        cargo = new Cargo();
        cargo.setId(1);
        cargo.setWeightUnit("lbs");
        cargo.setWeight(21);
        cargo.setPieces(5);

        baggageDto = new BaggageDto();
        baggageDto.setId(1);
        baggageDto.setWeightUnit("lbs");
        baggageDto.setWeight(21);
        baggageDto.setPieces(5);

        cargoDto = new CargoDto();
        cargoDto.setId(1);
        cargoDto.setWeightUnit("lbs");
        cargoDto.setWeight(21);
        cargoDto.setPieces(5);

        baggageDtoList.add(baggageDto);
        cargoDtoList.add(cargoDto);

        baggageList.add(baggage);
        cargoList.add(cargo);
    }

    @Test
    void shouldCreateTotalCargoWithDefinedValuesFromTotalCargoDto() {
//        given
        TotalCargoService totalCargoService = new TotalCargoServiceImpl(totalCargoDao, cargoService, baggageService);

        TotalCargoDto totalCargoDto = new TotalCargoDto();
        totalCargoDto.setBaggage(baggageDtoList);
        totalCargoDto.setCargo(cargoDtoList);
        totalCargoDto.setFlightId(1L);

        TotalCargo totalCargo = new TotalCargo();
        totalCargo.setBaggage(baggageList);
        totalCargo.setCargo(cargoList);
        totalCargo.setFlightId(1L);

        when(cargoService.addCargos(totalCargoDto)).thenReturn(cargoList);
        when(baggageService.addBaggage(totalCargoDto)).thenReturn(baggageList);
//        when
        totalCargoService.createTotalCargo(totalCargoDto);
//        then
        verify(totalCargoDao, times(1)).save(totalCargo);
    }

    @AfterEach
    void clear() {
        baggageDtoList.clear();
        cargoDtoList.clear();
        baggageList.clear();
        cargoList.clear();
    }

}
