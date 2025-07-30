package br.com.dinewise.domain.requests.restaurant;

import br.com.dinewise.domain.requests.address.AddressRequest;
import jakarta.validation.constraints.NotNull;

public record RestaurantRequest(
        @NotNull String name,
        @NotNull String typeCuisine,
        @NotNull String userLoginOwner,
        @NotNull String operationHours,
        @NotNull AddressRequest address
) {
}
