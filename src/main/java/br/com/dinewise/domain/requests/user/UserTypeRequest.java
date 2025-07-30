package br.com.dinewise.domain.requests.user;

import jakarta.validation.constraints.NotNull;

public record UserTypeRequest(
        @NotNull String userType
) {
}
