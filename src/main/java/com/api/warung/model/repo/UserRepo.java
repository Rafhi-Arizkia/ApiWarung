package com.api.warung.model.repo;

import com.api.warung.model.entitiy.UserEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntities,Long> {
    Optional<UserEntities> findByUserEmail(String userEmail);
}
