package com.wecare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wecare.entity.CoachEntity;

@Repository
public interface CoachRepository extends JpaRepository<CoachEntity, String>{
	Optional<CoachEntity> findByCoachId(String coachId);
}
