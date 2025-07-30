package br.com.dinewise.domain.requests.address;

import jakarta.validation.constraints.NotNull;

public record AddressRequest(
        @NotNull String street,
        @NotNull String houseNumber,
        @NotNull String complement,
        @NotNull String neighborhood,
        @NotNull String city,
        @NotNull String state,
        @NotNull String zipCode
) {
}
