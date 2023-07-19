package com.api.warung.model.entitiy;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_menu")
@Data
public class MenuEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_menu;
    private byte[] image;
    @Column(name = "nama_menu",length = 50,nullable = false)
    private String nameMenu;
    @Column(name = "description_menu",length = 150,nullable = false)
    private String descriptionMenu;
    private Double priceMenu;
    @ManyToOne
    @JoinColumn(name = "id_category")
    private CategoryEntities categoryEntities;
}
