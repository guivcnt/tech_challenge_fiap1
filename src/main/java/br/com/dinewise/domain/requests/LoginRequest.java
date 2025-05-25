package br.com.dinewise.domain.requests;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull String login, @NotNull String password) { }
