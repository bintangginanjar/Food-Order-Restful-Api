package com.food.order.restful.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "foods")
public class FoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String code;

    private String name;
    
    private Integer price;

    @Column(name = "is_ready")
    private Boolean isReady;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "foodEntity", cascade = CascadeType.ALL)
    private List<OrderItemEntity> OrderItemEntities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
    private CategoryEntity categoryEntity;
}
