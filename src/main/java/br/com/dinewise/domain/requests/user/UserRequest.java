package br.com.dinewise.domain.requests.user;

import br.com.dinewise.domain.requests.address.AddressRequest;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotNull String name,
        @NotNull String email,
        @NotNull String login,
        @NotNull String password,
        @NotNull String userType,
        @NotNull AddressRequest address
) {
}
