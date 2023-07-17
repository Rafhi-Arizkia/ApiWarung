package com.api.warung.model.entitiy;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_order")
@Data
public class OrderEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id_order;
    @Column(name = "order_date" , nullable = false)
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntities userOrder;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuEntities menuEntities;
}
