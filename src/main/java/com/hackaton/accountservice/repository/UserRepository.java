package com.hackaton.accountservice.repository;

import com.hackaton.accountservice.model.ProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ProfileModel, Long> {
}
