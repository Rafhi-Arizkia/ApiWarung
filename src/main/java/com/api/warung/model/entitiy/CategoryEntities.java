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
    @Column(name = "category_makanan", length = 50, nullable = false)
    private String categoryMakanan;
    @Column(name = "cataegory_minuman", length =50, nullable = false)
    private String categoryMinuman;
    @Column(name = "category_snack", length = 50, nullable = false)
    private String categorySnack;
}
