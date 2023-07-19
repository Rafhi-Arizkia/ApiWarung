package com.api.warung.model.entitiy;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_kategori")
@Data
public class CategoryEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_category;
    @Column(name = "category_name", length = 50, nullable = false)
    private String categoryName;
}
