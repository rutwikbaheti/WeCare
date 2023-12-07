package com.wecare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wecare.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{
	Optional<UserEntity> findByUserId(String userId);
}
