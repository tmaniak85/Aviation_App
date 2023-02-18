package com.smart.aviation.controller;

import com.smart.aviation.dto.TotalCargoDto;
import com.smart.aviation.service.TotalCargoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/TotalCargo")
@AllArgsConstructor
public class TotalCargoController {

    private final TotalCargoService totalCargoService;

    @PostMapping
    public void addTotalCargos(@RequestBody TotalCargoDto[] totalCargoDto) {
        totalCargoService.addTotalCargos(totalCargoDto);
    }

}
