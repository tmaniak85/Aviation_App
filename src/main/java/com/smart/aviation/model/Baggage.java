package com.smart.aviation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "baggage_table")
@Data
public class Baggage {

    @Id
    @GeneratedValue
    @JsonIgnore
    private long baggageId;
    @Column(nullable = false)
    private int id;
    @Column(nullable = false)
    private int weight;
    @Column(nullable = false)
    private String weightUnit;
    @Column(nullable = false)
    private int pieces;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private TotalCargo flight;

}
