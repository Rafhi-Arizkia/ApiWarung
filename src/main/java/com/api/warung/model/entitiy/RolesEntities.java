package com.api.warung.model.entitiy;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_roles")
@Data
public class RolesEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_roles;
    @Column(name = "roles_name",length = 20,nullable = false)
    private String rolesName;
    @ManyToMany(mappedBy = "roles")
    private List<UserEntities> userEntities = new ArrayList<>();
}
