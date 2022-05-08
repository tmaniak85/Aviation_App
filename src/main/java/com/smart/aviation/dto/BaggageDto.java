package com.smart.aviation.dto;

import lombok.Data;

@Data
public class BaggageDto {

    private int id;
    private int weight;
    private String weightUnit;
    private int pieces;

}
