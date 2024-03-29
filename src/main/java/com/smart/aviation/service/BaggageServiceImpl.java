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

    private final BaggageDao baggageDao;

    public Baggage createBaggage(BaggageDto baggageDto) {
        Baggage baggage = new Baggage();
        baggage.setId(baggageDto.getId());
        baggage.setWeight(baggageDto.getWeight());
        baggage.setWeightUnit(baggageDto.getWeightUnit());
        baggage.setPieces(baggageDto.getPieces());
        return baggageDao.save(baggage);
    }

    public List<Baggage> addBaggage(TotalCargoDto totalCargoDto) {
        List<Baggage> baggage = new ArrayList<>();
        List<BaggageDto> baggageListFromTotalCargo = totalCargoDto.getBaggage();
        baggageListFromTotalCargo.forEach(b -> {
            Baggage baggageObj = createBaggage(b);
            baggage.add(baggageObj);
        });
        return baggage;
    }
}
