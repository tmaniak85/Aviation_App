package com.smart.aviation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalCargoSumDto {

    private double totalBaggageInKg;
    private double totalCargosInKg;
    private double totalWeightInKg;

}
