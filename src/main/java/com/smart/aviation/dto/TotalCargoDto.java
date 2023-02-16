package com.smart.aviation.dto;

import lombok.Data;

import java.util.List;

@Data
public class TotalCargoDto {

    private Long flightId;
    private List<BaggageDto> baggage;
    private List<CargoDto> cargo;

}
