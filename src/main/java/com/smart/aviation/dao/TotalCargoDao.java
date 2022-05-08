package com.smart.aviation.dao;

import com.smart.aviation.model.TotalCargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalCargoDao extends JpaRepository<TotalCargo, Long> {
}
