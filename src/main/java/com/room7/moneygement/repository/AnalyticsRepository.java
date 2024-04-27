package com.room7.moneygement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.room7.moneygement.model.Analytics;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {

}
