package com.duva.duva.repository;

import com.duva.duva.entity.Depot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {}


