package com.lnt.fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lnt.fullstack.model.EcoReport;

@Repository
public interface EcoReportRepository extends JpaRepository<EcoReport, Long> {
}