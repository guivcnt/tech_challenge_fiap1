package br.com.dinewise.domain.requests.user;

import jakarta.validation.constraints.NotNull;

public record ChangeUserTypeRequest (
    @NotNull UserTypeEnum userType
) { }
