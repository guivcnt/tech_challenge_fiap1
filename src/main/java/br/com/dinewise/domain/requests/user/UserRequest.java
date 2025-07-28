package br.com.dinewise.domain.requests.user;

import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull String name,
        @NotNull String email,
        @NotNull String login,
        @NotNull String password,
        @NotNull UserTypeEnum userType,
        @NotNull String street,
        @NotNull String houseNumber,
        @NotNull String complement,
        @NotNull String neighborhood,
        @NotNull String city,
        @NotNull String state,
        @NotNull String zipCode ) { }
