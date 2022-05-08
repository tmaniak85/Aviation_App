package com.smart.aviation.dao;

import com.smart.aviation.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoDao extends JpaRepository<Cargo, Long> {
}
