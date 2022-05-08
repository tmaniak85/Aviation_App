package com.smart.aviation.model;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "total_cargo_table")
@Data
public class TotalCargo {

    @Id
    private long flightId;
    @OneToMany
    private List<Baggage> baggage;
    @OneToMany
    private List<Cargo> cargo;

}
