package br.com.dinewise.domain.requests;

import jakarta.validation.constraints.NotNull;

public record ChangePasswordRequest (
        @NotNull String oldPassword,
        @NotNull String newPassword
) { }
