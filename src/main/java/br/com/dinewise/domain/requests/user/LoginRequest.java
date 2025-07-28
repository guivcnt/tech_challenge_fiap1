package br.com.dinewise.domain.requests.user;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String login, @NotNull String password) { }
