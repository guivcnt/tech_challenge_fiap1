package br.com.dinewise.application.repository;

import br.com.dinewise.application.entity.UserEntity;
import br.com.dinewise.application.exception.DineWiseResponseError;
import br.com.dinewise.domain.requests.user.ChangePasswordRequest;
import br.com.dinewise.domain.requests.user.LoginRequest;
import br.com.dinewise.domain.requests.user.UserRequest;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity createUser(UserRequest request, Long userType) throws DineWiseResponseError;

    Optional<UserEntity> login(LoginRequest request) throws DineWiseResponseError;

    List<UserEntity> getAll();

    Optional<UserEntity> get(Long userId) throws DineWiseResponseError;

    Optional<UserEntity> get(String login) throws DineWiseResponseError;

    Optional<UserEntity> updateUser(Long userId, UserRequest request, Long userType) throws DineWiseResponseError;

    Optional<UserEntity> updatePassword(Long userId, ChangePasswordRequest request) throws DineWiseResponseError;

    Optional<UserEntity> updateUserType(Long userId, Long userType) throws DineWiseResponseError;

    Optional<UserEntity> deleteUser(Long userId) throws DineWiseResponseError;

}
