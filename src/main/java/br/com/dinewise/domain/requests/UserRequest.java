package br.com.dinewise.domain.requests;

import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull String name,
        @NotNull String email,
        @NotNull String login,
        @NotNull String password,
        @NotNull String userType,
        @NotNull String street,
        @NotNull String houseNumber,
        @NotNull String complement,
        @NotNull String neighborhood,
        @NotNull String city,
        @NotNull String state,
        @NotNull String zipCode ) { }
