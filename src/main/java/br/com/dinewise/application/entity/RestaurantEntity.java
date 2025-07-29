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
public class RestaurantEntity {
    private Long id;
    private String name;
    private String typeCuisine;
    private String operationHours;
    private Long userIdOwner;
    private Long addressId;
    private LocalDateTime lastDateModified;
}
