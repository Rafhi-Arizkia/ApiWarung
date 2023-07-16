package com.api.warung.model.repo;

import com.api.warung.model.entitiy.RolesEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepo extends JpaRepository<RolesEntities,Long> {
    RolesEntities findByRolesName(String rolesName);
}
