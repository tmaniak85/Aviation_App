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

    private final List<BaggageDto> baggageDtoList = new ArrayList<>();

    private final List<CargoDto> cargoDtoList = new ArrayList<>();

    private final List<Baggage> baggageList = new ArrayList<>();

    private final List<Cargo> cargoList = new ArrayList<>();

    @BeforeEach
    void init() {
        Baggage baggage = new Baggage();
        baggage.setId(1);
        baggage.setWeightUnit("lb");
        baggage.setWeight(21);
        baggage.setPieces(5);

        Cargo cargo = new Cargo();
        cargo.setId(1);
        cargo.setWeightUnit("lb");
        cargo.setWeight(21);
        cargo.setPieces(5);

        BaggageDto baggageDto = new BaggageDto();
        baggageDto.setId(1);
        baggageDto.setWeightUnit("lb");
        baggageDto.setWeight(21);
        baggageDto.setPieces(5);

        CargoDto cargoDto = new CargoDto();
        cargoDto.setId(1);
        cargoDto.setWeightUnit("lb");
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
        TotalCargoDto totalCargoDto = new TotalCargoDto();
        totalCargoDto.setBaggage(baggageDtoList);
        totalCargoDto.setCargo(cargoDtoList);
        totalCargoDto.setFlightId(1L);

        TotalCargo totalCargo = new TotalCargo();
        totalCargo.setBaggage(baggageList);
        totalCargo.setCargo(cargoList);
        totalCargo.setFlightId(1L);

        TotalCargoService totalCargoService = new TotalCargoServiceImpl(totalCargoDao, cargoService, baggageService);

        when(cargoService.addCargos(totalCargoDto)).thenReturn(cargoList);
        when(baggageService.addBaggage(totalCargoDto)).thenReturn(baggageList);
//        when
        totalCargoService.createTotalCargo(totalCargoDto);
//        then
        verify(totalCargoDao, times(1)).save(totalCargo);
    }

    @Test
    void shouldCreateTotalCargoObjectsWithDefinedValuesFromArray() {
//        given
        TotalCargoDto totalCargoDto1 = new TotalCargoDto();
        totalCargoDto1.setBaggage(baggageDtoList);
        totalCargoDto1.setCargo(cargoDtoList);
        totalCargoDto1.setFlightId(1L);
        TotalCargoDto totalCargoDto2 = new TotalCargoDto();
        totalCargoDto2.setBaggage(baggageDtoList);
        totalCargoDto2.setCargo(cargoDtoList);
        totalCargoDto2.setFlightId(2L);
        TotalCargoDto[] totalCargoDtoArray = {totalCargoDto1, totalCargoDto2};

        TotalCargoService totalCargoService = mock(TotalCargoServiceImpl.class);
        doNothing().when(totalCargoService).createTotalCargo(any());
        doCallRealMethod().when(totalCargoService).addTotalCargos(any());
//        when
        totalCargoService.addTotalCargos(totalCargoDtoArray);
//        then
        verify(totalCargoService, times(1)).createTotalCargo(totalCargoDto1);
        verify(totalCargoService, times(1)).createTotalCargo(totalCargoDto2);
        verify(totalCargoService, times(2)).createTotalCargo(any());
    }

    @AfterEach
    void clear() {
        baggageDtoList.clear();
        cargoDtoList.clear();
        baggageList.clear();
        cargoList.clear();
    }

}
