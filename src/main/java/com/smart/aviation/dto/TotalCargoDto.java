package com.smart.aviation.dto;

import lombok.Data;

@Data
public class TotalCargoDto {

    private Long flightId;
    private BaggageDto[] baggage;
    private CargoDto[] cargo;

}
