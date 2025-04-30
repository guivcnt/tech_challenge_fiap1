package br.com.dinewise.domain.requests;

public record UserRequest(
        String name,
        String email,
        String login,
        String password,
        String userType,
        String street,
        String houseNumber,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode ) { }
