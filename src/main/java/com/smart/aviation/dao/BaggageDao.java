package com.smart.aviation.dao;

import com.smart.aviation.model.Baggage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaggageDao extends JpaRepository<Baggage, Long> {
}
