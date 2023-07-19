package com.api.warung.model.repo;

import com.api.warung.model.entitiy.MenuEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepo extends JpaRepository<MenuEntities,Long> {
}
