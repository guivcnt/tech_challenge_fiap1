package br.com.dinewise.domain.requests.menu;

import jakarta.validation.constraints.NotNull;

public record MenuRequest (
        @NotNull String name,
        @NotNull String description,
        @NotNull Double price,
        @NotNull Boolean onlyForDelivery,
        @NotNull String imagePath,
        @NotNull Long restaurantId
) { }
