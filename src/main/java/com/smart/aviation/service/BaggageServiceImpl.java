package com.smart.aviation.service;

import com.smart.aviation.dao.BaggageDao;
import com.smart.aviation.dto.BaggageDto;
import com.smart.aviation.dto.TotalCargoDto;
import com.smart.aviation.model.Baggage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BaggageServiceImpl implements BaggageService{

    BaggageDao baggageDao;


    public Baggage createBaggage(BaggageDto baggageDto) {
        Baggage baggage = new Baggage();
        baggage.setId(baggageDto.getId());
        baggage.setWeight(baggageDto.getWeight());
        baggage.setWeightUnit(baggageDto.getWeightUnit());
        baggage.setPieces(baggageDto.getPieces());
        return baggageDao.save(baggage);
    }

    public List<Baggage> addBaggages(TotalCargoDto totalCargoDto) {
        List<Baggage> baggage = new ArrayList<>();
        BaggageDto[] baggageTest = totalCargoDto.getBaggage();
        for(BaggageDto baggageDto : baggageTest) {
            Baggage baggageObj = createBaggage(baggageDto);
            baggage.add(baggageObj);
        }
        return baggage;
    }

}
