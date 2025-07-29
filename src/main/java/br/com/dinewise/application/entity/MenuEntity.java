package br.com.dinewise.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntity {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean onlyForDelivery;
    private String imagePath;
    private Long restaurantId;
    private LocalDateTime lastDateModified;
}
