package br.com.dinewise.application.repository;

import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.ChangePasswordRequest;
import br.com.dinewise.domain.requests.LoginRequest;
import br.com.dinewise.domain.requests.UserRequest;

import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> login(LoginRequest request) throws DineWiseResponseError;

    UserEntity createUser(UserRequest request) throws DineWiseResponseError;

    Optional<UserEntity> updateUser(Long userId, UserRequest request) throws DineWiseResponseError;

    Optional<UserEntity> updatePassword(Long userId, ChangePasswordRequest request) throws DineWiseResponseError;

    Optional<UserEntity> deleteUser(Long userId) throws DineWiseResponseError;
}
