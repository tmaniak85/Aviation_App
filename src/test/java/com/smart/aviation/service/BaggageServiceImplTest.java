package com.smart.aviation.service;

import com.smart.aviation.dao.BaggageDao;
import com.smart.aviation.dto.BaggageDto;
import com.smart.aviation.dto.TotalCargoDto;
import com.smart.aviation.model.Baggage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaggageServiceImplTest {

    @Mock
    private BaggageDao baggageDao;

    private Baggage baggage;

    private BaggageDto baggageDto;

    @BeforeEach
    void init() {
        baggageDto = new BaggageDto();
        baggageDto.setId(1);
        baggageDto.setWeightUnit("lb");
        baggageDto.setWeight(21);
        baggageDto.setPieces(5);
        baggage = new Baggage();
        baggage.setId(1);
        baggage.setWeightUnit("lb");
        baggage.setWeight(21);
        baggage.setPieces(5);
        when(baggageDao.save(baggage)).thenReturn(baggage);
    }

    @Test
    void shouldCreateBaggageWithDefinedValues() {
//        given
        BaggageService baggageService = new BaggageServiceImpl(baggageDao);
//        when
        Baggage resultBaggage = baggageService.createBaggage(baggageDto);
//        then
        assertThat(resultBaggage, notNullValue());
        verify(baggageDao, times(1)).save(baggage);
        assertThat(resultBaggage.getId(), equalTo(baggageDto.getId()));
        assertThat(resultBaggage.getWeightUnit(), equalTo(baggageDto.getWeightUnit()));
        assertThat(resultBaggage.getWeight(), equalTo(baggageDto.getWeight()));
        assertThat(resultBaggage.getPieces(), equalTo(baggageDto.getPieces()));
    }

    @Test
    void shouldCreateListOfBaggageFromTotalCargo() {
//        given
        List<BaggageDto> baggageDtoListForTotalCargo = new ArrayList<>();
        baggageDtoListForTotalCargo.add(baggageDto);
        TotalCargoDto totalCargoDto = new TotalCargoDto();
        totalCargoDto.setBaggage(baggageDtoListForTotalCargo);
        BaggageService baggageService = new BaggageServiceImpl(baggageDao);
        when(baggageDao.save(baggage)).thenReturn(baggage);
//        when
        List<Baggage> baggageList = baggageService.addBaggage(totalCargoDto);
//        then
        assertThat(baggageList, hasSize(1));
        assertThat(baggageList.get(0).getId(), equalTo(baggageDto.getId()));
        assertThat(baggageList.get(0).getWeightUnit(), equalTo(baggageDto.getWeightUnit()));
        assertThat(baggageList.get(0).getWeight(), equalTo(baggageDto.getWeight()));
        assertThat(baggageList.get(0).getPieces(), equalTo(baggageDto.getPieces()));
    }

}
