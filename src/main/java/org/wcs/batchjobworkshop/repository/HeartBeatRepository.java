package org.wcs.batchjobworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wcs.batchjobworkshop.entity.HeartBeat;

public interface HeartBeatRepository extends JpaRepository<HeartBeat, Integer> {
}
